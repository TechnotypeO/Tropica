package me.tecc.tropica.storage;

import org.bukkit.plugin.Plugin;

public class PlayerContainer extends AbstractContainer {

    private static PlayerContainer playerContainer;

    public PlayerContainer(Plugin plugin, String filename) {
        super(plugin, filename);

        playerContainer = this;
    }

    public static PlayerContainer getInstance() {
        return playerContainer;
    }
}
