package me.tecc.tropica.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class BroadcastCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (player.hasPermission("Broadcast.use")) {
                StringBuilder message = new StringBuilder();
                for (String part : args) {
                    if (!message.toString().equals("")) message.append(" ");
                    message.append(part);
                }
                Bukkit.getServer().broadcastMessage(toColor("&2&lTropica &7» &e" + message));
            }

        }
        return true;


    }

    private String toColor (String string){
        return ChatColor.translateAlternateColorCodes('&', string);

    }
}