package me.tecc.tropica.HelpCommands;

import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class HelpCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {

        if (sender instanceof Player) {//checks if the sender is a player
            //there is your player variable
            Player player = (Player) sender;
            player.sendMessage(toColor("&2&LTropica &7» &7Help Menu"));
            player.sendMessage("");


            /*Ignore the GM stuff, we will change it soon, ~ funtodead  //Also If you see this Please remove the permission requirement from this command. and also the else

            /*1 */
            net.md_5.bungee.api.chat.TextComponent one = new TextComponent(toColor("&e1&f. /claim &f- &7Claim a land. &f(Hover to type)"));
            one.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(toColor("&b&lClick to type in chat &7(/Claim)")).create()));
            one.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/claim"));
            player.spigot().sendMessage(one);
            player.sendMessage("");
            net.md_5.bungee.api.chat.TextComponent two = new TextComponent(toColor("&e2&f. /unclaim &f- &7unclaim the land you're next to &f(Hover to type)"));
            two.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(toColor("&b&lClick to type in chat &7(/gmc)")).create()));
            two.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/unclaim"));
            player.spigot().sendMessage(two);
            player.sendMessage("");
            net.md_5.bungee.api.chat.TextComponent three = new TextComponent(toColor("&e3&f. /abandonallclaims &f- &7Unclaim all Land you ever claimed. &f(Hover to type)"));
            three.setHoverEvent(new HoverEvent(HoverEvent.Action., new ComponentBuilder(toColor("&b&lClick to type in chat &7(/gms)")).create()));
            three.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/abandonallclaims"));
            player.spigot().sendMessage(three);
            player.sendMessage("");
            net.md_5.bungee.api.chat.TextComponent four = new TextComponent(toColor("&e4&f. /gms [player] &f- &7Changes gamemode to Adventure. (&fHover to type)"));
            four.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(toColor("&b&lClick to type in chat &7(/gma)")).create()));
            player.spigot().sendMessage(four);
            player.sendMessage("");
            net.md_5.bungee.api.chat.TextComponent five = new TextComponent(toColor("&e5&f. /gms [player] &f- &7Changes gamemode to Spectator. (&fHover to type)"));
            five.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(toColor("&b&lClick to type in chat &7(/gmspec)")).create()));
            player.spigot().sendMessage(five);
            //For next Page
            net.md_5.bungee.api.chat.TextComponent six = new TextComponent(toColor("&6Next Page ->"));
            six.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(toColor("&7Click &ehere &7To go to next page")).create()));
            six.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/help 2"));
            player.spigot().sendMessage(six);
        }
        return true;
    }

    private String toColor(String s) {
        return ChatColor.translateAlternateColorCodes('&', s);
    }

}
