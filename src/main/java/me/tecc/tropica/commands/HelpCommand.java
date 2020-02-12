package me.tecc.tropica.commands;

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
            net.md_5.bungee.api.chat.TextComponent one = new TextComponent(toColor("&a1&f. /gm help &f- &7Sends this message."));
            one.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(toColor("&b&lClick to type in chat &7(/gm help)")).create()));
            player.spigot().sendMessage(one);

            net.md_5.bungee.api.chat.TextComponent two = new TextComponent(toColor("&a2&f. /gmc [player] &f- &7Changes gamemode to Creative. (&fHover to type)"));
            two.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(toColor("&b&lClick to type in chat &7(/gmc)")).create()));
            player.spigot().sendMessage(two);

            net.md_5.bungee.api.chat.TextComponent three = new TextComponent(toColor("&a3&f. /gms [player] &f- &7Changes gamemode to Survival. (&fHover to type)"));
            three.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(toColor("&b&lClick to type in chat &7(/gms)")).create()));
            player.spigot().sendMessage(three);

            net.md_5.bungee.api.chat.TextComponent four = new TextComponent(toColor("&a4&f. /gms [player] &f- &7Changes gamemode to Adventure. (&fHover to type)"));
            four.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(toColor("&b&lClick to type in chat &7(/gma)")).create()));
            player.spigot().sendMessage(four);

            net.md_5.bungee.api.chat.TextComponent five = new TextComponent(toColor("&a5&f. /gms [player] &f- &7Changes gamemode to Spectator. (&fHover to type)"));
            five.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(toColor("&b&lClick to type in chat &7(/gmspec)")).create()));
            player.spigot().sendMessage(five);
            //For next Page
            net.md_5.bungee.api.chat.TextComponent six = new TextComponent(toColor("&eNext Page ->"));
            six.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(toColor("&7Click &ehere &7To go to next page")).create()));
            six.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "help 2"));
            player.spigot().sendMessage(six);

            //Last updated by funtodead at 12/2/2020





            /* No perms for the player*/
            //else if he doesn't have

        }
        return true;
    }


    {
}

    private String toColor(String s) {
        return ChatColor.translateAlternateColorCodes('&', s);
    }

}
