package me.tecc.tropica.features.collection;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import me.tecc.tropica.TUtil;
import me.tecc.tropica.Tropica;
import me.tecc.tropica.features.playerData.PlayerWrapper;
import me.tecc.tropica.sidebar.Sidebar;
import me.tecc.tropica.storage.CollectionContainer;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.Sound;
import org.bukkit.configuration.MemorySection;
import org.bukkit.craftbukkit.v1_15_R1.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

import static java.util.Map.Entry.comparingByValue;
import static java.util.stream.Collectors.toMap;

/**
 * This class has been made to handle collections in more
 * depth way like controlling the collect items and increase
 * collections.
 *
 * This class also works together with:
 * {@link CollectionFeature}
 */
public class CollectionManager {
    // the instance
    private static CollectionManager collectionManager;
    // the feature
    private final CollectionFeature collectionCommand;
    // executor
    private final ExecutorService executorService = Executors.newCachedThreadPool();

    // leaderboard cache
    private final LoadingCache<Material, CollectionLeaderboard> leaderboardCache = CacheBuilder.newBuilder()
            .expireAfterWrite(10, TimeUnit.MINUTES)
            .build(new CacheLoader<Material, CollectionLeaderboard>() {

                @Override
                public CollectionLeaderboard load(Material material) throws Exception {
                    final CollectionLeaderboard collectionLeaderboard = new CollectionLeaderboard(material);
                    CollectionContainer collectionContainer = CollectionContainer.getInstance();

                    Collection<String> uuids = new ArrayList<>(Collections.emptyList());
                    uuids.addAll(collectionContainer.config.getKeys(false));

                    Map<UUID, Double> map = new HashMap<>();

                    for (String uuid : uuids) {
                        Object o = collectionContainer.config.get(uuid);
                        if (o instanceof HashMap) {
                            HashMap<String, Double> hashMap = (HashMap<String, Double>) collectionContainer.config.get(uuid);
                            map.put(UUID.fromString(uuid), hashMap.getOrDefault(material.toString(), 0D));
                        } else {
                            MemorySection memorySection = (MemorySection) collectionContainer.config.get(uuid);
                            map.put(UUID.fromString(uuid), (Double) memorySection.getValues(false).getOrDefault(material.toString(), 0D));
                        }
                    }

                    Map<UUID, Double> sorted;
                    sorted = map
                            .entrySet()
                            .stream()
                            .sorted(comparingByValue())
                            .collect(toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e2, LinkedHashMap::new));
                    Map<UUID, Double> sorted2;
                    sorted2 = sorted
                            .entrySet()
                            .stream()
                            .sorted(Collections.reverseOrder(comparingByValue()))
                            .collect(toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e2, LinkedHashMap::new));

                    Map<UUID, Integer> places = new HashMap<>();

                    List<String> itemLore = new ArrayList<>();
                    itemLore.add("");
                    itemLore.add("&r &r &r &r &r &3&lTOP 10");
                    for (int i = 0; i < 10; i++) {
                        itemLore.add("&f"+(i+1)+". &8???");
                    }

                    final List<UUID> list = new ArrayList<>();
                    for (int i = 0; i < sorted2.size(); i++) {

                        for (Map.Entry<UUID, Double> m : sorted2.entrySet()) {
                            UUID uuid = m.getKey();
                            Double d = m.getValue();

                            if (!list.contains(uuid)) {
                                if (i < 10) {
                                    OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(uuid);
                                    itemLore.set(i + 2, "&f"+(i+1)+". &e"+offlinePlayer.getName()+" &7- &a"+TUtil.toFancyDouble(Math.ceil(d)));
                                }

                                places.put(uuid, i + 1);
                                list.add(uuid);
                                break;
                            }
                        }
                    }

                    collectionLeaderboard.setLore(itemLore);
                    collectionLeaderboard.setPlaces(places);


                    return collectionLeaderboard;
                }
            });

    /**
     * Constructor of the class.
     * Registers the command and create new
     * instance of {@link CollectionFeature}
     */
    public CollectionManager() {
        collectionManager = this;
        this.collectionCommand = new CollectionFeature();

        // registration of the command
        Tropica.getTropica().getCommand("collection").setExecutor(collectionCommand);
    }

    /**
     * A method that handles the semi-event which is called
     * in {@link me.tecc.tropica.events.BasicEventHandler}.
     *
     * Responsible for controlling the increasement of
     * player collections and proper way of storing them.
     * Sends a fancy message on encountering a new item.
     *
     * @see Consumer
     * @see me.tecc.tropica.storage.AbstractContainer
     *
     * @param player the target player
     * @param itemStack the item
     */
    public void handleEvent(Player player, ItemStack itemStack) {
        // getting basic data
        final Material material = itemStack.getType();
        final UUID uuid = player.getUniqueId();
        final String path = uuid.toString();
        final int amount = itemStack.getAmount();
        final String originalName;

        if (itemStack.getType() == Material.PLAYER_HEAD) {
            return;
        }

        // getting the original name of item
        net.minecraft.server.v1_15_R1.ItemStack itemStack1 = CraftItemStack.asNMSCopy(itemStack.clone());
        originalName = itemStack1.getItem().g(itemStack1).getLegacyString();

        // getting the collection
        CollectionContainer.getInstance().getAsync(path, null,
                new Consumer<Object>() {
                    @Override
                    public void accept(Object o) {
                        final MemorySection memorySection;
                        final Map<String, Double> map;

                        if (o != null) {
                            if (o instanceof HashMap) {
                                map = (Map<String, Double>) o;
                            } else {
                                map = new HashMap<>();
                                memorySection = (MemorySection) o;
                                memorySection.getValues(false).forEach((key, value) -> map.put(key, (double) value));
                            }
                        } else {
                            map = new HashMap<>();
                        }

                        // the amount of collection
                        double collection = map.getOrDefault(material.toString(), 0D);

                        // check for the new item
                        if (collection < 1) {
                            // fancy message and sounds
                            TextComponent textComponent = new TextComponent(TUtil.toColor(
                                    "\n&r &r &r &r &r &a&l◆ &2&lNEW COLLECTION UNLOCKED &a&l◆\n"+
                                            "&r &r &r&7You've unlocked &f&n"+originalName+"\n"+
                                            "&r &r &r&7Your reward: &b+1 Knowledge Point"+
                                            "\n\n"+
                                            "&r &r &r&7[&eClick here to open your collection menu&7]\n"
                            ));
                            textComponent.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
                                    new ComponentBuilder(TUtil.toColor("&bClick here to open!")).create()));
                            textComponent.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/collection "+material.toString()));

                            player.spigot().sendMessage(textComponent);
                            player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_BELL, 1.0f, 2.0f);

                            PlayerWrapper playerWrapper = new PlayerWrapper(player);
                            int knowledge = playerWrapper.getInt("knowledge") + 1;
                            playerWrapper.setInt("knowledge", knowledge);

                            Sidebar.updateKnowledge(playerWrapper, 1);
                        }
                        // increasing the collection
                        collection += amount;
                        map.put(material.toString(), collection);

                        // saving process
                        CollectionContainer.getInstance().setAsync(path, map, null);
                    }
                });
    }

    /**
     * Returns the instance of this class
     * @return instance of {@link CollectionManager}
     */
    public static CollectionManager getInstance() {
        return collectionManager;
    }

    public LoadingCache<Material, CollectionLeaderboard> getLeaderboardCache() {
        return leaderboardCache;
    }

    public void getAsyncLeaderboard(Material material, Consumer<CollectionLeaderboard> consumer) {
        executorService.execute(() -> {
            try {
                consumer.accept(leaderboardCache.get(material));
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        });
    }
}
