package com.darko.main.darko.godMode;

import com.darko.main.AlttdUtility;
import com.darko.main.common.database.Database;
import com.darko.main.common.Methods;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class GodMode implements CommandExecutor, Listener {

    private static final List<Player> enabledPlayers = new ArrayList<>();
    private static final List<Player> healingPlayers = new ArrayList<>();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (!AlttdUtility.getInstance().getConfig().getBoolean("FeatureToggles.GodModeCommand"))
            return true;

        if (!(sender instanceof Player)) {
            Methods.sendConfigMessage(sender, "Messages.PlayerOnlyCommandMessage");
            return true;
        }

        if (Database.connection == null) {
            Methods.sendConfigMessage(sender, "Messages.NoDatabaseConnectionMessage");
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

                    if (!rs.getBoolean("god_mode_enabled")) {

                        statement = "UPDATE users SET god_mode_enabled = true WHERE UUID = '" + uuid + "';";
                        Database.connection.prepareStatement(statement).executeUpdate();
                        Methods.sendConfigMessage(player, "Messages.GodModeEnabled");

                    } else {

                        statement = "UPDATE users SET god_mode_enabled = false WHERE UUID = '" + uuid + "';";
                        Database.connection.prepareStatement(statement).executeUpdate();
                        Methods.sendConfigMessage(player, "Messages.GodModeDisabled");

                    }

                    cachePlayer(player);

                } catch (Throwable throwable) {
                    throwable.printStackTrace();
                }
            }
        }.runTaskAsynchronously(AlttdUtility.getInstance());

        return true;

    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
    public void onFoodLevelChange(FoodLevelChangeEvent event) {

        if (Database.connection == null)
            return;

        Player player = (Player) event.getEntity();

        if (!enabledPlayers.contains(player))
            return;

        event.setCancelled(true);
        player.setSaturation(20);
        player.setFoodLevel(20);

    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
    public void onEntityDamageEvent(EntityDamageEvent event) {

        if (Database.connection == null)
            return;

        if (!(event.getEntity() instanceof Player player))
            return;

        if (!enabledPlayers.contains(player))
            return;

        if (healingPlayers.contains(player))
            return;

        new BukkitRunnable() {
            @Override
            public void run() {

                double health = player.getHealth();

                if (health > 19 && health < 20) {
                    health = 20;
                } else if (health <= 19) {
                    health++;
                }

                player.setHealth(health);

                if (health >= 20) {
                    healingPlayers.remove(player);
                    this.cancel();
                } else if (!healingPlayers.contains(player)) {
                    healingPlayers.add(player);
                }

            }
        }.runTaskTimer(AlttdUtility.getInstance(), 2, 2);

    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.LOWEST)
    public void onPlayerDeath(PlayerDeathEvent event) {

        if (Database.connection == null)
            return;

        Player player = event.getEntity();

        if (!enabledPlayers.contains(player))
            return;

        event.setCancelled(true);
        player.setHealth(1);

    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
    public void onPlayerJoinEvent(PlayerJoinEvent event) {
        new BukkitRunnable() {
            @Override
            public void run() {
                cachePlayer(event.getPlayer());
            }
        }.runTaskAsynchronously(AlttdUtility.getInstance());
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
    public void onPlayerQuitEvent(PlayerQuitEvent event) {
        enabledPlayers.remove(event.getPlayer());
    }

    // Caches a player from the database, if the player has god_mode enabled adds them to the list,
    // removes them otherwise
    public static void cachePlayer(Player player) {
        try {

            if (Database.connection == null)
                return;

            String statement = "SELECT god_mode_enabled FROM users WHERE UUID = '" + player.getUniqueId() + "';";
            ResultSet rs = Database.connection.prepareStatement(statement).executeQuery();
            if (!rs.next())
                return;

            if (rs.getBoolean("god_mode_enabled")) {
                enabledPlayers.add(player);
            } else {
                enabledPlayers.remove(player);
            }

        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
    }

    public static void cacheAllPlayers() {
        enabledPlayers.clear();
        for (Player player : Bukkit.getOnlinePlayers()) {
            new BukkitRunnable() {
                @Override
                public void run() {
                    cachePlayer(player);
                }
            }.runTaskAsynchronously(AlttdUtility.getInstance());
        }
    }

}
