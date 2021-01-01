package me.tecc.tropica.storage;

import org.bukkit.plugin.Plugin;

public class PublicContainer extends AbstractContainer {

    private static PublicContainer publicContainer;

    public PublicContainer(Plugin plugin, String filename) {
        super(plugin, filename);

        publicContainer = this;
    }

    public static PublicContainer getInstance() {
        return publicContainer;
    }
}
