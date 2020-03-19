package me.tecc.tropica.features.teams;

import me.tecc.tropica.features.playerData.PlayerWrapper;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class TeamHandler {
    private static TeamHandler teamHandler;
    private Map<UUID, TeamType> teamTypeMap = new HashMap<>();

    public TeamHandler() {
        teamHandler = this;
    }

    public void loadPlayer(PlayerWrapper playerWrapper) {
        String savedTeam = playerWrapper.getString("team");
        TeamType teamType = TeamType.valueOf(savedTeam);
        teamTypeMap.put(playerWrapper.getPlayer().getUniqueId(), teamType);
    }

    public void unloadPlayer(Player player) {
        teamTypeMap.remove(player.getUniqueId());
    }

    public static boolean isTeam(Player player, TeamType teamType) {
        TeamHandler teamHandler = TeamHandler.getInstance();
        return (teamHandler.getTeamTypeMap().get(player.getUniqueId()).equals(teamType));
    }


    public Map<UUID, TeamType> getTeamTypeMap() {
        return teamTypeMap;
    }

    public static TeamHandler getInstance() {
        return teamHandler;
    }
}
