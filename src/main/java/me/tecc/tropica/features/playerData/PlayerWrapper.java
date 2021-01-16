package me.tecc.tropica.features.playerData;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.bukkit.entity.Player;

import java.util.UUID;

/**
 * Very useful Wrapper for all kind of player data.
 * Create a new instance of this class along with your code
 * when you want to save or get certain data.
 * <p>
 * Set values are only saved in {@link PlayerFeature}'s json field.
 * All data is automatically being saved every 5 minutes without any lag.
 * After creating instance you can use all get/set methods in your code.
 */
public class PlayerWrapper {
    private final PlayerFeature playerFeature;
    private final JsonObject jsonObject;
    private final UUID uuid = null;
    private Player player = null;

    /**
     * Simply use this constructor to work along with player data.
     * Available features:
     * + Setting
     * + Getting
     * Supports int, double, string, boolean, json
     *
     * @param player the player
     */
    public PlayerWrapper(Player player) {
        this.player = player;
        this.playerFeature = PlayerFeature.parse(player);
        this.jsonObject = playerFeature.getJsonObject();
    }

    public JsonArray getJsonArray(String path) {
        return getElement(path).getAsJsonArray();
    }

    public void setJsonArray(String path, JsonArray jsonArray) {
        jsonObject.add(path, jsonArray);
    }

    public JsonObject getJson(String path) {
        return getElement(path).getAsJsonObject();
    }

    public void setJson(String path, JsonObject jsonObject) {
        jsonObject.add(path, jsonObject);
    }

    public boolean getBoolean(String path) {
        return getElement(path).getAsBoolean();
    }

    public void setBoolean(String path, boolean b) {
        jsonObject.addProperty(path, b);
    }

    public String getString(String path) {
        return getElement(path).getAsString();
    }

    public void setString(String path, String s) {
        jsonObject.addProperty(path, s);
    }

    public int getInt(String path) {
        return getElement(path).getAsInt();
    }

    public void setInt(String path, int i) {
        jsonObject.addProperty(path, i);
    }

    public double getDouble(String path) {
        return getElement(path).getAsDouble();
    }

    public void setDouble(String path, double d) {
        jsonObject.addProperty(path, d);
    }

    private JsonElement getElement(String path) {
        return jsonObject.get(path);
    }

    public JsonObject getJsonObject() {
        return jsonObject;
    }

    public Player getPlayer() {
        return player;
    }

    public PlayerFeature getPlayerFeature() {
        return playerFeature;
    }

    public UUID getUuid() {
        return uuid;
    }
}
