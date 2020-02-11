package me.tecc.tropica;

import org.bukkit.plugin.java.JavaPlugin;

public final class Tropica extends JavaPlugin {

    @Override
    public void onEnable() {
        // Plugin startup logic
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    /**
     * Gets the Tropica plugin instance.
     * This is short for {@code Tropica.getPlugin(Tropica.class)}.
     * @return Tropica plugin instance
     */
    public static Tropica getTropica() {
        return Tropica.getPlugin(Tropica.class);
    }
}
