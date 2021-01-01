package me.tecc.tropica.events;

import me.tecc.tropica.Tropica;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.SignChangeEvent;

public class ColoredSigns implements Listener {

    public ColoredSigns() {
        Bukkit.getPluginManager().registerEvents(this, Tropica.getTropica());

    }

    @EventHandler
    public void onSignChange(SignChangeEvent e) {
        String[] lines = e.getLines();
        for (int i = 0; i < 4; i++) {
            String line = lines[i];
            line = ChatColor.translateAlternateColorCodes('&', line);
            e.setLine(i, line);
        }
    }
}
