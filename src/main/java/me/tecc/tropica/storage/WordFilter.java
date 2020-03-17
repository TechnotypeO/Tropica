package me.tecc.tropica.storage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

public class WordFilter {

    public static Collection<String> badWords = new ArrayList<>();

    public WordFilter() {
        this.registerBadWords(
                "sex", "nigga", "nigger", "fuck", "bitch",
                "nigger", "cum", "sperm", "fucking", "penis", "vagina",
                "cunt", "cock", "dick", "blowjob", "handjob", "porn");
    }

    private void registerBadWords(String... strings) {
        for (String string : strings) {

            String wideText = this.wideText(string);
            String[] symbolTexts = this.symbolText(string, wideText);

            badWords.add(string);
            badWords.add(wideText);
            badWords.addAll(Arrays.asList(symbolTexts));
        }
    }

    private String[] symbolText(String... strings) {
        String[] strings1 = new String[strings.length];

        for (int i = 0; i < strings.length; i++) {
            String string = strings[i];
            strings1[i] = string.replaceAll("s", "\\$");
        }

        return strings1;
    }

    private String wideText(String string) {
        char[] chars = string.toCharArray();
        StringBuilder newString = new StringBuilder();

        for (char c : chars) {
            newString.append(c).append(" ");
        }

        return newString.substring(0, newString.length() - 1);
    }

    public static boolean containsBadWords(String string) {
        for (String badWord : badWords) {
            if (string.toLowerCase().contains(badWord.toLowerCase())) {
                return true;
            }
        }

        return false;
    }

}
