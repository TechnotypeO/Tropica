package me.tecc.tropica.events;

import me.tecc.tropica.TUtil;
import me.tecc.tropica.Tropica;
import me.tecc.tropica.features.collection.CollectionManager;
import me.tecc.tropica.features.playerData.PlayerFeature;
import me.tecc.tropica.items.Item;
import me.tecc.tropica.items.NBTEditor;
import me.tecc.tropica.menus.Menu;
import me.tecc.tropica.sidebar.DynamicScoreboard;
import me.tecc.tropica.sidebar.Sidebar;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.inventory.*;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

public class BasicEventHandler implements Listener {
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

        new PlayerFeature(player);

        Sidebar.sidebar(player);
        DynamicScoreboard.updateTeamScoreboard();
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        event.setQuitMessage(""); // making quit message invisible
        PlayerFeature.quit(event.getPlayer());
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
            event.getInventory().setResult(NBTEditor.addInteger(event.getInventory().getResult(), "nonce", 1));
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
        if (event.getItemDrop().getItemStack().getType() == Material.NETHER_STAR) {
            Item item = new Item(event.getItemDrop().getItemStack());
            if (item.getName().equals(TUtil.toColor("&aTropica Menu &7(Right Click)"))) {
                event.setCancelled(true);
            }
        }
    }
}
