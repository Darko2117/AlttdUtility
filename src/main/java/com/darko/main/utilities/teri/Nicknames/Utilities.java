package com.darko.main.utilities.teri.Nicknames;

import java.awt.*;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class Utilities
{
    public static String stringRegen;
    private static final Pattern hexPattern;
    List<String> matchList;
    
    public Utilities() {
        this.matchList = new ArrayList<>();
    }
    
    public static String applyColor(String message) {
        ChatColor hexColor1 = null;
        ChatColor hexColor2 = null;
        StringBuilder stringBuilder = new StringBuilder();
        message = ChatColor.translateAlternateColorCodes('&', message);
        boolean startsWithColor = false;
        boolean lastColorMatters = false;

        if (message.matches(".*" + Utilities.stringRegen + ".*")) {
            String[] split = message.split(Utilities.stringRegen);
            for (String s : split){
                System.out.println(s);
            }

            ArrayList<String> list = new ArrayList<>();
            int nextIndex = 0;
            if (message.indexOf("}") <= 9) {
                startsWithColor = true;
                list.add(message.substring(0, message.indexOf("}")));
            }
            for (String s : split) {
                nextIndex += s.length();
                int tmp = message.indexOf("}", nextIndex);
                if (tmp < message.length() && tmp>=0) {
                    list.add(message.substring(nextIndex, tmp + 1));
                    nextIndex = tmp + 1;
                }
            }

            int i;
            boolean firstLoop = true;
            if (startsWithColor) {
                i = -1;
            } else {
                i = 0;
                stringBuilder.append(split[i]);
            }

            for (String s : list) {
                boolean lesser = s.contains("<");
                boolean bigger = s.contains(">");

                if (bigger && lesser) {
                    hexColor2 = ChatColor.of(s.substring(1, s.length() - 3));
                } else if (bigger || lesser) {
                    hexColor2 = ChatColor.of(s.substring(1, s.length() - 2));
                } else {
                    hexColor2 = ChatColor.of(s.substring(1, s.length() -1));
                }

                if (firstLoop) {
                    lastColorMatters = bigger;
                    hexColor1 = hexColor2;
                    firstLoop = false;
                    i++;
                    continue;
                }

                if (lesser && lastColorMatters) {
                    stringBuilder.append(hexGradient(hexColor1.getColor(), hexColor2.getColor(), split[i]));
                } else {
                    stringBuilder.append(hexColor1).append(split[i]);
                }

                hexColor1 = hexColor2;
                lastColorMatters = bigger;
                i++;
            }
            if (split.length > i){
                stringBuilder.append(hexColor1).append(split[i]);
            }
        }
        return stringBuilder.length()==0 ? message : stringBuilder.toString();
    }
    
    public static String removeHexColors(final String message) {
        return message.replaceAll("[\\{#A-Fa-f0-9}<>]{9,11}", "");
    }
    
    static {
        Utilities.stringRegen = "\\{#([A-Fa-f0-9]{6}|[A-Fa-f0-9]{3})(<)?(>)?}";
        hexPattern = Pattern.compile(Utilities.stringRegen);
    }

    public static String hexGradient(Color color1, Color color2, String text){
        double r = color1.getRed();
        double g = color1.getGreen();
        double b = color1.getBlue();

        double rDifference = (color1.getRed() - color2.getRed()) / ((double) text.length() - 1);
        double gDifference = (color1.getGreen() - color2.getGreen()) / ((double) text.length() - 1);
        double bDifference = (color1.getBlue() - color2.getBlue()) / ((double) text.length() - 1);

        StringBuilder stringBuilder = new StringBuilder();
        char[] chars = text.toCharArray();
        for (int i = 0; i < text.length(); i++) {
            if (i > 0) {
                r = r - rDifference;
                g = g - gDifference;
                b = b - bDifference;
            }
            stringBuilder.append(ChatColor.of(new Color((int) r, (int) g, (int) b))).append(chars[i]);
        }

        return stringBuilder.toString();
    }

    public static void updateCache() {
        if (!Nicknames.getInstance().nickCacheUpdate.isEmpty()){
            Nicknames.getInstance().nickCacheUpdate.forEach(uuid ->{
                Nicknames.getInstance().NickCache.remove(uuid);
                if (Bukkit.getOnlinePlayers().contains(Bukkit.getPlayer(uuid))) {
                    Nicknames.getInstance().NickCache.put(uuid, DatabaseQueries.getNick(uuid));
                }
            });
            DatabaseQueries.getNicknamesList().forEach(nick -> Nicknames.getInstance().NickCache.put(nick.getUuid(), nick));
        }
    }
}
