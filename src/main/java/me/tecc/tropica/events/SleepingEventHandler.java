package me.tecc.tropica.events;

import me.tecc.tropica.TUtil;
import me.tecc.tropica.Tropica;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerBedEnterEvent;

public class SleepingEventHandler implements Listener {

    public SleepingEventHandler() {
        Bukkit.getPluginManager().registerEvents(this, Tropica.getTropica());
    }

    @EventHandler
    public void onSleep(PlayerBedEnterEvent e) {
        Player player = e.getPlayer();

        if (e.getBedEnterResult().equals(PlayerBedEnterEvent.BedEnterResult.OK)) {

            int size = Bukkit.getOnlinePlayers().size(); // 10
            int sleeping = 1; // 2

            for (Player p : Bukkit.getOnlinePlayers()) {
                if (p.isSleeping()) {
                    sleeping += 1;
                }
            }

            // 10 - 100
            // 2 - ?

            int number = 100 * sleeping / size;
            if (number >= 75) {
                player.getWorld().setTime(1000);
                Bukkit.broadcastMessage(TUtil.toColor("&aThe night has passed!"));

                e.setCancelled(true);
            }
        }
    }
}
