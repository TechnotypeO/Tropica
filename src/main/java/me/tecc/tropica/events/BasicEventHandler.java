package me.tecc.tropica.events;

import me.tecc.tropica.TUtil;
import me.tecc.tropica.Tropica;
import me.tecc.tropica.features.collection.CollectionManager;
import me.tecc.tropica.features.playerData.PlayerFeature;
import me.tecc.tropica.features.playerData.PlayerWrapper;
import me.tecc.tropica.features.teams.TeamHandler;
import me.tecc.tropica.items.Item;
import me.tecc.tropica.items.NBTEditor;
import me.tecc.tropica.menus.Menu;
import me.tecc.tropica.sidebar.DynamicScoreboard;
import me.tecc.tropica.sidebar.Rank;
import me.tecc.tropica.sidebar.Sidebar;
import me.tecc.tropica.storage.BazaarContainer;
import me.tecc.tropica.storage.WordFilter;
import me.tecc.tropica.texts.Text;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.minecraft.server.v1_16_R3.*;
import org.apache.commons.lang.WordUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Furnace;
import org.bukkit.craftbukkit.v1_16_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftPlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.inventory.*;
import org.bukkit.event.player.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;
import java.util.stream.Collectors;

public class BasicEventHandler implements Listener {
    private final Random random = new Random();
    public BasicEventHandler() {
        Bukkit.getPluginManager().registerEvents(this, Tropica.getTropica());
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        event.setJoinMessage(""); // making join message invisible
        Player player = event.getPlayer();

        Item menu = new Item(Material.NETHER_STAR, 1, "&aTropica Menu &7(Right Click)",
                "&9Information!",
                "&7This is your personal Tropica",
                "&7menu. You can use it any time",
                "&7you want to.",
                "",
                "&eRight click to open!"
        );
        menu.setItemStack(NBTEditor.addInteger(menu.getItemStack(), "nonce", 1));
        player.getInventory().setItem(8, menu.getItemStack());

        new PlayerFeature(player, aBoolean -> {
            new BukkitRunnable() {

                @Override
                public void run() {
                    Sidebar.sidebar(player);
                    DynamicScoreboard.updateTeamScoreboard();

                    String uuid = player.getUniqueId().toString();

                    BazaarContainer.getInstance().getAsync(uuid, 0.0D, o -> {
                        double cash = (double) o;
                        if (cash > 0) {
                            new Text("&e&lBAZAAR! &7You earned &6$"+TUtil.toFancyDouble(cash)+" &7while being offline!").send(player);
                            PlayerWrapper playerWrapper = new PlayerWrapper(player);
                            playerWrapper.setDouble("cash", playerWrapper.getDouble("cash") + cash);
                            Sidebar.updateCash(playerWrapper, cash);

                            player.playSound(player.getLocation(), Sound.ENTITY_WANDERING_TRADER_YES, 1.0f, 2.0f);

                            BazaarContainer.getInstance().setAsync(uuid, 0.0, null);
                        }

                    });
                }
            }.runTask(Tropica.getTropica());

        });

    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        event.setQuitMessage(""); // making quit message invisible
        PlayerFeature.quit(event.getPlayer());
        TeamHandler.getInstance().unloadPlayer(event.getPlayer());
    }

    @EventHandler
    public void onSwapHands(PlayerSwapHandItemsEvent e) {
        if (e.getPlayer().getInventory().getHeldItemSlot() == 8) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onItemPickUp(EntityPickupItemEvent event) {
        ItemStack itemStack = event.getItem().getItemStack();
        if (NBTEditor.hasInteger(itemStack, "nonce")) {
            return;
        }
        if (event.getEntityType() == EntityType.PLAYER) CollectionManager.getInstance().handleEvent((Player) event.getEntity(), itemStack);

        event.getItem().setItemStack(NBTEditor.addInteger(itemStack, "nonce", 1));
        event.setCancelled(true);
    }

    @EventHandler
    public void onInventoryOpen(InventoryOpenEvent event) {
        if (event.getInventory().getType().isCreatable()) {

            if (Menu.names.contains(event.getView().getTitle())) {
                return;
            }

            ItemStack[] contents = event.getInventory().getContents();
            boolean modified = false;

            for (int i = 0, contentsLength = contents.length; i < contentsLength; i++) {
                ItemStack content = contents[i];
                if (content != null && content.getType() != Material.AIR) {
                    if (!NBTEditor.hasInteger(content, "nonce")) {
                        content = NBTEditor.addInteger(content, "nonce", 1);
                        contents[i] = content;

                        CollectionManager.getInstance().handleEvent((Player) event.getPlayer(), contents[i]);
                        modified = true;
                    }
                }
            }

            if (modified) {
                event.getInventory().setContents(contents);
            }
        }
    }

    @EventHandler
    public void onFurnace(FurnaceSmeltEvent e) {
        ItemStack itemStack = e.getResult();
        if (NBTEditor.hasInteger(itemStack, "nonce")) {
            return;
        }
        itemStack = NBTEditor.addInteger(itemStack, "nonce", 1);
        e.setResult(itemStack);

        Furnace furnace = (Furnace) e.getBlock().getState();
        if (!furnace.getSnapshotInventory().getViewers().isEmpty()) {
            Player p = (Player) furnace.getSnapshotInventory().getViewers().get(random.nextInt(furnace.getSnapshotInventory().getViewers().size()));
            CollectionManager.getInstance().handleEvent(p, itemStack);
        }

    }

    @EventHandler
    public void onInventoryMove(InventoryMoveItemEvent event) {
        ItemStack itemStack = event.getItem();
        if (NBTEditor.hasInteger(itemStack, "nonce")) {
            return;
        }

        new BukkitRunnable() {

            @Override
            public void run() {
                ItemStack[] contents = event.getSource().getContents();

                for (int i = 0, contentsLength = contents.length; i < contentsLength; i++) {
                    ItemStack content = contents[i];
                    if (content != null && content.getType() != Material.AIR) {
                        if (!NBTEditor.hasInteger(content, "nonce")) {
                            content = NBTEditor.addInteger(content, "nonce", 1);
                            contents[i] = content;
                        }
                    }
                }

                event.getSource().setContents(contents);
            }
        }.runTaskLater(Tropica.getTropica(), 1L);


        event.setCancelled(true);
    }

    @EventHandler
    public void onPrepareCraft(PrepareItemCraftEvent event) {
        try {
            ItemStack itemStack = event.getInventory().getResult();
            if (itemStack.getType() == Material.AIR) {
                return;
            }

            itemStack = NBTEditor.addInteger(event.getInventory().getResult(), "nonce", 1);
            if (itemStack.getType() == Material.PLAYER_HEAD) {
                itemStack = NBTEditor.addString(event.getInventory().getResult(), "uuid", UUID.randomUUID().toString());
            }

            event.getInventory().setResult(itemStack);
        } catch (Exception e) {

        }
    }

    @EventHandler
    public void onCraft(CraftItemEvent event) {
        Player player = (Player) event.getWhoClicked();
        ItemStack result = event.getRecipe().getResult();
        CollectionManager.getInstance().handleEvent(player, result);
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {
        if (e.getCurrentItem() != null) {
            Item clicked = new Item(e.getCurrentItem());
            if (clicked.getName().equals(TUtil.toColor("&aTropica Menu &7(Right Click)"))) {
                e.setCancelled(true);
                return;
            }

            if (e.getHotbarButton() == 8) {
                e.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onItemDrpo(PlayerDropItemEvent event) {
        Item item = new Item(event.getItemDrop().getItemStack());

        if (event.getItemDrop().getItemStack().getType() == Material.NETHER_STAR) {
            if (item.getName().equals(TUtil.toColor("&aTropica Menu &7(Right Click)"))) {
                event.setCancelled(true);
                return;
            }
        }

        if (NBTEditor.hasString(item.getItemStack(), "drop")) {
            event.getPlayer().playSound(event.getPlayer().getLocation(), Sound.ENTITY_VILLAGER_NO, 1.0f, 0.5f);
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onAchievement(PlayerAdvancementDoneEvent e) {
        Player player = e.getPlayer();
        String name = e.getAdvancement().getKey().getKey().split("/")[1].replaceAll("_","");
        name = WordUtils.capitalizeFully(name);

        // fancy message and sounds
        TextComponent textComponent = new TextComponent(TUtil.toColor(
                "\n&r &r &r &r &r &a&l◆ &2&lADVANCEMENT DONE &a&l◆\n"+
                        "&r &r &r&7You've completed &f&n"+name+"\n"+
                        "&r &r &r&7Your reward: &b+10 Knowledge Point"+
                        "\n"
        ));

        player.spigot().sendMessage(textComponent);

        PlayerWrapper playerWrapper = new PlayerWrapper(player);
        int knowledge = playerWrapper.getInt("knowledge") + 10;
        playerWrapper.setInt("knowledge", knowledge);

        Sidebar.updateKnowledge(playerWrapper, 10);
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent e) {
        e.setCancelled(true);
        Player player = e.getPlayer();
        String rank = Rank.getRank(player);
        String prefix = Rank.getPrefix(rank);

        String message = e.getMessage();
        String chat = "&7: ";

        if (Rank.hasRank(player, "vip") || Rank.hasRank(player, "tester")) {
            chat = "&f: ";
        }
        if (Rank.hasRank(player, "owner")) {
            chat = "&f: &e";
        }

        if (WordFilter.containsBadWords(message)) {
            message = "Ooh, just did a bad thing. Yeah!";
        }

        TextComponent textComponent = new TextComponent(TUtil.toColor(prefix + player.getName() + chat) + message);
        textComponent.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
                new ComponentBuilder(TUtil.toColor("&7Hey, you can click this to &ecopy&7 the message!")).create()));
        textComponent.setClickEvent(new ClickEvent(ClickEvent.Action.COPY_TO_CLIPBOARD, message));

        for (Player p : Bukkit.getOnlinePlayers()) {
            p.spigot().sendMessage(textComponent);
        }
    }

    @EventHandler
    public void onMobKill(EntityDeathEvent e) {
        Entity entity = e.getEntity();

        if (entity instanceof Monster) {
            Monster monster = (Monster) entity;
            if (monster.getLastDamageCause() instanceof EntityDamageByEntityEvent) {
                EntityDamageByEntityEvent lastDamageCause = (EntityDamageByEntityEvent) monster.getLastDamageCause();

                if (lastDamageCause.getDamager() instanceof Player) {
                    Player player = (Player) lastDamageCause.getDamager();

                    double cash = Math.ceil((Math.random() * 10) + 1);

                    PlayerWrapper playerWrapper = new PlayerWrapper(player);
                    playerWrapper.setDouble("cash", playerWrapper.getDouble("cash") + cash);

                    Collection<Player> collection =
                            Bukkit.getOnlinePlayers().stream().filter(p -> p.getLocation().distance(entity.getLocation()) <= 20).collect(Collectors.toList());
                    entity.getWorld().playSound(entity.getLocation(), Sound.BLOCK_NOTE_BLOCK_BELL, 1.0f, 2.0f);

                    spawnMoney(cash, "&6+$x", entity.getLocation(), collection);

                    Sidebar.updateCash(playerWrapper, cash);
                }
            }
        }
    }
    private final Map<UUID, List<Integer>> ids = new HashMap<>();

    private void spawnMoney(double price, String pattern, Location location, Collection<Player> entities) {
        Location center = location.getBlock().getLocation().add(0.5, -1.0, 0.5);

        if (random.nextDouble() <= 0.5) {
            center.add(random.nextDouble(), 0, random.nextDouble());
        } else if (random.nextDouble() <= 0.5) {
            center.add(-random.nextDouble(), 0, random.nextDouble());
        } else if (random.nextDouble() <= 0.5) {
            center.add(-random.nextDouble(), 0, -random.nextDouble());
        } else if (random.nextDouble() <= 0.5) {
            center.add(random.nextDouble(), 0, -random.nextDouble());
        }

        EntityArmorStand entityArmorStand = craftEntityArmorStand(center, pattern.replaceAll("x", TUtil.toFancyDouble(price)));

        final DataWatcher dataWatcher = entityArmorStand.getDataWatcher();
        final PacketPlayOutEntityMetadata metadata = new PacketPlayOutEntityMetadata(entityArmorStand.getId(), dataWatcher, false);

        PacketPlayOutSpawnEntityLiving spawn = new PacketPlayOutSpawnEntityLiving(entityArmorStand);


        for (Player p : entities) {
            if (p.getLocation().distance(center) <= 20) {
                ((CraftPlayer) p).getHandle().playerConnection.sendPacket(spawn);
                ((CraftPlayer) p).getHandle().playerConnection.sendPacket(metadata);

                List<Integer> integers = ids.getOrDefault(p.getUniqueId(), new ArrayList<>());
                integers.add(entityArmorStand.getId());
                ids.put(p.getUniqueId(), integers);
            }
        }


        new BukkitRunnable() {
            int timer = 0;

            @Override
            public void run() {
                timer += 1;
                if (timer >= 20) {
                    PacketPlayOutEntityDestroy packet = new PacketPlayOutEntityDestroy(entityArmorStand.getId());

                    for (Player p : entities) {
                        ((CraftPlayer) p).getHandle().playerConnection.sendPacket(packet);
                    }
                    this.cancel();
                } else {

                    PacketPlayOutEntity.PacketPlayOutRelEntityMove packet = new PacketPlayOutEntity.
                            PacketPlayOutRelEntityMove(entityArmorStand.getId(), (short) 0, (short)555, (short)0, true);
                    for (Player p : entities) {
                        ((CraftPlayer) p).getHandle().playerConnection.sendPacket(metadata);
                        ((CraftPlayer) p).getHandle().playerConnection.sendPacket(packet);
                    }
                }
            }
        }.runTaskTimer(Tropica.getTropica(), 0L, 1L);
    }

    private EntityArmorStand craftEntityArmorStand(Location loc, String s) {
        WorldServer worldServer = ((CraftWorld) loc.getWorld()).getHandle();
        EntityArmorStand entityArmorStand = new EntityArmorStand(EntityTypes.ARMOR_STAND, worldServer);

        entityArmorStand.setSilent(true);
        entityArmorStand.setMarker(false);
        entityArmorStand.setNoGravity(true);
        entityArmorStand.setSmall(true);
        entityArmorStand.setInvisible(true);
        entityArmorStand.setCustomNameVisible(true);
        entityArmorStand.setCustomName(IChatBaseComponent.ChatSerializer.a("{\"text\":\""+ TUtil.toColor(s) +"\"}"));

        entityArmorStand.setLocation(loc.getX(), loc.getY(), loc.getZ(), loc.getYaw(), loc.getPitch());
        return entityArmorStand;
    }
}
