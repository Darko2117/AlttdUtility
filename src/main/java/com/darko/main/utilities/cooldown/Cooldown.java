package com.darko.main.utilities.cooldown;

import com.darko.main.API.APIs;
import com.darko.main.Main;
import com.darko.main.other.Methods;
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

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if(!Main.getInstance().getConfig().getBoolean("FeatureToggles.CooldownCommand")) return true;

        if (!(sender instanceof Player)) {
            Methods.sendConfigMessage(sender, "Messages.PlayerOnlyCommandMessage");
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

        if(!Main.getInstance().getConfig().getBoolean("FeatureToggles.CooldownCommand")) return null;

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
            if (entry.getValue().equals(permissionName)) {
                permission = entry.getKey();
                break;
            }
        }

        if (permission == null) {
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&eYou have no cooldown on " + permissionName));
            return;
        }

        LuckPerms api = APIs.LuckPermsApiCheck();
        User user = api.getUserManager().getUser(player.getUniqueId());

        Integer hours = 0, minutes = 0, seconds = 0;

        for (Node node : user.getNodes()) {
            if (node.getKey().equals(permission)) {
                seconds = Integer.parseInt(String.valueOf(node.getExpiry().getEpochSecond() - System.currentTimeMillis() / 1000L));
            }
        }

        while (seconds >= 60) {
            minutes++;
            seconds -= 60;
        }
        while (minutes >= 60) {
            hours++;
            minutes -= 60;
        }

        Boolean displayHours = hours > 0;
        Boolean displayMinutes = minutes > 0;
        Boolean displaySeconds = seconds > 0;

        if (!displaySeconds && !displayMinutes && !displayHours) {
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&eYou have no cooldown on " + permissionName));
            return;
        }

        StringBuilder message = new StringBuilder(ChatColor.GREEN + "Your cooldown on " + permissionName + " is ");
        if (displayHours) message.append(hours).append(" hours ");
        if (displayMinutes) message.append(minutes).append(" minutes ");
        message.append(seconds).append(" seconds").append(".");

        player.sendMessage(message.toString());

    }

    HashMap<String, String> getCooldownCommandInfo() {

        HashMap<String, String> resultsHashMap = new HashMap<>();

        List<String> resultsList = Main.getInstance().getConfig().getStringList("CooldownCommandPermissions");
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
