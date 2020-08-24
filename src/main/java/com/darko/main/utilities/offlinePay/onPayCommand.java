package com.darko.main.utilities.offlinePay;

import com.darko.main.other.Methods;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import com.darko.main.Main;

public class onPayCommand implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPayCommands(PlayerCommandPreprocessEvent event) {

        if(!Main.getInstance().getConfig().getBoolean("FeatureToggles.BlockOfflinePay")) return;

        Player player = event.getPlayer();
        String command = event.getMessage();
        if (player.hasPermission("utility.offlinepay.disabled")) {
            if (command.startsWith("/cmi pay ") && command.endsWith(" -confirmed")) {
                StringBuilder name = new StringBuilder(command);
                name.delete(0, 9);
                name.reverse();
                name.delete(0, 11);
                name.delete(0, name.indexOf(" ") + 1);
                name.reverse();
                if (!onlineChecker(name.toString())) {
                    event.setCancelled(true);
                    Methods.sendConfigMessage(player, "Messages.OfflinePlayerPayment");
                }
            } else if (command.startsWith("/cmi:cmi pay ")) {
                StringBuilder name = new StringBuilder(command);
                name.delete(0, 13);
                name.reverse();
                name.delete(0, name.indexOf(" ") + 1);
                name.reverse();
                if (!onlineChecker(name.toString())) {
                    event.setCancelled(true);
                    Methods.sendConfigMessage(player, "Messages.OfflinePlayerPayment");
                }
            } else if (command.startsWith("/cmi pay ")) {
                StringBuilder name = new StringBuilder(command);
                name.delete(0, 9);
                name.reverse();
                name.delete(0, name.indexOf(" ") + 1);
                name.reverse();
                if (!onlineChecker(name.toString())) {
                    event.setCancelled(true);
                    Methods.sendConfigMessage(player, "Messages.OfflinePlayerPayment");
                }
            } else if (command.startsWith("/cmi:cmi money pay ")) {
                StringBuilder name = new StringBuilder(command);
                name.delete(0, 19);
                name.reverse();
                name.delete(0, name.indexOf(" ") + 1);
                name.reverse();
                if (!onlineChecker(name.toString())) {
                    event.setCancelled(true);
                    Methods.sendConfigMessage(player, "Messages.OfflinePlayerPayment");
                }
            } else if (command.startsWith("/cmi money pay ")) {
                StringBuilder name = new StringBuilder(command);
                name.delete(0, 15);
                name.reverse();
                name.delete(0, name.indexOf(" ") + 1);
                name.reverse();
                if (!onlineChecker(name.toString())) {
                    event.setCancelled(true);
                    Methods.sendConfigMessage(player, "Messages.OfflinePlayerPayment");
                }
            } else if (command.startsWith("/pay ")) {
                StringBuilder name = new StringBuilder(command);
                name.delete(0, 5);
                name.reverse();
                name.delete(0, name.indexOf(" ") + 1);
                name.reverse();
                if (!onlineChecker(name.toString())) {
                    event.setCancelled(true);
                    Methods.sendConfigMessage(player, "Messages.OfflinePlayerPayment");
                }
            }
        }
    }

    Boolean onlineChecker(String string) {
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (player.getName().equalsIgnoreCase(string)) {
                return true;
            }
        }
        return false;
    }
}
