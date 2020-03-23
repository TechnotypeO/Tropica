package me.tecc.tropica.commands;

import me.tecc.tropica.TUtil;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class EnderChestCommand implements CommandExecutor {
    private Object Player;

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            player.openInventory(player.getEnderChest());
            player.sendMessage(TUtil.toColor("&5&LENDERCHEST! &7Opened your &5Enderchest"));
          //~funtodead - I couldn't find out how to add the right way to if player.closeinventory, to send him a message
            //You closed your enderchest. if someone sees this. and can help. possibly pm me so i can also learn.


        }


        return true;
    }
}

