package me.tecc.tropica.features.jumppads;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public class JumpPad {
    private final int x;
    private final int y;
    private final int z;
    private final World world;
    private final Location location;

    public JumpPad(int x, int y, int z, World world) {

        this.x = x;
        this.y = y;
        this.z = z;
        this.world = world;

        this.location = new Location(world, x, y, z);
    }

    public Location getLocation() {
        return location;
    }

    public int getZ() {
        return z;
    }

    public int getY() {
        return y;
    }

    public int getX() {
        return x;
    }

    public World getWorld() {
        return world;
    }

    @Override
    public String toString() {
        return x+":"+y+":"+z+":"+world.getName();
    }

    public static JumpPad parse(String string) {
        String[] spacing = string.split(":");
        int x = Integer.parseInt(spacing[0]);
        int y = Integer.parseInt(spacing[1]);
        int z = Integer.parseInt(spacing[2]);
        World world = Bukkit.getWorld(spacing[3]);

        return new JumpPad(x, y, z, world);
    }

    public void launch(Player player) {
        player.playSound(player.getLocation(), Sound.ENTITY_FIREWORK_ROCKET_LAUNCH, 1.0f, 2.0f);
        player.setVelocity(player.getLocation().getDirection().multiply(0.5).add(new Vector(0, 1.5, 0)));
    }
}
