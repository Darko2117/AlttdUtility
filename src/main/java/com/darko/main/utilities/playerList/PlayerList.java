package com.darko.main.utilities.playerList;

import com.darko.main.AlttdUtility;
import com.darko.main.API.APIs;
import com.darko.main.other.Methods;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.model.user.User;
import net.luckperms.api.node.NodeType;
import net.luckperms.api.node.types.InheritanceNode;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.stream.Collectors;

public class PlayerList implements CommandExecutor, TabCompleter {

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if(!AlttdUtility.getInstance().getConfig().getBoolean("FeatureToggles.PlayerListCommand")) return true;

        if (args.length == 0) {

            StringBuilder message = new StringBuilder();

            message.append(ChatColor.GRAY + "==============================\n");

            Integer currentPlayers = Bukkit.getServer().getOnlinePlayers().size();
            Integer maxPlayers = Bukkit.getServer().getMaxPlayers();

            StringBuilder playerCount = new StringBuilder();

            ChatColor playerCountColor;

            if (currentPlayers.floatValue() <= maxPlayers.floatValue() / 100.0f * 75.0f) {
                playerCountColor = ChatColor.GREEN;
            } else if (currentPlayers.floatValue() > maxPlayers.floatValue() / 100.0f * 75.0f && currentPlayers < maxPlayers) {
                playerCountColor = ChatColor.GOLD;
            } else {
                playerCountColor = ChatColor.RED;
            }
            playerCount.append(playerCountColor + "" + currentPlayers + ChatColor.YELLOW + "/" + playerCountColor + maxPlayers);

            message.append(ChatColor.YELLOW + "" + ChatColor.BOLD + "Online players: " + playerCount + "\n");


            HashMap<String, List<String>> groupSectionsWithListsOfGroups = new LinkedHashMap<>();

            for (String groupSection : AlttdUtility.getInstance().getConfig().getKeys(true)) {

                StringBuilder groupSectionString = new StringBuilder(groupSection);

                if (groupSection.startsWith("ListGroups")) {

                    groupSectionString.delete(0, 11);
                    if (groupSectionString.length() != 0) {
                        groupSectionsWithListsOfGroups.put(groupSectionString.toString(), AlttdUtility.getInstance().getConfig().getStringList("ListGroups." + groupSectionString));
                    }

                }

            }

            LuckPerms api = APIs.LuckPermsApiCheck();

            for (Map.Entry<String, List<String>> entry : groupSectionsWithListsOfGroups.entrySet()) {

                List<String> groups = entry.getValue();

                List<String> players = new ArrayList<>();

                for (Player player : Bukkit.getServer().getOnlinePlayers()) {

                    User user = api.getUserManager().getUser(player.getUniqueId());
                    if (groups.contains(user.getPrimaryGroup())) {
                        players.add(player.getDisplayName());
                    }

                }

                if (!players.isEmpty()) {

                    message.append(ChatColor.YELLOW + "" + entry.getKey() + ": " + ChatColor.RESET + "" + ChatColor.YELLOW);

                    for (String player : players) {
                        message.append(ChatColor.RESET + player + ChatColor.RESET + ", ");
                    }

                    message.delete(message.length() - 2, message.length());
                    message.append("\n");

                }

            }

            message.append(ChatColor.GRAY + "==============================\n");
            sender.sendMessage(message.toString());

        } else {

            if (sender.hasPermission("utility.list.specific")) {

                String group = args[0];

                LuckPerms api = APIs.LuckPermsApiCheck();

                StringBuilder message = new StringBuilder();

                for (Player players : Bukkit.getOnlinePlayers()) {

                    User user = api.getUserManager().getUser(players.getUniqueId());

                    Set<String> userGroups = user.getNodes().stream().filter(NodeType.INHERITANCE::matches)
                            .map(NodeType.INHERITANCE::cast).map(InheritanceNode::getGroupName)
                            .collect(Collectors.toSet());

                    if (userGroups.contains(group)) {
                        message.append(players.getName() + ", ");
                    }

                }

                if (message.length() != 0) {

                    message.delete(message.length() - 2, message.length());
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', AlttdUtility.getInstance().getConfig()
                            .getString("Messages.ListGroup").replace("%group%", group)));
                    sender.sendMessage(message.toString());

                } else {

                    new Methods().sendConfigMessage(sender, "Messages.GroupNotFound");

                }

            } else {

                new Methods().sendConfigMessage(sender, "Messages.NoPermission");

            }

        }

        return true;

    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {

        if (args.length == 1) {

            if(sender.hasPermission("utility.list.specific")){

                List<String> completions = new ArrayList<>();

                for (String string : OnlineGroups()) {

                    if (string.startsWith(args[0])) {
                        completions.add(string);
                    }

                }

                return completions;

            }

        }

        return null;

    }

    private static Set<String> OnlineGroups() {

        Set<String> groups = new HashSet<>();
        LuckPerms api = APIs.LuckPermsApiCheck();

        for (Player player : Bukkit.getOnlinePlayers()) {

            User user = api.getUserManager().getUser(player.getUniqueId());
            Set<String> groupsTemp = user.getNodes().stream().filter(NodeType.INHERITANCE::matches)
                    .map(NodeType.INHERITANCE::cast).map(InheritanceNode::getGroupName).collect(Collectors.toSet());
            groups.addAll(groupsTemp);

        }

        return groups;

    }

}