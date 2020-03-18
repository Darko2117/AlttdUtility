package com.darko.main.utilities.online;

import java.util.Set;
import java.util.stream.Collectors;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.darko.main.Main;
import com.darko.main.utilities.other.APIs;

import net.luckperms.api.LuckPerms;
import net.luckperms.api.model.user.User;
import net.luckperms.api.node.NodeType;
import net.luckperms.api.node.types.InheritanceNode;
import net.md_5.bungee.api.ChatColor;

public class OnlineCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        Player player = (Player) sender;
        if (player.hasPermission("utility.online")) {
            try {
                LuckPerms api = APIs.LuckPermsApi();
                if (args.length == 1) {

                    String groupName = args[0].toString();
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', Main.getInstance().getConfig()
                            .getString("Messages.OnlineGroup").replace("%group%", groupName)));
                    if (!Main.getInstance().getConfig().getStringList("BlackListedGroups").contains(args[0].toString())
                            || player.hasPermission("utility.online.seeblacklisted")) {
                        StringBuilder names = new StringBuilder();
                        for (Player players : Bukkit.getOnlinePlayers()) {
                            User user = api.getUserManager().getUser(players.getUniqueId());
                            Set<String> userGroups = user.getNodes().stream().filter(NodeType.INHERITANCE::matches)
                                    .map(NodeType.INHERITANCE::cast).map(InheritanceNode::getGroupName)
                                    .collect(Collectors.toSet());
                            if (userGroups.contains(groupName)) {
                                names.append(players.getName() + ", ");
                            }
                        }
                        player.sendMessage(names.substring(0, names.length() - 2).toString());
                    } else {
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&',
                                Main.getInstance().getConfig().getString("Messages.GroupNotFound")));
                    }
                }
            } catch (Exception e) {
                player.sendMessage(ChatColor.translateAlternateColorCodes('&',
                        Main.getInstance().getConfig().getString("Messages.GroupNotFound")));
            }
        } else {
            player.sendMessage(ChatColor.translateAlternateColorCodes('&',
                    Main.getInstance().getConfig().getString("Messages.NoPermission")));
        }

        return false;
    }
}
