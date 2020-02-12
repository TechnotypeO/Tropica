package me.tecc.tropica.storage;

import org.bukkit.plugin.Plugin;

public class CollectionContainer extends AbstractContainer {
    private static CollectionContainer collectionContainer;

    public CollectionContainer(Plugin plugin, String filename) {
        super(plugin, filename);

        collectionContainer = this;
    }

    public static CollectionContainer getInstance() {
        return collectionContainer;
    }
}
