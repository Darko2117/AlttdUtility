package com.darko.main.utilities.rebootWhitelist;

import com.darko.main.Main;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;

public class RebootWhitelist implements CommandExecutor, TabCompleter, Listener {

    static Boolean enabled = false;

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerLogin(PlayerLoginEvent event) {

        if (!event.getResult().equals(PlayerLoginEvent.Result.ALLOWED)) return;

        if (!enabled) return;

        if (!Main.getInstance().getConfig().getBoolean("FeatureToggles.RebootWhitelist")) return;

        event.setResult(PlayerLoginEvent.Result.KICK_OTHER);
        event.setKickMessage(ChatColor.translateAlternateColorCodes('&', Main.getInstance().getConfig().getString("Messages.RebootWhitelistKickMessage")));

        Main.getInstance().getLogger().info(event.getPlayer().getName() + " attempted to join but RebootWhitelist is on.");

    }

    public static void disableAfterBoot() {

        new BukkitRunnable() {
            @Override
            public void run() {
                if (enabled) {
                    enabled = false;
                    Main.getInstance().getConfig().set("RebootWhitelist.Enabled", false);
                    Main.getInstance().saveConfig();
                    Main.getInstance().getLogger().info("RebootWhitelist disabled.");
                }
            }
        }.runTaskLater(Main.getInstance(), Main.getInstance().getConfig().getInt("RebootWhitelist.DisableTimeAfterBoot") * 20);

    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (!Main.getInstance().getConfig().getBoolean("FeatureToggles.RebootWhitelist")) return true;

        if (args.length == 0)
            enabled = !enabled;
        else if (args[0].toLowerCase().equals("on")) enabled = true;
        else if (args[0].toLowerCase().equals("off")) enabled = false;

        if (enabled) {

            for (String s : Main.getInstance().getConfig().getStringList("RebootWhitelist.CommandsOnEnable")) {
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), s);
            }

            Main.getInstance().getConfig().set("RebootWhitelist.Enabled", true);
            Main.getInstance().saveConfig();
            sender.sendMessage(ChatColor.GREEN + "RebootWhitelist enabled.");
        } else {
            Main.getInstance().getConfig().set("RebootWhitelist.Enabled", false);
            Main.getInstance().saveConfig();
            sender.sendMessage(ChatColor.RED + "RebootWhitelist disabled.");
        }

        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {

        if (Main.getInstance().getConfig().getBoolean("FeatureToggles.RebootWhitelist")) return null;

        if (args.length == 1) {

            List<String> choices = new ArrayList<>();
            choices.add("on");
            choices.add("off");

            List<String> completions = new ArrayList<>();
            for (String s : choices) {
                if (s.toLowerCase().startsWith(args[0].toLowerCase())) {
                    completions.add(s);
                }
            }

            return completions;

        }

        return null;

    }

    public static void reload() {

        enabled = Main.getInstance().getConfig().getBoolean("RebootWhitelist.Enabled");

    }

}
