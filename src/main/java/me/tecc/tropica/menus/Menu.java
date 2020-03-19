package me.tecc.tropica.menus;

import me.tecc.tropica.items.Item;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.Arrays;
import java.util.List;

public class Menu {
    private final int size;
    private final String name;
    private final Inventory inventory;

    public static List<String> names = Arrays.asList("Collection Menu", "Tropica Menu");

    public Menu(int size, String name) {
        this.size = size;
        this.name = name;
        this.inventory = Bukkit.createInventory(null, size, name);

        if (!names.contains(name)) {
            names.add(name);
        }
    }

    public String getName() {
        return name;
    }

    public int getSize() {
        return size;
    }

    public Inventory getInventory() {
        return inventory;
    }

    public void setSlot(int index, Item item) {
        this.inventory.setItem(index, item.getItemStack());
    }

    public void open(Player player, boolean sound) {
        player.openInventory(inventory);
        if (sound) {
            player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_HAT, 1.0f, 1.0f);
        }
    }
}
