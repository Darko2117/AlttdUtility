package com.darko.main.utilities.teri.Nicknames;

import com.Zrips.CMI.Containers.CMIUser;
import com.Zrips.CMI.CMI;
import com.Zrips.CMI.utils.Util;
import com.darko.main.Main;
import com.darko.main.register.Register;
import org.bukkit.Bukkit;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.CommandExecutor;
import org.bukkit.inventory.InventoryHolder;

import java.sql.SQLException;

public class Nicknames implements CommandExecutor {

    public DatabaseQueries database;
    String[] blockedCodes;
    String[] allowedColorCodes;
    static Nicknames instance;

    public Nicknames() {
        instance = this;
        blockedCodes = new String[] { "&k", "&l", "&n", "&m", "&o" }; //TODO put in config
        allowedColorCodes = new String[] { "&0", "&1", "&2", "&3", "&4", "&5", "&6", "&7", "&8", "&9", "&a", "&b", "&c", "&d", "&e", "&f", "&r" };
    }

    public boolean onCommand(final CommandSender sender, final Command cmd, final String label, final String[] args) {
        if (sender instanceof Player) {
            final Player player = (Player)sender;
            if (player.hasPermission("nicknames.setnicknames")) {
                if (args.length == 0 || args.length > 2) {
                    player.sendMessage(ChatColor.YELLOW + "Usage: " + ChatColor.GOLD + "/nick " + ChatColor.WHITE + "(playername) [nickname/off]");
                }
                if (args.length == 1) {
                    final String getNick = args[0];
                    if (getNick.equalsIgnoreCase("off")) {
                        try {
                            resetNick(player);
                            DatabaseQueries.removePlayerFromDataBase(player.getUniqueId());
                        }
                        catch (SQLException e) {
                            e.printStackTrace();
                        }
                        player.sendMessage(format(Main.getInstance().getConfig().getString("Messages.NickReset")));
                    }
                    else if (noBlockedCodes(getNick)) {
                        String nickLength = Utilities.removeHexColors(getNick);
                        for (final String colorCodes : allowedColorCodes) {
                            nickLength = nickLength.replace(colorCodes, "");
                        }
                        if (nickLength.length() >= 3 && nickLength.length() <= 16) {
                            if (getNick.matches("[a-zA-Z0-9 &_{}<>#]*")) {
                                setNick(player, getNick);
                                DatabaseQueries.setNicknameInDatabase(player.getUniqueId(), Util.CMIChatColor.deColorize(getNick(player)));
                                player.sendMessage(format(Main.getInstance().getConfig().getString("Messages.NickChanged")
                                        .replace("%nickname%", getNick(player))));
                            }
                            else {
                                player.sendMessage(format(Main.getInstance().getConfig().getString("Messages.NickInvalidCharacters")));
                            }
                        }
                        else {
                            player.sendMessage(format(Main.getInstance().getConfig().getString("Messages.NickLengthInvalid")));
                        }
                    }
                    else {
                        player.sendMessage(format(Main.getInstance().getConfig().getString("Messages.NickBlockedColorCodes")));
                    }
                }
                if (args.length == 2) {
                    if (player.hasPermission("nicknames.setnicknames.others")) {
                        final Player target = Bukkit.getServer().getPlayer(args[0]);
                        if (target != null && target.isOnline()) {
                            final String getNick2 = args[1];
                            if (getNick2.equalsIgnoreCase("off")) {
                                try {
                                    resetNick(target);
                                    DatabaseQueries.removePlayerFromDataBase(player.getUniqueId());
                                }
                                catch (SQLException e2) {
                                    e2.printStackTrace();
                                }
                                player.sendMessage(format(Main.getInstance().getConfig().getString("Messages.NickResetOthers")
                                        .replace("%player%", target.getName())));
                            }
                            else if (noBlockedCodes(getNick2)) {
                                String nickLength2 = Utilities.removeHexColors(getNick2);
                                for (final String colorCodes2 : allowedColorCodes) {
                                    nickLength2 = nickLength2.replace(colorCodes2, "");
                                }
                                if (nickLength2.length() >= 3 && nickLength2.length() <= 16) {
                                    if (getNick2.matches("[a-zA-Z0-9 &_{}#<>]*")) {
                                        setNick(target, getNick2);
                                        DatabaseQueries.setNicknameInDatabase(target.getUniqueId(), Util.CMIChatColor.deColorize(getNick(target)));
                                        player.sendMessage(format(Main.getInstance().getConfig().getString("Messages.NickChangedOthers")
                                                .replace("%targetplayer%", target.getName())
                                                .replace("%nickname%", getNick(target))));
                                        target.sendMessage(format(Main.getInstance().getConfig().getString("Messages.NickTargetNickChange")
                                                .replace("%nickname%", getNick(target))
                                                .replace("%sendernick%", getNick(player))
                                                .replace("%player%", player.getName())));
                                    }
                                    else {
                                        player.sendMessage(format(Main.getInstance().getConfig().getString("Messages.NickInvalidCharacters")));
                                    }
                                }
                                else {
                                    player.sendMessage(format(Main.getInstance().getConfig().getString("Messages.NickLengthInvalid")));
                                }
                            }
                            else {
                                player.sendMessage(format(Main.getInstance().getConfig().getString("Messages.NickBlockedColorCodes")));
                            }
                        }
                        else {
                            player.sendMessage(format(Main.getInstance().getConfig().getString("Messages.NickPlayerNotOnline")));
                        }
                    }
                    else {
                        player.sendMessage(format(Main.getInstance().getConfig().getString("Messages.NoPermission")));
                    }
                }
            }
            else {
                player.sendMessage(format(Main.getInstance().getConfig().getString("Messages.NoPermission")));
            }
            if (false){
                NicknamesGui nicknamesGui = new NicknamesGui(sender.getName());
                Main.getInstance().getServer().getPluginManager().registerEvents(nicknamesGui, Main.getInstance());
                nicknamesGui.openInventory(sender.getServer().getPlayer(sender.getName()));
            }
        }
        else {
            sender.sendMessage("Console commands are disabled.");
        }
        return true;
    }

//    public void resetNick(final Player player) {
//        Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "cmi nick off " + player.getName());
//    }

    public void resetNick(final Player player){//TODO test this
        final CMIUser user = CMI.getInstance().getPlayerManager().getUser(player);
        user.setNickName(null, true);
    }

    public String getNick(final Player player) {
        final CMIUser user = CMI.getInstance().getPlayerManager().getUser(player);
        return user.getNickName();
    }

    public void setNick(final Player player, final String nickName) {
        final CMIUser user = CMI.getInstance().getPlayerManager().getUser(player);
        user.setNickName(Util.CMIChatColor.translateAlternateColorCodes(nickName), true);
        user.updateDisplayName();
    }

    @Deprecated
    public void setNick(final CommandSender sender, final Player player, final String nickName) {
        Bukkit.getServer().dispatchCommand(sender, "cmi nick " + nickName + " " + player.getName());
    }

    public static String format(final String m) {
        return Utilities.applyColor(m);
    }

    public boolean noBlockedCodes(final String getNick) {
        for (final String blockedCodes : blockedCodes) {
            if (getNick.contains(blockedCodes)) {
                return false;
            }
        }
        return true;
    }

    public static Nicknames getInstance() {
        return Nicknames.instance;
    }
}
