package me.tecc.tropica.events;

import me.tecc.tropica.Tropica;
import me.tecc.tropica.features.collection.CollectionManager;
import me.tecc.tropica.items.NBTEditor;
import me.tecc.tropica.menus.Menu;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.inventory.InventoryMoveItemEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
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
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        event.setQuitMessage(""); // making quit message invisible
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

            boolean custom = false;
            if (Menu.names.contains(event.getView().getTitle())) {
                custom = true;
            }

            ItemStack[] contents = event.getInventory().getContents();
            boolean modified = false;

            for (int i = 0, contentsLength = contents.length; i < contentsLength; i++) {
                ItemStack content = contents[i];
                if (content != null && content.getType() != Material.AIR) {
                    if (!NBTEditor.hasInteger(content, "nonce")) {
                        content = NBTEditor.addInteger(content, "nonce", 1);
                        contents[i] = content;

                        if (!custom) {
                            CollectionManager.getInstance().handleEvent((Player) event.getPlayer(), contents[i]);
                        }
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
}
