package me.tecc.tropica;

import me.tecc.tropica.events.BasicEventHandler;
import me.tecc.tropica.features.collection.CollectionManager;
import me.tecc.tropica.storage.CollectionContainer;
import me.tecc.tropica.storage.PublicContainer;
import org.bukkit.plugin.java.JavaPlugin;

public final class Tropica extends JavaPlugin {

    @Override
    public void onEnable() {
        if (!this.getDataFolder().exists()) {
            this.getDataFolder().mkdirs();
        }

        // initialize custom files
        new PublicContainer(this, "publicContainer.yml");
        new CollectionContainer(this, "collectionContainer.yml");

        // initialize other managers
        new CollectionManager();

        // initialize listeners
        new BasicEventHandler();
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
