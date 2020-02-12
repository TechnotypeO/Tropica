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

public class CollectionCommand implements CommandExecutor, Listener {
    private final Material[] allMaterials;
    private final LinkedList<Material> listOfMaterials;
    private final Map<UUID, Integer> pages;

    public CollectionCommand() {
        allMaterials = Material.values();
        listOfMaterials = new LinkedList<>();
        for (Material m : allMaterials) {
            if (m.isItem()) {
                if (m.equals(Material.AIR) || m.equals(Material.BARRIER) || m.equals(Material.KNOWLEDGE_BOOK)
                        || m.equals(Material.BEDROCK) || m.equals(Material.DEBUG_STICK) || m.equals(Material.JIGSAW)
                || m.equals(Material.POTION) || m.equals(Material.LINGERING_POTION) || m.equals(Material.SPAWNER)
                        || m.equals(Material.SPLASH_POTION) || m.equals(Material.TIPPED_ARROW) || m.equals(Material.END_PORTAL_FRAME)
                || m.equals(Material.FARMLAND)) {
                    continue;
                }
                if (m == null) {
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

        pages = new HashMap<>();

        Bukkit.getPluginManager().registerEvents(this, Tropica.getTropica());
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (args.length >= 1) {
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

    private void openMenuCollection(Player player, boolean b, Material material) {
        Menu menu = new Menu(6*9, "Collection Menu");
        menu.setSlot(21, new Item(material));
        menu.setSlot(23, new Item(Material.OAK_SIGN));

        menu.setSlot(49, new Item(Material.RED_STAINED_GLASS_PANE, 1, "&cGo Back"));
        menu.open(player, b);
    }

    private void openMenu(Player player, boolean b) {
        Menu menu = new Menu(6*9, "Collection Menu");

        int page = pages.getOrDefault(player.getUniqueId(), 0);

        for (int i = 0; i < 45; i++) {
            int slot = i + (page * 45);

            if (listOfMaterials.size() > slot) {
                Material material = listOfMaterials.get(slot);
                if (material == null) {
                    continue;
                }
                menu.setSlot(i, new Item(material, 1, material.toString()));
            }

        }

        if (page > 0) {
            menu.setSlot(47, new Item(Material.BLUE_STAINED_GLASS_PANE, 1, "&bPrevious Page"));
        }
        menu.setSlot(51, new Item(Material.BLUE_STAINED_GLASS_PANE, 1, "&bNext Page"));
        menu.open(player, b);
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getView().getTitle().equalsIgnoreCase("Collection Menu")) {
            event.setCancelled(true);

            Player player = (Player) event.getWhoClicked();
            Item item = new Item(event.getCurrentItem());

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
        }
    }

}
