package me.tecc.tropica.storage;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

public class AbstractContainer {
    private final static ExecutorService executor = Executors.newCachedThreadPool();
    private static AbstractContainer abstractContainer;

    private Plugin plugin;
    private File file;
    public FileConfiguration config;

    public AbstractContainer(Plugin plugin, String filename) {
        abstractContainer = this;

        this.plugin = plugin;
        this.file = new File(plugin.getDataFolder(), filename);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        this.config = YamlConfiguration.loadConfiguration(file);
    }

    /**
     * A method which saves the file.
     * @return true/false (successfully saved or not)
     */
    public boolean save() {
        try {
            config.save(file);
            return true;

        } catch (IOException e) {
            e.printStackTrace();
        }

        return false;
    }

    /**
     * A method which allows you to get certain data
     * from the dedicated container/file.
     * Consumer has been used here to use the callback.
     *
     * @param path the path for the object you need
     * @param defaultObject default return value
     * @param callback the consumer which the return value
     */
    public void getAsync(String path, Object defaultObject, Consumer<Object> callback) {
        executor.execute(() -> callback.accept(this.getSync(path, defaultObject)));
    }

    /**
     * A method which passes the data from config at a
     * certain path next to the getAsync method.
     *
     * @param path the path for the object you need
     * @param defaultObject default return value
     * @return the object
     */
    private Object getSync(String path, Object defaultObject) {
        return this.config.get(path, defaultObject);
    }

    /**
     * A method which sets a certain path to a value
     * and automatically saves it in the file.
     *
     * Also returns a consumer which informs either the
     * saving process was successful or not.
     *
     * @param path the path for the object to be saved at
     * @param value the value which you want it to be set to
     * @param consumer the consumer with true or false return, can be null
     *                 if you don't need to work with process of successful
     *                 saving. So, if you want to inform a player in chat
     *                 that something was saved then you can use this!
     */
    private void setAsync(String path, Object value, Consumer<Boolean> consumer) {
        executor.execute(() -> {
            this.config.set(path, value);

            if (consumer != null) {
                consumer.accept(this.save());
            }
        });
    }

    public static AbstractContainer getInstance() {
        return abstractContainer;
    }
}
