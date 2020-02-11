package me.tecc.tropica;

import org.bukkit.NamespacedKey;

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
}
