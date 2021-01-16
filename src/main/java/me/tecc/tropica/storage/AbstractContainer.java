package me.tecc.tropica.storage;

import me.tecc.tropica.Tropica;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

public abstract class AbstractContainer implements IStorage {
    private final static ExecutorService executor = Executors.newCachedThreadPool();

    private final Plugin plugin;
    private final File file;
    public FileConfiguration config;
    private static int saving = 0;
    private static BukkitRunnable bukkitRunnable = null;

    public AbstractContainer(Plugin plugin, String filename) {

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
     *
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
     * A method which saves the file asynchronously.
     *
     * @return true/false (successfully saved or not)
     */
    public boolean saveAsync() {
        CompletableFuture<Boolean> future = CompletableFuture.supplyAsync(() -> {
            try {
                config.save(file);
                return true;
            } catch (IOException e) {
                e.printStackTrace();
            }

            return false;
        });

        try {
            return future.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        return false;
    }

    /**
     * A method which allows you to get certain data
     * from the specified container/file.
     * Consumer has been used here to use the callback.
     *
     * @param path          The path for the object you need
     * @param defaultObject The default return value.
     * @param callback      The consumer that will, once
     *                      ready, accept the return value.
     */
    public void getAsync(String path, Object defaultObject, Consumer<Object> callback) {
        executor.execute(() -> callback.accept(this.getSync(path, defaultObject)));
    }

    public Object getAsync(String path, Object defaultObject) throws ExecutionException, InterruptedException {
        // create CompletableFuture for getting it asynchronously using getSync method
        CompletableFuture<Object> future = CompletableFuture.supplyAsync(() -> this.getSync(path, defaultObject));
        return future.get();
    }

    /**
     * A method which passes the data from config at a
     * certain path next to the getAsync method.
     *
     * @param path          the path for the object you need
     * @param defaultObject default return value
     * @return the object
     */
    private Object getSync(String path, Object defaultObject) {
        return this.config.get(path, defaultObject);
    }

    /**
     * A method which sets a certain path to a value
     * and automatically saves it in the file.
     * <p>
     * Also returns a consumer which informs either the
     * saving process was successful or not.
     *
     * @param path     the path for the object to be saved at
     * @param value    the value which you want it to be set to
     * @param consumer the consumer with true or false return, can be null
     *                 if you don't need to work with process of successful
     *                 saving. So, if you want to inform a player in chat
     *                 that something was saved then you can use this!
     */
    public void setAsync(String path, Object value, Consumer<Boolean> consumer) {
        executor.execute(() -> {
            this.config.set(path, value);
            saving = 3;

            if (bukkitRunnable == null) {
                bukkitRunnable = new BukkitRunnable() {
                    @Override
                    public void run() {
                        saving -= 1;
                        if (saving <= 0) {
                            boolean save = save();

                            if (consumer != null) {
                                consumer.accept(save);
                            }
                            this.cancel();
                            bukkitRunnable = null;
                        }
                    }
                };

                bukkitRunnable.runTaskTimer(Tropica.getTropica(), 0, 20L);
            }
        });
    }
}
