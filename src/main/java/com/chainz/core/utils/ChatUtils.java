package com.chainz.core.utils;

import org.bukkit.ChatColor;

import java.util.ArrayList;

public class ChatUtils {
    public static String getUrlDomainName(String url) {
        String domainName = url;
        int index = domainName.indexOf("://");
        if (index != -1)
            domainName = domainName.substring(index + 3);
        index = domainName.indexOf('/');
        if (index != -1)
            domainName = domainName.substring(0, index);
        domainName = domainName.replaceFirst("^www.*?\\.", "");
        return domainName;
    }

    private static Integer getChars(String s, String c) {
        int rep = 0;
        for (int i = 0; i < s.length(); i++) {
            char ch = s.charAt(i);
            if (Character.toString(ch).equals(c))
                rep++;
        }
        return rep;
    }

    private static boolean hasLetters(String word) {
        boolean bol = false;
        for (int i = 0; i < word.length(); i++) {
            char ch = word.charAt(i);
            if (Character.isLetter(ch)) {
                bol = true;
                break;
            }
        }
        return bol;
    }

    public static ArrayList<String> getUrls(String text) {
        ArrayList<String> containedUrls = new ArrayList<>();
        String noColorText = ChatColor.stripColor(text);
        String[] words = noColorText.split(" ");
        for (String word : words) {
            if (word.startsWith("http://") || word.startsWith("https://") || (word.contains("/") && word.contains(".") &&
                    !word.startsWith(".") && !word.startsWith("/")) || (getChars(word, ".") >= 1 && hasLetters(word) &&
                    !word.startsWith(".") && !word.endsWith(".")))
                containedUrls.add(word);
        }
        return containedUrls;
    }

    public static ArrayList<Integer> getUrlsIndex(String text) {
        ArrayList<Integer> urlsIndex = new ArrayList<>();
        String noColorText = ChatColor.stripColor(text);
        String[] words = noColorText.split(" ");
        for (int i = 0; i < words.length; i++) {
            String word = words[i];
            if (word.startsWith("http://") || word.startsWith("https://") || (word.contains("/") && word.contains(".") &&
                    !word.startsWith(".") && !word.startsWith("/")) || (getChars(word, ".") >= 1 && hasLetters(word) &&
                    !word.startsWith(".") && !word.endsWith(".")))
                urlsIndex.add(i);
        }
        return urlsIndex;
    }

    public static String getColorFromString(String word) {
        String color = "";
        for (int i = 0; i < word.length(); i++) {
            char ch = word.charAt(i);
            if (Character.toString(ch).equals("&") && i != word.length() - 1)
                color = color + "&" + word.charAt(i + 1);
        }
        return color;
    }
}
