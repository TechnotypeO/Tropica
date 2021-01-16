package me.tecc.tropica.commands;

import me.tecc.tropica.Tropica;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

public class EnderChestCommand implements CommandExecutor, Listener {

    public EnderChestCommand() {
        Bukkit.getPluginManager().registerEvents(this, Tropica.getTropica());
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            player.openInventory(player.getEnderChest());
        }
        return true;
    }
}

