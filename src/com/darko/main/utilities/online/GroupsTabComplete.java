package com.darko.main.utilities.online;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import com.darko.main.Main;
import com.darko.main.utilities.other.APIs;

import net.luckperms.api.LuckPerms;
import net.luckperms.api.model.user.User;
import net.luckperms.api.node.NodeType;
import net.luckperms.api.node.types.InheritanceNode;

public class GroupsTabComplete implements TabCompleter {

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (args.length == 1) {
            List<String> completions = new ArrayList<>();
            for (String string : OnlineGroups()) {
                if (string.startsWith(args[0])) {
                    completions.add(string);
                }
            }
            return completions;
        }
        return null;
    }

    private static Set<String> OnlineGroups() {

        Set<String> groups = new HashSet<>();
        LuckPerms api = APIs.LuckPermsApi();
        for (Player player : Bukkit.getOnlinePlayers()) {
            User user = api.getUserManager().getUser(player.getUniqueId());
            Set<String> groupsTemp = user.getNodes().stream().filter(NodeType.INHERITANCE::matches)
                    .map(NodeType.INHERITANCE::cast).map(InheritanceNode::getGroupName).collect(Collectors.toSet());
            for (String group : groupsTemp) {
                if ((!groups.contains(group)
                        && !Main.getInstance().getConfig().getStringList("BlackListedGroups").contains(group))
                        || (!groups.contains(group) && player.hasPermission("utility.online.seeblacklisted"))) {
                    groups.add(group);
                }
            }
        }
        return groups;
    }
}
