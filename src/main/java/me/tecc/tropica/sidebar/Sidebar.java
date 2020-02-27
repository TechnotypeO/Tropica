package me.tecc.tropica.sidebar;

import me.tecc.tropica.TUtil;
import me.tecc.tropica.Tropica;
import me.tecc.tropica.features.playerData.PlayerWrapper;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.DisplaySlot;

public class Sidebar {
    public static void sidebar(Player player) {
        PlayerWrapper playerWrapper = new PlayerWrapper(player);
        int knowledge = playerWrapper.getInt("knowledge");

        DynamicScoreboard d = new DynamicScoreboard(TUtil.toColor("&2[&a&lTROPICA&2] &e&lBETA!"), DisplaySlot.SIDEBAR);
        d.addField("&7&o" + TUtil.getCurrentDateString(), "");
        d.addField("", "");
        d.addField("This game is", " currently");
        d.addField("under heavy ", "development&7!");
        d.addField("&e&m-----------", "&e&m---------");
        d.addField("", "");
        d.addField("Knowledge:", " &b"+knowledge);
        d.addField("", "");
        d.addField("&asloverteam.net", "");
        d.applyScoreboard(player);
    }

    public static void updateKnowledge(PlayerWrapper wrapper, int value) {
        Player player = wrapper.getPlayer();
        int knowledge = wrapper.getInt("knowledge");

        DynamicScoreboard d = DynamicScoreboard.scoreboards.get(player.getUniqueId());
        int index = d.getFields().size();

        d.updateField(index - 3, "Knowledge:",
                " &b"+TUtil.toFancyNumber(knowledge) + " &3(+"+TUtil.toFancyNumber(value)+")");
        new BukkitRunnable() {

            @Override
            public void run() {
                if (player.isOnline()) {
                    d.updateField(index - 3, "Knowledge:", " &b"+TUtil.toFancyNumber(knowledge));
                }
            }
        }.runTaskLater(Tropica.getTropica(), 20*3);
    }
}
