package me.tecc.tropica.sidebar;

import me.tecc.tropica.TUtil;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.util.*;

public class DynamicScoreboard {
    private static int customID = 0;
    private static ChatColor[] chatColors = ChatColor.values();
    @SuppressWarnings("WeakerAccess")
    public static LinkedHashMap<UUID, DynamicScoreboard> scoreboards = new LinkedHashMap<>();

    private Scoreboard scoreboard;
    private Objective objective;
    private LinkedList<Team> fields;

    public DynamicScoreboard(final String scoreboardTitle, final DisplaySlot displaySlot) {
        customID += 1;
        this.scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
        this.objective = this.scoreboard.registerNewObjective(customID+"DS", "dummy", "dummy");
        this.objective.setDisplaySlot(displaySlot);
        this.objective.setDisplayName(TUtil.toColor(scoreboardTitle));
        this.fields = new LinkedList<>();
    }

    public void addField(final String prefix, final String suffix) {
        final int currentSize = fields.size();
        String entry = chatColors[currentSize].toString() + chatColors[currentSize / 4].toString() + chatColors[chatColors.length - 1];

        Team team = this.scoreboard.registerNewTeam(currentSize+"T");
        team.addEntry(entry);

        team.setPrefix(TUtil.toColor(prefix));
        team.setSuffix(TUtil.toColor(suffix));

        fields.add(team);
        this.objective.getScore(entry).setScore(16 - currentSize);
    }

    public void updateField(final int index, final String prefix, final String suffix) {
        try {
            Team team = this.scoreboard.getTeam(index+"T");

            team.setPrefix(TUtil.toColor(prefix));
            team.setSuffix(TUtil.toColor(suffix));

        } catch (IllegalArgumentException ignored) {}
    }

    public Team getField(final int index) {
        return fields.get(index);
    }

    public void clearFields() {
        for (Team team : this.scoreboard.getTeams()) {
            team.unregister();
        }

        fields.clear();
    }

    public void applyScoreboard(final Player player) {
        player.setScoreboard(this.scoreboard);
        scoreboards.put(player.getUniqueId(), this);
    }

    public LinkedList<Team> getFields() {
        return fields;
    }

    public static int getCustomID() {
        return customID;
    }

    public Scoreboard getScoreboard() {
        return scoreboard;
    }

    public Objective getObjective() {
        return objective;
    }

    public DynamicScoreboard getScoreboard(final Player player) {
        return scoreboards.get(player.getUniqueId());
    }


    public static void updateTeamScoreboard() {
        Collection<UUID> uuidCollection = new ArrayList<>();

        for (Player player : Bukkit.getOnlinePlayers()) {
            final Scoreboard scoreboard = player.getScoreboard();

            int i = 1;
            for (RankObject rankObject : Rank.rankObjects) {
                Team team = scoreboard.getTeam("0" + i + rankObject.getName());
                if (team != null) {
                    team.unregister();
                }
                team = scoreboard.registerNewTeam("0" + i + rankObject.getName());
                team.setOption(Team.Option.COLLISION_RULE, Team.OptionStatus.NEVER);
                team.setColor(org.bukkit.ChatColor.getByChar(rankObject.getColor().charAt(1)));

                if (rankObject.getName().equalsIgnoreCase("player")) {
                    team.setPrefix(TUtil.toColor("&7"));
                } else {
                    team.setPrefix(TUtil.toColor(rankObject.getPrefix() + " " + rankObject.getColor()));
                }

                for (final Player player1 : Bukkit.getOnlinePlayers()) {
                    if (player1.hasPermission("slover.rank."+rankObject.getName())) {
                        if (!uuidCollection.contains(player1.getUniqueId())) {
                            team.addEntry(player1.getName());
                            uuidCollection.add(player1.getUniqueId());
                        }
                    }
                }
                i++;
            }
        }
    }
}
