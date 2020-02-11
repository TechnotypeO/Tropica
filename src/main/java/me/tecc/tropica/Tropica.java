package me.tecc.tropica;

import com.google.gson.Gson;
import me.tecc.tropica.commands.HelpCommand;
import org.bukkit.plugin.java.JavaPlugin;

public final class Tropica extends JavaPlugin {

    @Override
    public void onEnable() {
        // Plugin startup logic

       getCommand("help").setExecutor(new HelpCommand());

    }
}
