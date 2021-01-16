package me.tecc.tropica.storage;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * AbstractDatabase is a class used for databases.
 * The difference between AbstractDatabase and AbstractContainer is that AbstractDatabase
 * uses JSON for storing data, which as AbstractContainer does not.
 */
public abstract class AbstractDatabase implements IStorage {
    /**
     * Instantiates an AbstractDatabase. This is shorthand for
     * {@code new AbstractDatabase(file, false)}.
     *
     * @param file The file to load.
     * @throws IOException           If an IO exception occurred.
     *                               Cases of this happening are:
     *                               <ul>
     *                                   <li>if the file could not be created, see
     *                                   {@code createIfNotFound}</li>
     *                                   <li>if the file was not found, see
     *                                   {@code createIfNotFound}</li>
     *                               </ul>
     * @throws FileNotFoundException If the file was not found, and
     *                               {@code createIfNotFound} is false.
     * @see AbstractDatabase#AbstractDatabase(File, boolean)
     */
    public AbstractDatabase(File file) throws IOException {
        // call alternate constructor
        this(file, false);
    }

    /**
     * Instantiates an AbstractDatabase.
     *
     * @param file             The file to load.
     * @param createIfNotFound If it should create the file in the case that it does not
     *                         already exist.
     * @throws IOException           If an IO exception occurred.
     *                               Cases of this happening are:
     *                               <ul>
     *                                   <li>if the file could not be created, see
     *                                   {@code createIfNotFound}</li>
     *                                   <li>if the file was not found, see
     *                                   {@code createIfNotFound}</li>
     *                               </ul>
     * @throws FileNotFoundException If the file was not found, and
     *                               {@code createIfNotFound} is false.
     */
    public AbstractDatabase(File file, boolean createIfNotFound) throws IOException {
        // Checks if the file does not exist
        if (!file.exists())
            // If file does not exist, check if parameter createIfNotFound is true
            if (createIfNotFound)
                // Create the file.
                //noinspection ResultOfMethodCallIgnored
                file.createNewFile();
                // Else if parameter createIfNotFound is false, throw FileNotFoundException.
            else throw new FileNotFoundException("Could not find file '" + file + "'.");
    }


}