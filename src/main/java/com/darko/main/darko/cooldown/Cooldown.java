package com.darko.main.darko.cooldown;

import com.darko.main.common.API.APIs;
import com.darko.main.AlttdUtility;
import com.darko.main.common.Methods;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.model.user.User;
import net.luckperms.api.node.Node;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Cooldown implements CommandExecutor, TabCompleter {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (!AlttdUtility.getInstance().getConfig().getBoolean("FeatureToggles.CooldownCommand")) return true;

        if (!(sender instanceof Player)) {
            new Methods().sendConfigMessage(sender, "Messages.PlayerOnlyCommandMessage");
            return true;
        }

        Player player = (Player) sender;

        if (args.length == 0)
            for (String permissionName : getCooldownCommandInfo().values()) {
                sendCooldownMessage(player, permissionName);
            }
        else sendCooldownMessage(player, args[0]);

        return true;

    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {

        if (!AlttdUtility.getInstance().getConfig().getBoolean("FeatureToggles.CooldownCommand")) return null;

        if (args.length == 1) {

            List<String> permissions = new ArrayList<>();
            for (Map.Entry<String, String> entry : getCooldownCommandInfo().entrySet())
                permissions.add(entry.getValue());

            List<String> completions = new ArrayList<>();

            for (String string : permissions) {
                if (string.startsWith(args[0])) {
                    completions.add(string);
                }
            }

            return completions;

        }

        return null;

    }

    void sendCooldownMessage(Player player, String permissionName) {

        String permission = null;

        for (Map.Entry<String, String> entry : getCooldownCommandInfo().entrySet()) {
            if (entry.getValue().equalsIgnoreCase(permissionName)) {
                permission = entry.getKey();
                break;
            }
        }

        if (permission == null) {
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&eYou have no cooldown on " + permissionName));
            return;
        }

        LuckPerms api = APIs.getLuckPermsAPI();
        User user = api.getUserManager().getUser(player.getUniqueId());

        Integer seconds = null;

        for (Node node : user.getNodes()) {
            if (node.getKey().equals(permission)) {
                seconds = Integer.parseInt(String.valueOf(node.getExpiry().getEpochSecond() - System.currentTimeMillis() / 1000L));
            }
        }

        if (seconds == null || seconds <= 0) {
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&eYou have no cooldown on " + permissionName));
            return;
        }

        String timeString = new Methods().getTimeStringFromIntSeconds(seconds);

        player.sendMessage(ChatColor.GREEN + "Your cooldown on " + permissionName + " is " + timeString + ".");

    }

    HashMap<String, String> getCooldownCommandInfo() {

        HashMap<String, String> resultsHashMap = new HashMap<>();

        List<String> resultsList = AlttdUtility.getInstance().getConfig().getStringList("CooldownCommandPermissions");
        if (resultsList == null) return resultsHashMap;

        for (String string : resultsList) {

            String permission, name;

            StringBuilder reader = new StringBuilder(string);
            reader.delete(0, reader.indexOf("Permission:") + 11);
            reader.delete(reader.indexOf(" "), reader.length());
            permission = reader.toString();

            reader = new StringBuilder(string);
            reader.delete(0, reader.indexOf("Name:") + 5);
            name = reader.toString();

            resultsHashMap.put(permission, name);

        }

        return resultsHashMap;

    }

}
