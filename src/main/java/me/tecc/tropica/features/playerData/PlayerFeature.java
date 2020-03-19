package me.tecc.tropica.features.playerData;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import me.tecc.tropica.Tropica;
import me.tecc.tropica.storage.PlayerContainer;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.function.Consumer;

/**
 * PlayerFeature is a class which holds all players' custom data which
 * is stored in the playerContainer.yml. All keys in this file are UUIDs
 * and values are JsonObjects converted into Strings.
 *
 * This class holds fields like the static collection of all unique
 * instances of this particular class.
 *
 * The cache map holds PlayerFeature instances which depend on the UUIDs.
 * This map is used to quickly obtain the data by providing player's UUID.
 * The whole process is being done automatically in the constructor and
 * the parse method.
 */
public class PlayerFeature {
    // collections of all instances
    private static Collection<PlayerFeature> playerFeatures = new ArrayList<>();
    // cache with objects depended on UUIDs
    private static Map<UUID, PlayerFeature> cache = new HashMap<>();
    // this is a runnable which saves all changes every 60s
    // 'all changes' are basically all changes with have been
    // done to the jsonObject through PlayerWrapper.class
    private static PlayerTaskManager playerTaskManager = null;

    // useful fields
    private Player player;
    private boolean isLoaded = false;
    private JsonObject jsonObject = new JsonObject();

    /**
     * Creates the instance of the class.
     * After create the instance, it gathers json object from
     * the playerContainter.yml.
     *
     * @param player the player
     */
    public PlayerFeature(Player player, Consumer<Boolean> consumer) {
        this.player = player;

        //get json from files
        PlayerContainer.getInstance().getAsync(player.getUniqueId().toString(), "",
                o -> {
                    final String string = (String) o;

                    //default values here
                    if (string.equals("")) {
                        JsonObject data = new JsonObject();
                        //all default values
                        data.addProperty("knowledge", 0);
                        data.addProperty("cash", 100.0D);
                        data.addProperty("team", "none");

                        jsonObject = data;
                    } else {
                        //reading from files
                        jsonObject = new JsonParser().parse(string).getAsJsonObject();
                        if (!jsonObject.has("cash")) {
                            jsonObject.addProperty("cash", 100D);
                        }
                        if (jsonObject.has("team")) {
                            jsonObject.addProperty("team", "none");
                        }
                    }
                    isLoaded = true;
                    if (consumer != null) consumer.accept(true);
                });

        playerFeatures.add(this);

        // initialization of auto saver (runnable)
        if (playerTaskManager == null) {
            playerTaskManager = new PlayerTaskManager();
            playerTaskManager.runTaskTimer(Tropica.getTropica(), 20*60L, 20*60*5L);
        }
    }

    /**
     * A method which returns the PlayerFeature instance
     * based on the inserted player.
     *
     * @param player the target
     * @return the instance of object
     */
    public static PlayerFeature parse(Player player) {
        // checking for cache
        if (cache.containsKey(player.getUniqueId())) {
            return cache.get(player.getUniqueId());
        }

        PlayerFeature playerFeature = null;

        // searching in the collection
        for (PlayerFeature pf : playerFeatures) {
            if (pf.getPlayer().equals(player)) {
                playerFeature = pf;
                break;
            }
        }

        // checking if null, then creating a new instance if not created
        if (playerFeature == null) {
            playerFeature = new PlayerFeature(player, null);
        }
        // inserting into cache
        cache.put(player.getUniqueId(), playerFeature);

        return playerFeature;

    }

    public static void quit(Player player) {
        PlayerFeature playerFeature = parse(player);
        playerTaskManager.save(playerFeature);

        cache.remove(player.getUniqueId());
        playerFeatures.remove(playerFeature);
    }


    public static Collection<PlayerFeature> getCollection() {
        return playerFeatures;
    }

    public boolean isLoaded() {
        return isLoaded;
    }

    public JsonObject getJsonObject() {
        return jsonObject;
    }

    public Player getPlayer() {
        return player;
    }
}
