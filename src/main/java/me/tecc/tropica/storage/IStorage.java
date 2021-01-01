package me.tecc.tropica.storage;

public interface IStorage {
    /**
     * Saves the current storage.
     *
     * @return Whether or not the current storage could be saved.
     */
    boolean save();

    /**
     * Save the current storage asynchronously.
     *
     * @return Whether or not the current storage could be saved.
     */
    boolean saveAsync();
}
