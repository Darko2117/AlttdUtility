package com.darko.main.utilities.teri.Nicknames;

import java.awt.*;

import com.darko.main.Main;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Utilities
{
    public static String stringRegen;
    static String[] blockedCodes = Main.getInstance().getConfig().getStringList("Nicknames.BlockedColorCodes").toArray(new String[0]);;
    static String[] allowedColorCodes = Main.getInstance().getConfig().getStringList("Nicknames.AllowedColorCodes").toArray(new String[0]);;
    
    public static String applyColor(String message) {
        ChatColor hexColor1 = null;
        ChatColor hexColor2;
        StringBuilder stringBuilder = new StringBuilder();
        message = ChatColor.translateAlternateColorCodes('&', message);
        boolean startsWithColor = false;
        boolean lastColorMatters = false;

        if (message.matches(".*" + Utilities.stringRegen + ".*")) {
            String[] split = message.split(Utilities.stringRegen);

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
    
    public static String removeAllColors(String string) {

        for (final String colorCodes : allowedColorCodes) {
            string = string.replace(colorCodes, "");
        }

        return string.replaceAll("\\{#[A-Fa-f0-9]{6}(<)?(>)?}", "");
    }

    static {
        Utilities.stringRegen = "\\{#([A-Fa-f0-9]{6}|[A-Fa-f0-9]{3})(<)?(>)?}";
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
                Nick nick = DatabaseQueries.getNick(uuid);
                if (nick == null){
                    Nicknames.getInstance().NickCache.remove(uuid);
                } else {
                    Nicknames.getInstance().NickCache.put(uuid, nick);
                }
            });
        }
    }

    public static boolean validNick(Player sender, OfflinePlayer target, String nickName) {
        if (noBlockedCodes(nickName)) {

            String cleanNick = Utilities.removeAllColors(nickName);

            if (cleanNick.length() >= 3 && cleanNick.length() <= 16) {

                if (cleanNick.matches("[a-zA-Z0-9_]*") && nickName.length()<=192) {//192 is if someone puts {#xxxxxx<>} in front of every letter

                    if (!cleanNick.equalsIgnoreCase(target.getName())){
                        for (Nick nick : Nicknames.getInstance().NickCache.values()){
                            if ((nick.getCurrentNickNoColor() != null && nick.getCurrentNickNoColor().equalsIgnoreCase(cleanNick))
                                    || (nick.getNewNickNoColor() != null && nick.getNewNickNoColor().equalsIgnoreCase(cleanNick))){
                                sender.sendMessage(applyColor(Main.getInstance().getConfig().getString("Messages.NickTaken")));
                                return false;
                            }
                        }
                    }

                    return true;

                } else {
                    sender.sendMessage(applyColor(Main.getInstance().getConfig().getString("Messages.NickInvalidCharacters")));
                }
            } else {
                sender.sendMessage(applyColor(Main.getInstance().getConfig().getString("Messages.NickLengthInvalid")));
            }
        } else {
            sender.sendMessage(applyColor(Main.getInstance().getConfig().getString("Messages.NickBlockedColorCodes")));
        }
        return false;
    }

    public static boolean noBlockedCodes(final String getNick) {
        for (final String blockedCodes : blockedCodes) {
            if (getNick.contains(blockedCodes)) {
                return false;
            }
        }
        return true;
    }

    public static void bungeeMessageHandled(UUID uniqueId, Player player, String channel) {
        ByteArrayDataOutput out = ByteStreams.newDataOutput();

        out.writeUTF("Forward"); // So BungeeCord knows to forward it
        out.writeUTF("ALL");
        out.writeUTF("NickName" + channel); // The channel name to check if this your data

        ByteArrayOutputStream msgbytes = new ByteArrayOutputStream();
        DataOutputStream msgout = new DataOutputStream(msgbytes);
        try {
            msgout.writeUTF(uniqueId.toString());
        } catch (IOException exception){
            exception.printStackTrace();
            return;
        }
        byte[] bytes = msgbytes.toByteArray();
        out.writeShort(bytes.length);
        out.write(bytes);

        player.sendPluginMessage(Main.getInstance(), "BungeeCord", out.toByteArray());
    }
}
