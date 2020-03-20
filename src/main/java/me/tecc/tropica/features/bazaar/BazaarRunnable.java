package me.tecc.tropica.features.bazaar;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import me.tecc.tropica.Tropica;
import me.tecc.tropica.storage.PublicContainer;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.LinkedList;

public class BazaarRunnable extends BukkitRunnable {
    private BazaarHandler bazaarHandler;

    public BazaarRunnable(BazaarHandler bazaarHandler) {
        this.bazaarHandler = bazaarHandler;

        this.runTaskTimer(Tropica.getTropica(), 20*10L, 20*60L);
    }

    @Override
    public void run() {
        LinkedList<JsonObject> jsonObjects = bazaarHandler.getAuctions();
        JsonArray jsonElements = new JsonArray();

        for (JsonObject jsonObject : jsonObjects) {
            jsonElements.add(jsonObject);
        }

        PublicContainer.getInstance().setAsync("bazaar", jsonElements.toString(), null);
    }
}
