package me.tecc.tropica.menus;

import me.tecc.tropica.TUtil;
import me.tecc.tropica.Tropica;
import me.tecc.tropica.features.bazaar.BazaarHandler;
import me.tecc.tropica.features.collection.CollectionManager;
import me.tecc.tropica.features.recipes.RecipeHandler;
import me.tecc.tropica.items.Item;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Iterator;

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
                "&eClick to browse!"
        ));

        menu.setSlot(15, new Item(Material.EMERALD, 1, "&aBazaar the Tropical Market",
                "&9Information!",
                "&7The main source of &6Cash&7.",
                "&7Place created for economy growth.",
                "&7Sell, buy, and create auctions.",
                "",
                "&eClick to browse!"
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

    @EventHandler
    public void onDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();

        Iterator<ItemStack> drops = event.getDrops().iterator();
        while (drops.hasNext()) {
            ItemStack next = drops.next();
            Item item = new Item(next);
            if (item.getName().equals(TUtil.toColor("&aTropica Menu &7(Right Click)"))) {
                drops.remove();
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
                CollectionManager.getInstance().getCollectionCommand().openMenu(player, false);
                return;
            }

            if (item.getName().equalsIgnoreCase(TUtil.toColor("&dRecipes"))) {
                player.playSound(player.getLocation(), Sound.ENTITY_ITEM_FRAME_REMOVE_ITEM, 1.0f, 1.5f);
                RecipeHandler.getInstance().getRecipeMenu().openMenu(player, false);
                return;
            }

            if (item.getName().equalsIgnoreCase(TUtil.toColor("&aBazaar the Tropical Market"))) {
                player.playSound(player.getLocation(), Sound.ENTITY_ITEM_FRAME_REMOVE_ITEM, 1.0f, 1.5f);
                BazaarHandler.getInstance().openMenu(player, false);
            }

        }
    }

    public static TropicaMenu getInstance() {
        return tropicaMenu;
    }
}
