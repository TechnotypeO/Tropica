package me.tecc.tropica.features.teams;

public enum TeamType {
    PANDA("&aPanda"),
    SPIDER("&cSpider"),
    MONKEY("&6Monkey"),
    SILVERFISH("&7Silverfish"),
    NONE("&eNone");

    private final String prefix;

    TeamType(String prefix) {
        this.prefix = prefix;
    }

    public String getPrefix() {
        return prefix;
    }
}
