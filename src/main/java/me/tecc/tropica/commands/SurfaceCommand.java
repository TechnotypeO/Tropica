package me.tecc.tropica.commands;

import me.tecc.tropica.TUtil;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SurfaceCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (commandSender instanceof Player) {
            Player player = (Player) commandSender;
            Block block = player.getWorld().getHighestBlockAt(player.getLocation());
            player.teleport(block.getLocation());
            player.sendMessage(TUtil.toColor("&2&lSURFACE! &7Teleported you to the highest location."));
            player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1.0f, 1.0f);
        }
        return true;
    }
}
