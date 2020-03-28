package me.tecc.tropica.commands;

import me.tecc.tropica.TUtil;
import me.tecc.tropica.items.Item;
import me.tecc.tropica.items.NBTEditor;
import me.tecc.tropica.texts.Text;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class UndroppableCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            ItemStack itemStack = player.getInventory().getItemInMainHand();

            if (itemStack != null && itemStack.getType() != Material.AIR) {

                if (NBTEditor.hasString(itemStack, "drop")) {
                    new Text("&c&lREMOVED! &7The &fUndroppable Tag &7has been &eremoved&7 from your item!").send(player);
                    player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_ATTACK_SWEEP, 1.0f, 0.5f);

                    itemStack = NBTEditor.removeString(itemStack, "drop");
                    itemStack = updateLore(new Item(itemStack), false);
                    player.getInventory().setItemInMainHand(itemStack);

                } else {
                    new Text("&a&lADDED! &7The &fUndroppable Tag &7has been &eadded&7 to your item!").send(player);
                    player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_ATTACK_SWEEP, 1.0f, 2.0f);

                    itemStack = NBTEditor.addString(itemStack, "drop", "drop");
                    itemStack = updateLore(new Item(itemStack), true);
                    player.getInventory().setItemInMainHand(itemStack);
                }

            } else {
                new Text("&c&lOOPS! &7You have to hold an actual item!").send(player);
            }
        }
        return true;
    }

    private ItemStack updateLore(Item item, boolean b) {
        List<String> lore = item.getLore();

        if (b) {
            lore.add(TUtil.toColor("&9⦿ &bUndroppable Tag"));
        } else {
            lore.remove(TUtil.toColor("&9⦿ &bUndroppable Tag"));
        }
        item.setLore(lore);
        return item.getItemStack();
    }
}
