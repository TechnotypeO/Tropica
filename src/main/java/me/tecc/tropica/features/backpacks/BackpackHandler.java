package me.tecc.tropica.features.backpacks;

import me.tecc.tropica.TUtil;
import me.tecc.tropica.Tropica;
import me.tecc.tropica.items.Item;
import me.tecc.tropica.items.NBTEditor;
import me.tecc.tropica.items.sCoder;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class BackpackHandler implements Listener {
    private Set<UUID> dropped = new HashSet<>();

    public BackpackHandler() {
        Bukkit.getPluginManager().registerEvents(this, Tropica.getTropica());
    }

    @EventHandler
    public void onItemDrop(PlayerDropItemEvent e) {
        Player player = e.getPlayer();

        if (e.getItemDrop() != null) {
            Item item = new Item(e.getItemDrop().getItemStack());
            if (item.getName().equalsIgnoreCase(TUtil.toColor("&aTropical Backpack &7(Right Click)"))) {
                if (player.getOpenInventory() != null) {
                    if (player.getOpenInventory().getTitle().equalsIgnoreCase("Tropical Backpack")) {
                        dropped.add(player.getUniqueId());
                        player.closeInventory();

                        new BukkitRunnable() {
                            @Override
                            public void run() {
                                dropped.remove(player.getUniqueId());
                            }
                        }.runTaskLater(Tropica.getTropica(), 10L);

                    }
                }
            }
        }
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent e) {
        Player player = e.getPlayer();

        if (e.getAction() == Action.RIGHT_CLICK_BLOCK || e.getAction() == Action.RIGHT_CLICK_AIR) {
            if (e.getItem() != null && e.getItem().getType() != Material.AIR) {
                Item item = new Item(e.getItem());

                // this is due to Minecraft drop and right click packet
                if (item.getName().equalsIgnoreCase(TUtil.toColor("&aTropical Backpack &7(Right Click)"))) {
                    e.setCancelled(true);
                }

                new BukkitRunnable() {

                    @Override
                    public void run() {
                        if (item.getItemStack() == null || item.getItemStack().getType() == Material.AIR) {
                            return;
                        }

                        if (item.getName().equalsIgnoreCase(TUtil.toColor("&aTropical Backpack &7(Right Click)"))) {
                            e.setCancelled(true);

                            if (dropped.contains(player.getUniqueId())) {
                                return;
                            }

                            Inventory inventory = Bukkit.createInventory(null, 3*9, "Tropical Backpack");

                            if (NBTEditor.hasString(item.getItemStack(), "items")) {
                                try {
                                    inventory.setContents(sCoder.itemStackArrayFromBase64(NBTEditor.getString(item.getItemStack(), "items")));
                                } catch (IOException ex) {
                                    ex.printStackTrace();
                                }
                            }
                            player.openInventory(inventory);
                            dropped.remove(player.getUniqueId());
                        }
                    }
                }.runTaskLater(Tropica.getTropica(), 3L);

            }
        }
    }

    @EventHandler
    public void onItemClick(InventoryClickEvent e) {
        Player player = (Player) e.getWhoClicked();
        if (e.getView().getTitle().equalsIgnoreCase("Tropical Backpack")) {
            int slot = player.getInventory().getHeldItemSlot();
            if (e.getClick() == ClickType.NUMBER_KEY) {
                e.setCancelled(true);
                return;
            }

            if (e.getCurrentItem() != null) {
                Item item = new Item(e.getCurrentItem());
                if (e.getClickedInventory().getType() != InventoryType.PLAYER) {
                    return;
                }

                if (item.getName().contains("Backpack") && e.getSlot() == slot) {
                    e.setCancelled(true);
                }
            }
        }

    }

    @EventHandler
    public void onClose(InventoryCloseEvent e) {
        Player player = (Player) e.getPlayer();
        if (e.getView().getTitle().equalsIgnoreCase("Tropical Backpack")) {
            int slot = player.getInventory().getHeldItemSlot();
            ItemStack itemStack = player.getInventory().getItem(slot);
            if (itemStack == null || itemStack.getType() == Material.AIR) {
                return;
            }

            itemStack = NBTEditor.addString(itemStack, "items", sCoder.itemStackArrayToBase64(e.getInventory().getContents()));
            player.getInventory().setItem(slot, itemStack);
        }
    }
}
