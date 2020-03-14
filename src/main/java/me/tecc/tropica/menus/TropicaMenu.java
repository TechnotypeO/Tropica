package me.tecc.tropica.menus;

import me.tecc.tropica.TUtil;
import me.tecc.tropica.Tropica;
import me.tecc.tropica.items.Item;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;

public class TropicaMenu implements Listener {
    private static TropicaMenu tropicaMenu;

    public TropicaMenu() {
        tropicaMenu = this;

        Bukkit.getPluginManager().registerEvents(this, Tropica.getTropica());
    }

    public void openTropicaMenu(Player player, boolean sound) {
        Menu menu = new Menu(4*9, "Tropica Menu");

        menu.setSlot(11, new Item(Material.PAINTING, 1, "&6Collection",
                "&9Information!",
                "&7View all of your collections.",
                "&7Contains leaderboards and the",
                "&7counter of collected items.",
                "",
                "&eClick to browse!"
        ));

        menu.setSlot(13, new Item(Material.ENCHANTED_BOOK, 1, "&dRecipes",
                "&9Information!",
                "&7View all special recipes.",
                "",
                "&cComing Soon!"
        ));

        menu.setSlot(31, new Item(Material.RED_STAINED_GLASS_PANE, 1, "&cClose"));

        menu.open(player, sound);
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        if (event.getItem() != null) {
            if (event.getAction() == Action.RIGHT_CLICK_BLOCK || event.getAction() == Action.RIGHT_CLICK_AIR) {
                Item item = new Item(event.getItem());
                if (item.getName().equals(TUtil.toColor("&aTropica Menu &7(Right Click)"))) {
                    event.setCancelled(true);
                    this.openTropicaMenu(event.getPlayer(), true);
                }
            }
        }
    }

    /**
     * Click inventory handler
     * @see EventHandler
     * @see InventoryClickEvent
     */
    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getView().getTitle().equalsIgnoreCase("Tropica Menu")) {
            event.setCancelled(true);

            Player player = (Player) event.getWhoClicked();
            Item item = new Item(event.getCurrentItem());

            if (item.getItemStack() == null || item.getType() == Material.AIR) {
                return;
            }

            // item checks
            if (item.getName().equalsIgnoreCase(TUtil.toColor("&cClose"))) {
                player.playSound(player.getLocation(), Sound.ENTITY_ITEM_FRAME_REMOVE_ITEM, 1.0f, 1.5f);
                player.closeInventory();
                return;
            }

            if (item.getName().equalsIgnoreCase(TUtil.toColor("&6Collection"))) {
                player.playSound(player.getLocation(), Sound.ENTITY_ITEM_FRAME_REMOVE_ITEM, 1.0f, 1.5f);
                player.performCommand("collection");
            }

        }
    }

    public static TropicaMenu getInstance() {
        return tropicaMenu;
    }
}
