package com.darko.main.utilities.commandOnJoin;

import com.darko.main.Main;
import com.darko.main.other.Methods;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.ArrayList;
import java.util.List;

public class CommandOnJoin implements CommandExecutor, Listener, TabCompleter {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (!Main.getInstance().getConfig().getBoolean("FeatureToggles.CommandOnJoin")) return true;

        if (args.length < 2) {
            new Methods().sendConfigMessage(sender, "Messages.InvalidUsageCommandOnJoinMessage");
            return true;
        }

        String playerName = args[0];
        String commandToSet = "";

        for (Integer i = 1; i < args.length; i++) {

            commandToSet = commandToSet.concat(args[i]);
            if (i < args.length - 1) commandToSet = commandToSet.concat(" ");

        }

        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Main.getInstance().getConfig()
                .getString("Messages.CommandOnJoinSetMessage").replace("%player%", playerName).replace("%command%", commandToSet)));

        List<String> commands = Main.getInstance().getConfig().getStringList("CommandOnJoin");
        commands.add("Player:" + playerName + " " + "Command:" + commandToSet);
        Main.getInstance().getConfig().set("CommandOnJoin", commands);
        Main.getInstance().saveConfig();

        return true;

    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerJoin(PlayerJoinEvent event) {

        if (!Main.getInstance().getConfig().getBoolean("FeatureToggles.CommandOnJoin")) return;

        try {

            for (String s : Main.getInstance().getConfig().getStringList("CommandOnJoin")) {

                s = s.substring(s.indexOf("Player:") + 7);
                String name = s.substring(0, s.indexOf(" "));

                s = s.substring(s.indexOf("Command:") + 8);
                String command = s;

                if (event.getPlayer().getName().equals(name)) {
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command);
                    Main.getInstance().getLogger().info("Ran the command:" + command + " because " + name + " joined.");
                }

            }

        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }

    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {

        if (!Main.getInstance().getConfig().getBoolean("FeatureToggles.CommandOnJoin")) return null;

        if (args.length == 1) {

            List<String> names = new ArrayList<>();

            for (Player p : Bukkit.getOnlinePlayers()) {

                List<String> completions = new ArrayList<>();
                if (p.getName().startsWith(args[0])) {
                    completions.add(p.getName());
                }

                return completions;

            }

            return names;

        }

        return null;

    }

}
