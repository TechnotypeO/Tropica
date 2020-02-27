package me.tecc.tropica;

import org.bukkit.ChatColor;
import org.bukkit.NamespacedKey;

import java.io.File;
import java.text.NumberFormat;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

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
     * Same as toColor(String[]) method, but for a string list.
     * @param s String list
     * @return {@link List<String>}, translated to Minecraft valid colored text.
     */
    public static List<String> toColor(List<String> s) {
        return Arrays.asList(toColor((String[]) s.toArray()));
    }

    /**
     * Same as toColor(String) method, but for a string array.
     * @param s String array
     * @return {@link String[]}, translated to Minecraft valid colored text.
     */
    public static String[] toColor(String[] s) {
        for (int i = 0; i < s.length; i++) {
            String si = s[i];
            s[i] = toColor(si);
        }
        return s;
    }

    /**
     * Transforms a number into fancy cost.
     * Example: 1000 --> 1,000
     * @param num The number to transform into a fancy number.
     * @return a {@link String} of the fancy number.
     */
    public static String toFancyNumber(long num) {
        return NumberFormat.getInstance(Locale.US).format(num);
    }

    /**
     * Transforms a double into fancy cost.
     * Example 50000.2 --> 50,000.2
     * @param num The double to transform into fancy cost.
     * @return a {@link String} of the double (in fancy cost)
     */
    public static String toFancyDouble(double num) {
        return NumberFormat.getInstance(Locale.US).format(num);
    }

    /**
     * Translates a fancy-cost number (as a String) to Integer.
     * @param num The string to translate into Integer.
     * @return {@link Integer}
     */
    public static long fromFancyNumber(String num) {
        NumberFormat format = NumberFormat.getInstance(Locale.US);
        Number number = 0;
        try {
            number = format.parse(num);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return number.longValue();
    }

    /**
     * Translates a fancy-cost double (as a String) to Double.
     * @param num The string to translate into Double.
     * @return {@link Double}
     */
    public static double fromFancyDouble(String num) {
        NumberFormat format = NumberFormat.getInstance(Locale.US);
        Number number = 0.0D;
        try {
            number = format.parse(num);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return number.doubleValue();
    }

    /**
     * Gets a file from the plugin resource directory ({@code ~/plugins/Tropica/}).
     */
    public static File getFileFromPluginDir(String fileName) {
        // get tropica plugin data folder
        File path = Tropica.getTropica().getDataFolder();
        // resolve file using the File(File, String) constructor
        return new File(path, fileName);
    }

    /**
     * Gets the current date.
     * Format: dd/MM/yyyy
     * @return Current date as {@link String}
     */
    public static String getCurrentDateString() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDate localDate = LocalDate.now();
        return dtf.format(localDate);
    }
}
