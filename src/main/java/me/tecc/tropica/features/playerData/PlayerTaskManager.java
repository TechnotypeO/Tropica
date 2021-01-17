package me.tecc.tropica.features.playerData;

import me.tecc.tropica.TUtil;
import me.tecc.tropica.storage.PlayerContainer;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.text.DecimalFormat;
import java.util.Collection;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Saved players data every 1 minute
 */
public class PlayerTaskManager extends BukkitRunnable {
    private static PlayerTaskManager instance;
    private final DecimalFormat decimalFormat = new DecimalFormat("#.##");
    private final ExecutorService executorService = Executors.newCachedThreadPool();

    public PlayerTaskManager() {
        instance = this;
    }

    @Override
    public void run() {
        executorService.execute(() -> {
            Collection<PlayerFeature> collection = PlayerFeature.getCollection();
            collection.forEach(this::save);
        });
    }

    public void save(PlayerFeature c) {
        if (c.isLoaded()) {
            long data = System.currentTimeMillis();

            Player player = c.getPlayer();
            String uuid = player.getName();

            String toBeSaved = c.getJsonObject().toString();
            PlayerContainer.getInstance().setAsync(uuid, toBeSaved, aBoolean -> {
                if (aBoolean) {
                    long difference = System.currentTimeMillis() - data;
                    long seconds = (difference / 1000);
                    double ceil = Math.ceil((double) seconds);

                    TextComponent textComponent = new TextComponent(
                            TUtil.toColor("&aSaved your data in &f" + decimalFormat.format(ceil) + "s&a!"));

                    player.spigot().sendMessage(ChatMessageType.ACTION_BAR, textComponent);
                }
            });
        }
    }

    public static PlayerTaskManager getInstance() {
        return instance;
    }
}
