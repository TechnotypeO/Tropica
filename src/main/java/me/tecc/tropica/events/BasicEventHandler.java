package me.tecc.tropica.events;

import me.tecc.tropica.Tropica;
import me.tecc.tropica.features.playerData.PlayerFeature;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class BasicEventHandler implements Listener {
    public BasicEventHandler() {
        Bukkit.getPluginManager().registerEvents(this, Tropica.getTropica());
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        new PlayerFeature(player, aBoolean -> {
        });
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        PlayerFeature.quit(event.getPlayer());
    }
}
