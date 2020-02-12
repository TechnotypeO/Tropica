package me.tecc.tropica.events;

import me.tecc.tropica.Tropica;
import me.tecc.tropica.features.collection.CollectionManager;
import me.tecc.tropica.items.NBTEditor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

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
        CollectionManager.getInstance().handleEvent(event);

        event.getItem().setItemStack(NBTEditor.addInteger(itemStack, "nonce", 1));
        event.setCancelled(true);
    }

    @EventHandler
    public void onInventoryOpen(InventoryOpenEvent event) {
        if (event.getInventory().getType().isCreatable()) {

            @NotNull ItemStack[] contents = event.getInventory().getContents();
            boolean modified = false;

            for (int i = 0, contentsLength = contents.length; i < contentsLength; i++) {
                ItemStack content = contents[i];
                if (content != null && content.getType() != Material.AIR) {
                    if (!NBTEditor.hasInteger(content, "nonce")) {
                        content = NBTEditor.addInteger(content, "nonce", 1);
                        contents[i] = content;

                        modified = true;
                    }
                }
            }

            if (modified) {
                event.getInventory().setContents(contents);
            }
        }
    }



}
