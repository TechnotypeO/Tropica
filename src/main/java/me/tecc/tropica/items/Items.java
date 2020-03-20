package me.tecc.tropica.items;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("unused")
public class Items {

    public static Item parseItem(ItemStack item) {
        return new Item(item);
    }

    public static void giveItem(Player player, ItemStack item) {
        HashMap<Integer, ItemStack> rest = player.getInventory().addItem(item);
        if (rest.size() >= 1) {
            for (Map.Entry<Integer, ItemStack> r : rest.entrySet()) {
                player.getWorld().dropItem(player.getLocation().add(0, 0.25, 0), r.getValue());
            }
        }
    }

}
