package me.tecc.tropica;

import me.tecc.tropica.commands.CraftingCommand;
import me.tecc.tropica.commands.EnderChestCommand;
import me.tecc.tropica.commands.SurfaceCommand;
import me.tecc.tropica.events.BasicEventHandler;
import me.tecc.tropica.events.ColoredSigns;
import me.tecc.tropica.features.homes.HomeHandler;
import me.tecc.tropica.features.playerData.PlayerTaskManager;
import me.tecc.tropica.storage.PlayerContainer;
import me.tecc.tropica.storage.PublicContainer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

public final class Tropica extends JavaPlugin {

    @Override
    public void onEnable() {
        if (!this.getDataFolder().exists()) {
            this.getDataFolder().mkdirs();
        }

        // initialize custom files
        new PublicContainer(this, "publicContainer.yml"); //file for server stuff
        new PlayerContainer(this, "playerContainer.yml"); //file for player data
        // initialize listeners
        new BasicEventHandler();
        new ColoredSigns();
        // init home handler
        new HomeHandler();
        //Commands <-
        getCommand("enderchest").setExecutor(new EnderChestCommand());
        getCommand("craft").setExecutor(new CraftingCommand());
        getCommand("surface").setExecutor(new SurfaceCommand());

        for (Player player : Bukkit.getOnlinePlayers()) {
            PlayerJoinEvent playerJoinEvent = new PlayerJoinEvent(player, "");
            Bukkit.getPluginManager().callEvent(playerJoinEvent);
        }

    }

    @Override
    public void onDisable() {
        PlayerTaskManager.getInstance().run();
    }

    /**
     * Gets the Tropica plugin instance.
     * This is short for {@code Tropica.getPlugin(Tropica.class)}.
     *
     * @return Tropica plugin instance
     */
    public static Tropica getTropica() {
        return Tropica.getPlugin(Tropica.class);
    }
}
