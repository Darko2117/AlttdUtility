package com.darko.main.teri.Nicknames;

import com.Zrips.CMI.CMI;
import com.Zrips.CMI.Containers.CMIUser;
import com.darko.main.common.API.APIs;
import com.darko.main.AlttdUtility;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import net.luckperms.api.LuckPerms;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.Date;

public class Nicknames implements CommandExecutor, TabCompleter {

    static Nicknames instance;
    HashMap<UUID, Nick> NickCache;
    ArrayList<UUID> nickCacheUpdate;

    public Nicknames() {
        instance = this;
        NickCache = new HashMap<>();
        nickCacheUpdate = new ArrayList<>();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (!AlttdUtility.getInstance().getConfig().getBoolean("FeatureToggles.Nicknames")) return true;

        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (args.length == 0) {
                sender.sendMessage(format(helpMessage(sender, HelpType.ALL)));
                return true;
            }
            switch (args[0].toLowerCase()){
                case "set":
                    if (args.length == 2 && hasPermission(sender, "utility.nick.set")){
                        handleNick(player, player, args[1]);
                    } else if (args.length == 3 && hasPermission(sender, "utility.nick.set.others")){
                        OfflinePlayer offlinePlayer = sender.getServer().getOfflinePlayer(args[1]);

                        if (offlinePlayer.isOnline() || offlinePlayer.hasPlayedBefore()){
                            handleNick(player, offlinePlayer, args[2]);
                        } else {
                            sender.sendMessage(format(helpMessage(sender, HelpType.SET_OTHERS)));
                        }
                    } else if (args.length > 3){
                        sender.sendMessage(format(helpMessage(sender, HelpType.SET_SELF, HelpType.SET_OTHERS)));
                    }
                    break;
                case "review":
                    if (args.length == 1 && hasPermission(sender, "utility.nick.review")){
                        NicknamesGui nicknamesGui = new NicknamesGui();
                        AlttdUtility.getInstance().getServer().getPluginManager().registerEvents(nicknamesGui, AlttdUtility.getInstance());
                        nicknamesGui.openInventory(player);
                    } else {
                        sender.sendMessage(format(helpMessage(sender, HelpType.REVIEW)));
                    }
                    break;
                case "request":
                    if (args.length == 2 && hasPermission(sender, "utility.nick.request")){
                        new BukkitRunnable() {
                            @Override
                            public void run() {
                                handleNickRequest(player, args[1]);
                            }
                        }.runTaskAsynchronously(AlttdUtility.getInstance());
                    } else {
                        sender.sendMessage(format(helpMessage(sender, HelpType.REQUEST)));
                    }
                    break;
                case "try":
                    if (args.length == 2 && hasPermission(sender, "utility.nick.try")){
                        LuckPerms api = APIs.LuckPermsApiCheck();
                        if (api != null){
                            if (Utilities.validNick(player, player, args[1])) {
                                sender.sendMessage(format(AlttdUtility.getInstance().getConfig().getString("Messages.NickTryout")
                                        .replace("%prefix", api.getUserManager().getUser(player.getUniqueId())
                                                .getCachedData().getMetaData().getPrefix())
                                        .replace("%nick%", args[1])));
                            }
                        } else {
                            sender.sendMessage(format(AlttdUtility.getInstance().getConfig().getString("Messages.NickNoLuckPerms")));
                        }
                    } else {
                        sender.sendMessage(format(helpMessage(sender, HelpType.TRY)));
                    }
                    break;
                case "help":
                    sender.sendMessage(format(helpMessage(sender, HelpType.ALL)
                            + "For more info on nicknames and how to use rgb colors go to: &bhttps://alttd.com/nicknames&f"));
                    break;
                default:
                    sender.sendMessage(format(helpMessage(sender, HelpType.ALL)));
            }
        } else {
            sender.sendMessage("Console commands are disabled.");
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {

        if (!AlttdUtility.getInstance().getConfig().getBoolean("FeatureToggles.Nicknames")) return null;

        List<String> completions = new ArrayList<>();
        if (!sender.hasPermission("utility.nick")) return completions;

        if (args.length == 1){
            List<String> choices = new ArrayList<>();
            if (sender.hasPermission("utility.nick.set")) {
                choices.add("set");
            }
            if (sender.hasPermission("utility.nick.review")) {
                choices.add("review");
            }
            if (sender.hasPermission("utility.nick.request")) {
                choices.add("request");
            }
            if (sender.hasPermission("utility.nick.try")) {
                choices.add("try");
            }
            choices.add("help");

            for (String s : choices) {
                if (s.startsWith(args[0])) {
                    completions.add(s);
                }
            }
        } else if (args.length == 2) {
            if (args[0].equalsIgnoreCase("set")) {
                List<String> choices = new ArrayList<>();
                List<String> onlinePlayers = new ArrayList<>();
                Bukkit.getOnlinePlayers().forEach(a -> onlinePlayers.add(a.getName()));

                if (sender.hasPermission("utility.nick.set.others")) {
                    choices.addAll(onlinePlayers);
                }

                for (String s : choices) {
                    if (s.startsWith(args[1])) {
                        completions.add(s);
                    }
                }
            }
        }
        return completions;
    }

    private void handleNickRequest(Player player, String nickName) {
        if (!Utilities.validNick(player, player, nickName)){
            return;
        }

        Utilities.updateCache();
        UUID uniqueId = player.getUniqueId();

        if (NickCache.containsKey(uniqueId)){
            Nick nick = NickCache.get(uniqueId);
            long timeSinceLastChange =  new Date().getTime() - nick.getLastChangedDate();
            long waitTime = AlttdUtility.getInstance().getConfig().getLong("Nicknames.WaitTime");
            if (timeSinceLastChange > waitTime){
                if (nick.hasRequest()){
                    player.sendMessage(format(AlttdUtility.getInstance().getConfig().getString("Messages.NickRequestReplaced")
                            .replace("%oldRequestedNick%", nick.getNewNick())
                            .replace("%newRequestedNick%", nickName)));
                }
                nick.setNewNick(nickName);
                nick.setRequestedDate(new Date().getTime());
            } else {
                player.sendMessage(format(AlttdUtility.getInstance().getConfig().getString("Messages.NickTooSoon")
                        .replace("%time%", formatTime((timeSinceLastChange-waitTime)*-1))));
                return;
            }
        } else {
            NickCache.put(uniqueId, new Nick(uniqueId, null, 0, nickName, new Date().getTime()));
        }
        DatabaseQueries.newNicknameRequest(uniqueId, nickName);
        bungeeMessageRequest(player);
        player.sendMessage(format(AlttdUtility.getInstance().getConfig().getString("Messages.NickRequested")
                .replace("%nick%", nickName)));
    }

    private void bungeeMessageRequest(Player player) {
        ByteArrayDataOutput out = ByteStreams.newDataOutput();

        UUID uniqueId = player.getUniqueId();

        out.writeUTF("Forward"); // So BungeeCord knows to forward it
        out.writeUTF("ALL");
        out.writeUTF("NickNameRequest"); // The channel name to check if this your data

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

        player.sendPluginMessage(AlttdUtility.getInstance(), "BungeeCord", out.toByteArray());

        String notification = Utilities.applyColor(AlttdUtility.getInstance().getConfig().getString("Messages.NickNewRequest")
                .replace("%player%", player.getName()));

        TextComponent component = new TextComponent(TextComponent.fromLegacyText(Utilities.applyColor(notification)));
        component.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/nick review"));
        component.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
                new ComponentBuilder(Utilities.applyColor("&6Click this text to review the request!")).create()));

        AlttdUtility.getInstance().getServer().getOnlinePlayers().forEach(p ->{
            if (p.hasPermission("utility.nick.review")){
                p.sendMessage(component);
            }
        });
        Nicknames.getInstance().nickCacheUpdate.add(uniqueId);
    }

    private String formatTime(long timeInMillis){
        long second = (timeInMillis / 1000) % 60;
        long minute = (timeInMillis / (1000 * 60)) % 60;
        long hour = (timeInMillis / (1000 * 60 * 60)) % 24;
        long days = (timeInMillis / (1000 * 60 * 60 * 24));

        StringBuilder stringBuilder = new StringBuilder();
        if (days!=0){
            stringBuilder.append(days).append(" days ");
        }
        if (days!=0 || hour!=0){
            stringBuilder.append(hour).append(" hours ");
        }
        if (days!=0 || hour!=0 || minute != 0){
            stringBuilder.append(minute).append(" minutes and ");
        }
        stringBuilder.append(second).append(" seconds");
        return stringBuilder.toString();
    }

    private void handleNick(Player sender, OfflinePlayer target, final String nickName) {
        if (nickName.equalsIgnoreCase("off")) {

            try {
                if (target.isOnline()){
                    resetNick(target.getPlayer());
                }
                DatabaseQueries.removePlayerFromDataBase(target.getUniqueId());
                NickCache.remove(target.getUniqueId());
                nickCacheUpdate.add(target.getUniqueId());
            } catch (SQLException e) {
                e.printStackTrace();
            }

            if (!sender.equals(target)){
                sender.sendMessage(format(AlttdUtility.getInstance().getConfig().getString("Messages.NickResetOthers")
                        .replace("%player%", target.getName())));
            }

            if (target.isOnline()){
                target.getPlayer().sendMessage(format(AlttdUtility.getInstance().getConfig().getString("Messages.NickReset")));
            }

            NickEvent nickEvent = new NickEvent(sender.getName(), target.getName(), null, NickEvent.NickEventType.RESET);
            nickEvent.callEvent();

        } else if (Utilities.validNick(sender, target, nickName)) {
            if (target.isOnline()) {
                setNick(target.getPlayer(), nickName);
            } else {
                Utilities.bungeeMessageHandled(target.getUniqueId(), sender, "Set");
            }

            DatabaseQueries.setNicknameInDatabase(target.getUniqueId(), nickName);
            NickEvent nickEvent = new NickEvent(sender.getName(), target.getName(), nickName, NickEvent.NickEventType.SET);
            nickEvent.callEvent();

            if (NickCache.containsKey(target.getUniqueId())){
                Nick nick = NickCache.get(target.getUniqueId());
                nick.setCurrentNick(nickName);
                nick.setLastChangedDate(new Date().getTime());
            } else {
                NickCache.put(target.getUniqueId(), new Nick(target.getUniqueId(), nickName, new Date().getTime()));
            }

            if (!sender.equals(target)){
                sender.sendMessage(format(AlttdUtility.getInstance().getConfig().getString("Messages.NickChangedOthers")
                        .replace("%targetplayer%", target.getName())
                        .replace("%nickname%", nickName)));
                if (target.isOnline()) {
                    target.getPlayer().sendMessage(format(AlttdUtility.getInstance().getConfig().getString("Messages.NickTargetNickChange")
                            .replace("%nickname%", getNick(target.getPlayer()))
                            .replace("%sendernick%", getNick(sender))
                            .replace("%player%", target.getName())));
                }
            } else if (target.isOnline()){
                target.getPlayer().sendMessage(format(AlttdUtility.getInstance().getConfig().getString("Messages.NickChanged")
                        .replace("%nickname%", getNick(target.getPlayer()))));
            }
        }
    }

    private String helpMessage(final CommandSender sender, final HelpType... helpTypes) {
        StringBuilder message = new StringBuilder();
        for (HelpType helpType : helpTypes){
            if (helpType.equals(HelpType.ALL)){
                return helpMessage(sender, helpType);
            }
            message.append(helpMessage(sender, helpType));
        }
        return message.toString();
    }

    private String helpMessage(CommandSender sender, HelpType type) {
        StringBuilder message = new StringBuilder();
        switch (type){
            case ALL:
                message.append(helpMessage(sender, HelpType.SET_SELF));
                message.append(helpMessage(sender, HelpType.SET_OTHERS));
                message.append(helpMessage(sender, HelpType.REQUEST));
                message.append(helpMessage(sender, HelpType.REVIEW));
                message.append(helpMessage(sender, HelpType.TRY));
                break;
            case SET_SELF:
                if (sender.hasPermission("utility.nick.set")){
                    message.append("&6/nick set <nickname>&f - Sets your nickname to the specified name.\n");
                }
                break;
            case SET_OTHERS:
                if (sender.hasPermission("utility.nick.set.others")){
                    message.append("&6/nick set <username> <nickname>&f - Sets the specified user's nickname to the specified name.\n");
                }
                break;
            case REQUEST:
                if (sender.hasPermission("utility.nick.request")){
                    message.append("&6/nick request <nickname>&f - Requests a username to be reviewed by staff.\n" +
                            "   &7Try using &8/nick try <nickname>&7 to see if you like the name, you can only change it once per day!\n");
                }
                break;
            case REVIEW:
                if (sender.hasPermission("utility.nick.review")){
                    message.append("&6/nick review&f - Opens the nickname review GUI (left click to accept a nick, right click to deny it)\n");
                }
                break;
            case TRY:
                if (sender.hasPermission("utility.nick.try")){
                    message.append("&6/nick try <nickname>&f - Shows you what your nickname will look like in chat.\n");
                }
        }
        return message.toString();
    }

    private boolean hasPermission(CommandSender sender, String permission) {
        if (!sender.hasPermission(permission)){
            sender.sendMessage(format(AlttdUtility.getInstance().getConfig().getString("Messages.NoPermission")));
            return false;
        }
        return true;
    }

    public void resetNick(final Player player){
        final CMIUser user = CMI.getInstance().getPlayerManager().getUser(player);
        user.setNickName(null, true);
        user.updateDisplayName();
    }

    public String getNick(final Player player) {
        final CMIUser user = CMI.getInstance().getPlayerManager().getUser(player);
        return user.getNickName();
    }

    public void setNick(final Player player, final String nickName) {
        final CMIUser user = CMI.getInstance().getPlayerManager().getUser(player);
        if (nickName == null){
            user.setNickName(null, true);
        } else {
            user.setNickName(Utilities.applyColor(nickName), true);
        }
        user.updateDisplayName();
    }

    @Deprecated
    public void setNick(final CommandSender sender, final Player player, final String nickName) {
        Bukkit.getServer().dispatchCommand(sender, "cmi nick " + nickName + " " + player.getName());
    }

    public static String format(final String m) {
        return Utilities.applyColor(m);
    }

    public static Nicknames getInstance() {
        return Nicknames.instance;
    }

    private enum HelpType {
        ALL,
        SET_SELF,
        SET_OTHERS,
        REVIEW,
        REQUEST,
        TRY
    }
}
