package me.tecc.tropica;

import com.google.gson.Gson;
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
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
