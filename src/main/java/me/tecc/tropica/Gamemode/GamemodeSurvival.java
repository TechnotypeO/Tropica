package me.tecc.tropica.Gamemode;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class GamemodeSurvival implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
                if (player.hasPermission("gm.*")) {
                    GameMode gameMode = GameMode.SURVIVAL;
                    String message = "";
                    Player target;

                    if (args.length >= 1) {
                        target = Bukkit.getPlayer(args[0]);
                        message = "&7Your gamemode was changed to &2Survival &7by &e"+player.getDisplayName()+"&7!";
                    } else {
                        target = player;
                        message = "&7Your gamemode is now &2Survival";
                    }

                    if (target == null) {
                        player.sendMessage(toColor("&cPlayer not found!"));
                        return true;
                    }

                    target.setGameMode(gameMode);
                    target.sendMessage(toColor(message));
                }
        }
        return true;
    }

    private String toColor(String string) {
        return ChatColor.translateAlternateColorCodes('&', string);
    }
}

