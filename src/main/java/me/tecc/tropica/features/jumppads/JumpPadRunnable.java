package me.tecc.tropica.features.jumppads;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.scheduler.BukkitRunnable;

public class JumpPadRunnable extends BukkitRunnable {
    private Location location;

    public JumpPadRunnable(Location location) {
        this.location = location;
    }

    @Override
    public void run() {
        Location l =  location.clone().add(0.5, 0.775, 0.5);

        if (l.getChunk().isLoaded()) {
            location.getWorld().spawnParticle(Particle.CLOUD, l, 10, 0.25, 0 ,0.25, 0);
        }

    }

}
