package com.darko.main.utilities.playerList;

import com.darko.main.Main;
import com.darko.main.API.APIs;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.model.user.User;
import net.luckperms.api.node.NodeType;
import net.luckperms.api.node.types.InheritanceNode;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.stream.Collectors;

public class PlayerList implements CommandExecutor {

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (args.length == 0) {
            if (sender.hasPermission("utility.list")) {

                FileConfiguration config = Main.getInstance().getConfig();
                StringBuilder message = new StringBuilder();

                message.append(ChatColor.GRAY + "==============================\n");

                Integer currentPlayers = Bukkit.getServer().getOnlinePlayers().size();
                Integer maxPlayers = Bukkit.getServer().getMaxPlayers();

                StringBuilder playerCount = new StringBuilder();

                ChatColor playerCountColor = null;

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

                for (String groupSection : config.getKeys(true)) {

                    StringBuilder groupSectionString = new StringBuilder(groupSection);

                    if (groupSection.startsWith("ListGroups")) {
                        groupSectionString.delete(0, 11);
                        if (groupSectionString.length() != 0) {
                            groupSectionsWithListsOfGroups.put(groupSectionString.toString(), config.getStringList("ListGroups." + groupSectionString));
                        }
                    }
                }

                Iterator it1 = groupSectionsWithListsOfGroups.entrySet().iterator();

                LuckPerms api = APIs.LuckPermsApiCheck();

                while (it1.hasNext()) {

                    Map.Entry pair = (Map.Entry) it1.next();

                    List<String> groups = (List<String>) pair.getValue();
                    List<String> players = new ArrayList<>();
                    for (Player player : Bukkit.getServer().getOnlinePlayers()) {

                        User user = api.getUserManager().getUser(player.getUniqueId());
                        if (groups.contains(user.getPrimaryGroup())) {
                            players.add(player.getDisplayName());
                        }
                    }

                    if (!players.isEmpty()) {

                        message.append(ChatColor.YELLOW + "" + pair.getKey() + ": " + ChatColor.RESET + "" + ChatColor.YELLOW);

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
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&',
                        Main.getInstance().getConfig().getString("Messages.NoPermission")));
            }
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
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Main.getInstance().getConfig()
                            .getString("Messages.ListGroup").replace("%group%", group)));
                    sender.sendMessage(message.toString());
                } else {
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&',
                            Main.getInstance().getConfig().getString("Messages.GroupNotFound")));
                }
            } else {
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&',
                        Main.getInstance().getConfig().getString("Messages.NoPermission")));
            }
        }

        return false;
    }
}