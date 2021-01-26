package com.darko.main.utilities.commandOnJoin;

import com.darko.main.AlttdUtility;
import com.darko.main.database.Database;
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
import org.bukkit.scheduler.BukkitRunnable;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class CommandOnJoin implements CommandExecutor, Listener, TabCompleter {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (!AlttdUtility.getInstance().getConfig().getBoolean("FeatureToggles.CommandOnJoin")) return true;

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

        String statement = "INSERT INTO command_on_join(Username, Command) VALUES("
                + "'" + playerName + "', "
                + "'" + commandToSet + "'"
                + ");";

        try {

            Database.connection.prepareStatement(statement).executeUpdate();
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', AlttdUtility.getInstance().getConfig()
                    .getString("Messages.CommandOnJoinSetMessage").replace("%player%", playerName).replace("%command%", commandToSet)));

        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }

        return true;

    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerJoin(PlayerJoinEvent event) {

        if (!AlttdUtility.getInstance().getConfig().getBoolean("FeatureToggles.CommandOnJoin")) return;

        new BukkitRunnable() {
            @Override
            public void run() {

                if (Database.connection == null) return;

                String playerName = event.getPlayer().getName();

                String statement = "SELECT * FROM command_on_join WHERE Username = '" + playerName + "';";

                try {

                    ResultSet rs = Database.connection.prepareStatement(statement).executeQuery();

                    while (rs.next()) {

                        String command = rs.getString("Command");

                        new BukkitRunnable() {
                            @Override
                            public void run() {

                                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command);
                                AlttdUtility.getInstance().getLogger().info("Ran the command: " + command + " because " + playerName + " joined.");

                            }
                        }.runTask(AlttdUtility.getInstance());
                    }

                } catch (Throwable throwable) {
                    throwable.printStackTrace();
                }

            }
        }.runTaskAsynchronously(AlttdUtility.getInstance());

    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {

        if (!AlttdUtility.getInstance().getConfig().getBoolean("FeatureToggles.CommandOnJoin")) return null;

        if (args.length == 1) {

            List<String> completions = new ArrayList<>();

            for (Player player : Bukkit.getOnlinePlayers()) {

                if (player.getName().toLowerCase().startsWith(args[0].toLowerCase())) {
                    completions.add(player.getName());
                }

            }

            return completions;

        }

        return null;

    }

}
