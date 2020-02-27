package me.tecc.tropica.sidebar;

import me.tecc.tropica.TUtil;
import me.tecc.tropica.features.playerData.PlayerWrapper;
import org.bukkit.entity.Player;
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
}
