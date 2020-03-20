package me.tecc.tropica.storage;

import org.bukkit.plugin.Plugin;

public class BazaarContainer extends AbstractContainer {
    private static BazaarContainer bazaarContainer;

    public BazaarContainer(Plugin plugin, String filename) {
        super(plugin, filename);

        bazaarContainer = this;
    }

    public static BazaarContainer getInstance() {
        return bazaarContainer;
    }
}
