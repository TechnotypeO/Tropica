package me.tecc.tropica.sidebar;

import me.tecc.tropica.TUtil;

public class RankObject {
    private final String name;
    private final String prefix;
    private final String color;
    private final int rarity;

    public RankObject(String name, String prefix, String color, int rarity) {
        this.name = name;
        this.prefix = prefix;
        this.color = color;
        this.rarity = rarity;
    }

    public static RankObject parseRank(String nick) {
        final String[] spacing = nick.split(";;");
        return new RankObject(spacing[0], spacing[1], spacing[2], (int) TUtil.fromFancyNumber(spacing[3]));
    }

    public static RankObject getByName(String name) {
        if (name.equalsIgnoreCase("player")) {
            return new RankObject("player", "&7", "&7", -1);
        }
        for (RankObject rankObject : Rank.rankObjects) {
            if (rankObject.getName().equalsIgnoreCase(name)) {
                return new RankObject(rankObject.getName(), rankObject.getPrefix(), rankObject.getColor(), rankObject.getRarity());
            }
        }
        return null;
    }

    public int getRarity() {
        return rarity;
    }

    public String getColor() {
        return color;
    }

    public String getPrefix() {
        return prefix;
    }

    public String getName() {
        return name;
    }

    public String toStringObject() {
        return getName()+";;"+getPrefix()+";;"+getColor()+";;"+rarity;
    }

}
