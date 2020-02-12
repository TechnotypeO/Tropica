package me.tecc.tropica.features.collection;

public class CollectionManager {
    private static CollectionManager collectionManager;

    public CollectionManager() {
        collectionManager = this;
    }

    public static CollectionManager getInstance() {
        return collectionManager;
    }
}
