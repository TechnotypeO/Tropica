package me.tecc.tropica;

import org.bukkit.ChatColor;
import org.bukkit.NamespacedKey;

import java.util.ArrayList;
import java.util.List;

public class TUtil {
    /**
     * Creates a namespaced key using {@link Tropica#getTropica()} as plugin and
     * the supplied key as the actual key.
     *
     * @param key The key to use.
     * @return A {@link NamespacedKey} formatted like "{@code tropica:<key>}"
     *
     * @see NamespacedKey
     */
    public static NamespacedKey getNamespacedKey(String key) {
        // return new namespaced key using
        // tropica instance as plugin and key param as key
        return new NamespacedKey(Tropica.getTropica(), key);
    }

    /**
     * Transforms text with alternative color codes into
     * minecraft valid colored text.
     * @param s Text with alternative color codes.
     * @return {@link String}, Minecraft valid colored text.
     */
    public static String toColor(String s) {
        return ChatColor.translateAlternateColorCodes('&', s);
    }

    /**
     * Same as toColor(String) method, but for a string list.
     * @param s String list
     * @return {@link List <String>}, translated to Minecraft valid colored text.
     */
    public static List<String> toColor(List<String> s) {
        List<String> strings = new ArrayList<>();
        for (String ss : s) {
            strings.add(toColor(ss));
        }
        return strings;
    }

    /**
     * Same as toColor(String) method, but for a string array.
     * @param s String array
     * @return {@link String[]}, translated to Minecraft valid colored text.
     */
    public static String[] toColor(String[] s) {
        String[] strings = new String[s.length];
        for (int i = 0; i < s.length; i++) {
            String ss = s[i];
            strings[i] = ss;
        }
        return strings;
    }
}
