package me.tecc.tropica.texts;

import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static me.tecc.tropica.TUtil.toColor;

public class Text {
    private List<String> stringList = null;
    private String[] strings = null;
    private String string = null;

    public Text(final String string) {
        this.string = string;
    }

    public Text(final String[] strings) {
        this.strings = strings;
    }

    public Text(final List<String> stringList) {
        this.stringList = stringList;
    }

    public String toString() {
        if (strings != null) {
            StringBuilder s = new StringBuilder();
            for (String s1 : strings) {
                s.append("\n").append(s1);
            }
            return toColor(s.toString());
        }
        if (string != null) {
            return toColor(toColor(string));
        }
        if (stringList != null) {
            StringBuilder s = new StringBuilder();
            for (String s1 : stringList) {
                s.append("\n").append(s1);
            }
            return toColor(s.toString());
        }
        return null;
    }

    public List<String> toList() {
        if (strings != null) {
            return toColor(new ArrayList<>(Arrays.asList(strings)));
        }
        if (string != null) {
            return toColor(new ArrayList<>(Collections.singletonList(string)));
        }
        if (stringList != null) {
            return toColor(stringList);
        }
        return null;
    }

    public String[] toArray() {
        if (strings != null) {
            return toColor(strings);
        }
        if (string != null) {
            String[] strings1 = new String[1];
            strings1[0] = string;
            return toColor(strings1);
        }
        if (stringList != null) {
            String[] strings1 = new String[stringList.size()];
            for (int i = 0; i < stringList.size(); i++) {
                String s = stringList.get(i);
                strings1[i] = toColor(s);
            }
            return toColor(strings1);
        }
        return null;
    }

    public void send(final Player player) {
        player.sendMessage(toColor(this.toString()));
    }

    public void send(final ConsoleCommandSender player) {
        player.sendMessage(toColor(this.toString()));
    }

    public void send(final CommandSender commandSender) {
        commandSender.sendMessage(toColor(this.toString()));
    }
}
