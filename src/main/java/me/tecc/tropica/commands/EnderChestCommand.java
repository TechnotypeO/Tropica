package me.tecc.tropica.commands;

import me.tecc.tropica.TUtil;
import me.tecc.tropica.Tropica;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType;

public class EnderChestCommand implements CommandExecutor, Listener {

    public EnderChestCommand() {
        Bukkit.getPluginManager().registerEvents(this, Tropica.getTropica());
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            player.openInventory(player.getEnderChest());
            player.sendMessage(TUtil.toColor("&5&LENDERCHEST! &7Opened your &5Enderchest"));
        }

        return true;
    }

    @EventHandler
    public void EnderchestClose(InventoryCloseEvent e) {
        if (e.getInventory().getType() == InventoryType.ENDER_CHEST) {
            e.getPlayer().sendMessage(TUtil.toColor("&5&LENDERCHEST! &7Closed your &5Enderchest"));
        }

    }

}

