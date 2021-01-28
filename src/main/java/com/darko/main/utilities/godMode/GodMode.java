package com.darko.main.utilities.godMode;

import com.darko.main.AlttdUtility;
import com.darko.main.database.Database;
import com.darko.main.other.Methods;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.sql.ResultSet;

public class GodMode implements CommandExecutor, Listener {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (!AlttdUtility.getInstance().getConfig().getBoolean("FeatureToggles.GodModeCommand")) return true;

        if (!(sender instanceof Player)) {
            new Methods().sendConfigMessage(sender, "Messages.PlayerOnlyCommandMessage");
            return true;
        }

        if (Database.connection == null) {
            new Methods().sendConfigMessage(sender, "Messages.NoDatabaseConnectionMessage");
            return true;
        }

        new BukkitRunnable() {
            @Override
            public void run() {
                try {

                    Player player = (Player) sender;
                    String uuid = player.getUniqueId().toString();

                    String statement = "SELECT * FROM users WHERE UUID = '" + uuid + "';";

                    ResultSet rs = Database.connection.prepareStatement(statement).executeQuery();
                    rs.next();

                    Boolean god_mode_enabled = rs.getBoolean("god_mode_enabled");

                    if (!god_mode_enabled) {

                        statement = "UPDATE users SET god_mode_enabled = true WHERE UUID = '" + uuid + "';";
                        Database.connection.prepareStatement(statement).executeUpdate();
                        new Methods().sendConfigMessage(player, "Messages.GodModeEnabled");

                    } else {

                        statement = "UPDATE users SET god_mode_enabled = false WHERE UUID = '" + uuid + "';";
                        Database.connection.prepareStatement(statement).executeUpdate();
                        new Methods().sendConfigMessage(player, "Messages.GodModeDisabled");

                    }

                    Database.reloadLoadedValues();

                } catch (Throwable throwable) {
                    throwable.printStackTrace();
                }
            }
        }.runTaskAsynchronously(AlttdUtility.getInstance());

        return true;

    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onFoodLevelChange(FoodLevelChangeEvent event) {

        if (event.isCancelled()) return;
        if (!AlttdUtility.getInstance().getConfig().getBoolean("FeatureToggles.GodModeCommand")) return;

        if (Database.connection == null) return;

        Player player = (Player) event.getEntity();

        if (Database.godModeEnabledPlayers.contains(player)) {

            event.setCancelled(true);
            player.setSaturation(20);

        }

    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerDeath(PlayerDeathEvent event) {

        if (event.isCancelled()) return;
        if (!AlttdUtility.getInstance().getConfig().getBoolean("FeatureToggles.GodModeCommand")) return;

        if (Database.connection == null) return;

        Player player = event.getEntity();

        if (Database.godModeEnabledPlayers.contains(player)) {

            event.setCancelled(true);
            player.setHealth(1);

        }

    }

}
