package me.tecc.tropica.sidebar;

import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class Rank {
    public static List<RankObject> rankObjects = new ArrayList<>();
    private int rarity = 1000;

    public Rank() {
        registerRanks();
    }

    private void registerRanks() {
        register("admin", "&c[ADMIN]", "&c");
        register("srmod", "&2[SR MOD]", "&2");
        register("mod", "&2[MOD]", "&2");
        register("builder", "&3[BUILDER]", "&3");
        register("translator", "&3[TRANSLATE]", "&3");
        register("helper", "&9[HELPER]", "&9");//14
        register("tester", "&b[TESTER]", "&b");//13
        register("partner", "&d[PARTNER]", "&d");
        register("retired", "&a[RETIRED]", "&a");
        register("youtuber", "&6[YT]", "&6");
        register("gold", "&6[GOLD]", "&6");
        register("legend", "&5[LEGEND]", "&5");
        register("crystal_plus", "&d[CRYSTAL+]", "&d");
        register("crystal", "&d[CRYSTAL]", "&d");
        register("hvp", "&b[HVP]", "&b");
        register("nitro", "&d[NITRO]", "&d");
        register("vip_plus", "&a[VIP&c+&a]", "&a");
        register("vip", "&a[VIP]", "&a");
        register("player", "&7", "");
    }

    private void register(final String name, final String prefix, final String color) {
        rankObjects.add(new RankObject(name, prefix, color, rarity));
        rarity -= 1;

    }

    public static String getRank(final Player player) {
        for (final RankObject rankObject : rankObjects) {
            if (player.hasPermission("slover.rank."+rankObject.getName())) {
                return rankObject.getName();
            }
        }
        return "player";
    }

    public static String getPrefix(final String s) {
        if (s.equalsIgnoreCase("player")) {
            return "";
        }

        for (final RankObject rankObject : rankObjects) {
            if (s.equalsIgnoreCase(rankObject.getName())) {
                return rankObject.getPrefix()+" "+rankObject.getColor();
            }
        }
        return "&7";
    }

    public static String getColor(final String s) {
        if (s.equalsIgnoreCase("player")) {
            return "&7";
        }

        for (RankObject rankObject : rankObjects) {
            if (s.equalsIgnoreCase(rankObject.getName())) {
                return rankObject.getColor();
            }
        }
        return "&7";
    }

    public static boolean hasRank(final Player player, final String rank) {
        return player.hasPermission("slover.rank." + rank);
    }

}
