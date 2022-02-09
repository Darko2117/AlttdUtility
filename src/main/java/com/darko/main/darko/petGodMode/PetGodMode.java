package com.darko.main.darko.petGodMode;

import com.darko.main.AlttdUtility;
import com.darko.main.common.database.Database;
import com.darko.main.common.Methods;
import de.Keyle.MyPet.api.entity.MyPet;
import de.Keyle.MyPet.api.entity.MyPetBukkitEntity;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class PetGodMode implements CommandExecutor, Listener {

    private static final List<Player> enabledPlayers = new ArrayList<>();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (!AlttdUtility.getInstance().getConfig().getBoolean("FeatureToggles.PetGodModeCommand")) return true;

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

                    if (!rs.getBoolean("pet_god_mode_enabled")) {

                        statement = "UPDATE users SET pet_god_mode_enabled = true WHERE UUID = '" + uuid + "';";
                        Database.connection.prepareStatement(statement).executeUpdate();
                        new Methods().sendConfigMessage(player, "Messages.PetGodModeEnabled");

                    } else {

                        statement = "UPDATE users SET pet_god_mode_enabled = false WHERE UUID = '" + uuid + "';";
                        Database.connection.prepareStatement(statement).executeUpdate();
                        new Methods().sendConfigMessage(player, "Messages.PetGodModeDisabled");

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
    public void onEntityDamage(EntityDamageEvent event) {

        if (Database.connection == null) return;

        if (!(event.getEntity() instanceof MyPetBukkitEntity craftMyPet)) return;
        MyPet myPet = craftMyPet.getMyPet();

        if (!enabledPlayers.contains(myPet.getOwner().getPlayer())) return;

        event.setCancelled(true);
        myPet.setHealth(myPet.getMaxHealth());

    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
    public void onPlayerJoinEvent(PlayerJoinEvent event) {
        cachePlayer(event.getPlayer());
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
    public void onPlayerQuitEvent(PlayerQuitEvent event) {
        enabledPlayers.remove(event.getPlayer());
    }

    //Caches a player from the database, if the player has pet_god_mode enabled adds them to the list, removes them otherwise
    public static void cachePlayer(Player player) {
        try {

            if (Database.connection == null) return;

            String statement = "SELECT pet_god_mode_enabled FROM users WHERE UUID = '" + player.getUniqueId() + "';";
            ResultSet rs = Database.connection.prepareStatement(statement).executeQuery();
            if (!rs.next()) return;

            if (rs.getBoolean("pet_god_mode_enabled")) {
                enabledPlayers.add(player);
            } else {
                enabledPlayers.remove(player);
            }

        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
    }

    public static void cacheAllPlayers() {
        for (Player player : Bukkit.getOnlinePlayers()) cachePlayer(player);
    }

}
