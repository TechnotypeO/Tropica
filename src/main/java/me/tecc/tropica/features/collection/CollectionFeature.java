package me.tecc.tropica.features.collection;

import me.tecc.tropica.TUtil;
import me.tecc.tropica.Tropica;
import me.tecc.tropica.items.Item;
import me.tecc.tropica.menus.Menu;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.UUID;


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

        // creating fancy item
        Item item = new Item(material);

        // collection preview item
        menu.setSlot(21, item);

        // leaderboard item
        menu.setSlot(23, new Item(Material.OAK_SIGN));

        menu.setSlot(49, new Item(Material.RED_STAINED_GLASS_PANE, 1, "&cGo Back"));
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
                // finally setting the item
                menu.setSlot(i, new Item(material, 1, material.toString()));
            }
        }

        // other items
        if (page > 0) {
            menu.setSlot(47, new Item(Material.BLUE_STAINED_GLASS_PANE, 1, "&bPrevious Page"));
        }
        menu.setSlot(51, new Item(Material.BLUE_STAINED_GLASS_PANE, 1, "&bNext Page"));
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
            }
        }
    }

}
