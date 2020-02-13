package me.tecc.tropica.features.collection;

import org.bukkit.Material;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public class CollectionLeaderboard {
    private Material material;
    private Map<UUID, Integer> places;
    private List<String> itemLore;

    public CollectionLeaderboard(Material material) {
        this.material = material;
    }

    public void setPlaces(Map<UUID, Integer> places) {
        this.places = places;
    }

    public Map<UUID, Integer> getPlaces() {
        return places;
    }

    public Material getMaterial() {
        return material;
    }

    public void setLore(List<String> itemLore) {
        this.itemLore = itemLore;
    }

    public List<String> getItemLore() {
        return itemLore;
    }
}
