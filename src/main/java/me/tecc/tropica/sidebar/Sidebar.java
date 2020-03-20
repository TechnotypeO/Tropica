package me.tecc.tropica.sidebar;

import me.tecc.tropica.TUtil;
import me.tecc.tropica.Tropica;
import me.tecc.tropica.features.playerData.PlayerWrapper;
import me.tecc.tropica.features.teams.TeamHandler;
import me.tecc.tropica.features.teams.TeamType;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.DisplaySlot;

public class Sidebar {
    public static void sidebar(Player player) {
        PlayerWrapper playerWrapper = new PlayerWrapper(player);
        int knowledge = playerWrapper.getInt("knowledge");
        double cash = playerWrapper.getDouble("cash");

        DynamicScoreboard d = new DynamicScoreboard(TUtil.toColor("&2[&a&lTROPICA&2] &e&lBETA!"), DisplaySlot.SIDEBAR);
        d.addField("&7&o" + TUtil.getCurrentDateString(), "");
        d.addField("", "");
        d.addField("This game is", " currently");
        d.addField("under heavy ", "development&7!");
        d.addField("&e&m-----------", "&e&m---------");
        d.addField("", "");
        d.addField("Team: ", TeamHandler.getInstance().getTeamTypeMap().getOrDefault(player.getUniqueId(), TeamType.NONE).getPrefix());
        d.addField("", "");
        d.addField("Knowledge:", " &b"+TUtil.toFancyNumber(knowledge));
        d.addField("Cash: ", "&6"+TUtil.toFancyDouble(cash));
        d.addField("", "");
        d.addField("&asloverteam.net", "");
        d.applyScoreboard(player);
    }

    public static void updateKnowledge(PlayerWrapper wrapper, int value) {
        Player player = wrapper.getPlayer();
        int knowledge = wrapper.getInt("knowledge");

        DynamicScoreboard d = DynamicScoreboard.scoreboards.get(player.getUniqueId());
        int index = d.getFields().size();

        d.updateField(index - 4, "Knowledge:",
                " &b"+TUtil.toFancyNumber(knowledge) + " &3(+"+TUtil.toFancyNumber(value)+")");
        new BukkitRunnable() {

            @Override
            public void run() {
                if (player.isOnline()) {
                    d.updateField(index - 4, "Knowledge:", " &b"+TUtil.toFancyNumber
                            (wrapper.getInt("knowledge")));
                }
            }
        }.runTaskLater(Tropica.getTropica(), 20*3);
    }

    public static void updateCash(PlayerWrapper wrapper, double value) {
        Player player = wrapper.getPlayer();
        double cash = wrapper.getDouble("cash");

        DynamicScoreboard d = DynamicScoreboard.scoreboards.get(player.getUniqueId());
        int index = d.getFields().size();

        if (value > 0) {
            d.updateField(index - 3, "Cash:",
                    " &6" + TUtil.toFancyDouble(cash) + " &e(+" + TUtil.toFancyDouble(value) + ")");
        } else {
            d.updateField(index - 3, "Cash:",
                    " &6" + TUtil.toFancyDouble(cash) + " &c(-" + TUtil.toFancyDouble(Math.abs(value)) + ")");

        }

        new BukkitRunnable() {

            @Override
            public void run() {
                if (player.isOnline()) {
                    d.updateField(index - 3, "Cash:", " &6"+TUtil.toFancyDouble(wrapper.getDouble("cash")));
                }
            }
        }.runTaskLater(Tropica.getTropica(), 20*3);
    }
}
