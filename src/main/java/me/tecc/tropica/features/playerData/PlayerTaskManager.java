package me.tecc.tropica.features.playerData;

import me.tecc.tropica.TUtil;
import me.tecc.tropica.storage.PlayerContainer;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Collection;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Saved players data every 5 minutes
 */
public class PlayerTaskManager extends BukkitRunnable {
    private static PlayerTaskManager instance;
    private ExecutorService executorService = Executors.newCachedThreadPool();

    public PlayerTaskManager() {
        instance = this;
    }

    @Override
    public void run() {
        executorService.execute(()->{
            Collection<PlayerFeature> collection = PlayerFeature.getCollection();
            collection.forEach(this::save);
        });
    }

    public void save(PlayerFeature c) {
        if (c.isLoaded()) {
            long data = System.currentTimeMillis();

            Player player = c.getPlayer();
            UUID uuid = player.getUniqueId();

            String toBeSaved = c.getJsonObject().toString();
            PlayerContainer.getInstance().setAsync(uuid.toString(), toBeSaved, aBoolean -> {
                if (aBoolean) {
                    long difference = System.currentTimeMillis() - data;
                    long seconds = (difference / 1000);

                    TextComponent textComponent = new TextComponent(
                            TUtil.toColor("&aSaved your data in &f"+(Math.ceil((double)seconds))+"s&a!"));

                    player.spigot().sendMessage(ChatMessageType.ACTION_BAR, textComponent);
                }
            });
        }
    }

    public static PlayerTaskManager getInstance() {
        return instance;
    }
}
