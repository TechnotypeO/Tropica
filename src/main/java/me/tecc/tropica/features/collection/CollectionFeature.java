package me.tecc.tropica.features.collection;

import me.tecc.tropica.TUtil;
import me.tecc.tropica.Tropica;
import me.tecc.tropica.menus.TropicaMenu;
import me.tecc.tropica.items.Item;
import me.tecc.tropica.items.NBTEditor;
import me.tecc.tropica.menus.Menu;
import me.tecc.tropica.storage.CollectionContainer;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.MemorySection;
import org.bukkit.craftbukkit.v1_15_R1.inventory.CraftItemStack;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.*;
import java.util.function.Consumer;


/**
 * This class has been made to handle the /collection command as
 * well as the inventory clicks and custom menus within this
 * particular collections system.
 *
 * Managed by classes:
 * {@link CollectionManager}
 *
 */
public class CollectionFeature implements CommandExecutor, Listener {
    // linked list of only obtainable items
    private final LinkedList<Material> listOfMaterials;

    // map for individual player pages
    private final Map<UUID, Integer> pages;

    /**
     * Constructor of this class which initializes
     * important fields such as Material Array and pages Map.
     *
     * Contains action which registers this listener.
     * @see Listener
     */
    public CollectionFeature() {
        // array of all Minecraft materials
        Material[] allMaterials = Material.values();

        this.listOfMaterials = new LinkedList<>();
        this.pages = new HashMap<>();

        // filtering the items (getting rid of unobtainable items or
        // just other weird items)
        for (Material m : allMaterials) {
            if (m.isItem()) {
                if (m.equals(Material.AIR) || m.equals(Material.BARRIER) || m.equals(Material.KNOWLEDGE_BOOK)
                        || m.equals(Material.BEDROCK) || m.equals(Material.DEBUG_STICK) || m.equals(Material.JIGSAW)
                || m.equals(Material.POTION) || m.equals(Material.LINGERING_POTION) || m.equals(Material.SPAWNER)
                        || m.equals(Material.SPLASH_POTION) || m.equals(Material.TIPPED_ARROW) || m.equals(Material.END_PORTAL_FRAME)
                || m.equals(Material.FARMLAND)) {
                    continue;
                }
                if (m == null) { // this is in fact needed
                    continue;
                }

                String s = m.toString();
                if (s.contains("_SPAWN_EGG") || s.contains("COMMAND_BLOCK")) {
                    continue;
                }
                if (s.contains("INFESTED_") || s.contains("STRUCTURE_")) {
                    continue;
                }
                listOfMaterials.add(m);
            }
        }

        // register the event
        Bukkit.getPluginManager().registerEvents(this, Tropica.getTropica());
    }

    /**
     * Command handler for the /collection command
     * @see CommandExecutor
     */
    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;

            if (args.length >= 1) {
                // getting material from the argument
                // example: /collection BEDROCK
                Material material = Material.getMaterial(args[0]);
                if (material != null) {
                    this.openMenuCollection(player, true, material);
                }
                return true;
            }
            this.openMenu(player, true);
        }
        return true;
    }

    /**
     * A method which opens a special collection menu.
     * Showing off the amount of collected item and leaderboards.
     * @see Menu
     *
     * @param player the target player
     * @param sound play sound on opening true/false
     * @param material the material
     */
    private void openMenuCollection(Player player, boolean sound, Material material) {
        Menu menu = new Menu(6*9, "Collection Menu");

        // getting basic data
        final UUID uuid = player.getUniqueId();
        final String path = uuid.toString();

        CollectionContainer.getInstance().getAsync(path, null, new Consumer<Object>() {
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

                // getting the amount
                double collection = map.getOrDefault(material.toString(), 0D);

                if (collection > 0) {
                    // getting the original name of item
                    net.minecraft.server.v1_15_R1.ItemStack itemStack1 = CraftItemStack.asNMSCopy(new ItemStack(material).clone());
                    String originalName = itemStack1.getItem().g(itemStack1).getLegacyString();

                    // creating fancy item
                    Item item = new Item(material, 1, "&e"+originalName,
                            "&9Information!",
                            "&7You're currently viewing",
                            "&7a singular collection.",
                            "",
                            "&7Your collection:",
                            "&8■ &a"+TUtil.toFancyCost(Math.ceil(collection)) + " &7&oof "+ originalName
                    );

                    // finally setting the item
                    menu.setSlot(21, item);

                    // loading leaderboard item
                    menu.setSlot(23, new Item(Material.OAK_SIGN, 1, "&bLeaderboard", "&7&oLoading..."));

                    CollectionManager.getInstance().getAsyncLeaderboard(material, new Consumer<CollectionLeaderboard>(){

                        @Override
                        public void accept(CollectionLeaderboard collectionLeaderboard) {
                            // leaderboard item
                            Map<UUID, Integer> places = collectionLeaderboard.getPlaces();

                            List<String> lore = new ArrayList<>(collectionLeaderboard.getItemLore());
                            lore.add("");
                            lore.add("&7Your position:");
                            lore.add("&8■ &3#"+TUtil.toFancyCost(places.getOrDefault(player.getUniqueId(), places.size())) + " &7&oout of "
                                    +TUtil.toFancyCost(places.size()));

                            menu.setSlot(23, new Item(Material.OAK_SIGN, 1, "&bLeaderboard")
                                    .setLore(lore)
                                    .addEnchantment(Enchantment.ARROW_INFINITE, 1)
                                    .setHideflags(true)
                            );
                        }
                    });


                } else {
                    // unknown collection
                    menu.setSlot(21, new Item(Material.BARRIER, 1, "&c???",
                            "&9Information!",
                            "&7This item wasn't unlocked yet.",
                            "&7",
                            "&cLocked!"));
                }
            }
        });

        Item goBack = new Item(Material.RED_STAINED_GLASS_PANE, 1, "&cGo Back");
        goBack.setItemStack(NBTEditor.addString(goBack.getItemStack(), "collection_viewer", "true"));
        menu.setSlot(49, goBack);

        menu.open(player, sound);
    }

    /**
     * A method which open the collection menu with
     * all obtainable items. Supports multiple pages
     * system to browse everything easily.
     * @see Menu
     *
     * @param player the target player
     * @param sound play sound on opening true/false
     */
    private void openMenu(Player player, boolean sound) {
        Menu menu = new Menu(6*9, "Collection Menu");
        // getting page of player
        int page = pages.getOrDefault(player.getUniqueId(), 0);

        // loop 45 times for the menu items
        for (int i = 0; i < 45; i++) {
            int slot = i + (page * 45); // slot based on the page

            if (listOfMaterials.size() > slot) { // simple check to not cause an exception
                Material material = listOfMaterials.get(slot);
                if (material == null) { // and again another check
                    continue;
                }

                // getting basic data
                final UUID uuid = player.getUniqueId();
                final String path = uuid.toString();

                int finalI = i;
                CollectionContainer.getInstance().getAsync(path, null, new Consumer<Object>() {
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

                        // getting the amount
                        double collection = map.getOrDefault(material.toString(), 0D);

                        if (collection > 0) {
                            // getting the original name of item
                            net.minecraft.server.v1_15_R1.ItemStack itemStack1 = CraftItemStack.asNMSCopy(new ItemStack(material).clone());
                            String originalName = itemStack1.getItem().g(itemStack1).getLegacyString();

                            // preparing item
                            Item item = new Item(material, 1, "&e"+originalName,
                                    "&9Information!",
                                    "&7This item was unlocked.",
                                    "",
                                    "&7Your collection:",
                                    "&8■ &a"+TUtil.toFancyCost(Math.ceil(collection)) + " &7&oof "+ originalName,
                                    "",
                                    "&eClick to view!"
                            );

                            // finally setting the item
                            menu.setSlot(finalI, item);

                        } else {
                            // unknown collection
                            menu.setSlot(finalI, new Item(Material.BARRIER, 1, "&c???",
                                    "&9Information!",
                                    "&7This item wasn't unlocked yet.",
                                    "&7",
                                    "&cLocked!"));
                        }
                    }
                });
            }
        }

        // other items
        if (page > 0) {
            menu.setSlot(47, new Item(Material.BLUE_STAINED_GLASS_PANE, 1, "&bPrevious Page"));
        }
        menu.setSlot(51, new Item(Material.BLUE_STAINED_GLASS_PANE, 1, "&bNext Page"));

        Item goBack = new Item(Material.RED_STAINED_GLASS_PANE, 1, "&cGo Back");
        goBack.setItemStack(NBTEditor.addString(goBack.getItemStack(), "all_collections", "true"));
        menu.setSlot(49, goBack);

        menu.open(player, sound);
    }

    /**
     * Click inventory handler
     * @see EventHandler
     * @see InventoryClickEvent
     */
    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getView().getTitle().equalsIgnoreCase("Collection Menu")) {
            event.setCancelled(true);

            Player player = (Player) event.getWhoClicked();
            Item item = new Item(event.getCurrentItem());

            if (item.getItemStack() == null || item.getType() == Material.AIR) {
                return;
            }

            // item checks
            if (item.getName().equalsIgnoreCase(TUtil.toColor("&bNext Page"))) {
                player.playSound(player.getLocation(), Sound.ENTITY_ITEM_FRAME_REMOVE_ITEM, 1.0f, 1.5f);

                pages.put(player.getUniqueId(), pages.getOrDefault(player.getUniqueId(), 0) + 1);
                openMenu(player, false);
                return;
            }

            if (item.getName().equalsIgnoreCase(TUtil.toColor("&bPrevious Page"))) {
                player.playSound(player.getLocation(), Sound.ENTITY_ITEM_FRAME_REMOVE_ITEM, 1.0f, 1.5f);

                pages.put(player.getUniqueId(), pages.getOrDefault(player.getUniqueId(), 0) - 1);
                openMenu(player, false);
                return;
            }

            if (item.getLore().contains(TUtil.toColor("&eClick to view!"))) {
                player.playSound(player.getLocation(), Sound.ENTITY_ITEM_FRAME_REMOVE_ITEM, 1.0f, 1.5f);
                openMenuCollection(player, false, item.getMaterial());
                return;
            }

            if (item.getName().equals(TUtil.toColor("&cGo Back"))) {
                player.playSound(player.getLocation(), Sound.ENTITY_ITEM_FRAME_REMOVE_ITEM, 1.0f, 1.5f);
                if (NBTEditor.hasString(item.getItemStack(), "collection_viewer")) {
                    openMenu(player, false);
                }
                if (NBTEditor.hasString(item.getItemStack(), "all_collections")) {
                    TropicaMenu.getInstance().openTropicaMenu(player, false);
                }
            }
        }
    }

}
