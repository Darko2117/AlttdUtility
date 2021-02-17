package com.darko.main.utilities.petGodMode;

import com.darko.main.AlttdUtility;
import com.darko.main.database.Database;
import com.darko.main.other.Methods;
import de.Keyle.MyPet.MyPetApi;
import de.Keyle.MyPet.api.entity.MyPet;
import de.Keyle.MyPet.api.entity.MyPetBukkitEntity;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.sql.ResultSet;

public class PetGodMode implements CommandExecutor, Listener {

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

                    Boolean pet_god_mode_enabled = rs.getBoolean("pet_god_mode_enabled");

                    if (!pet_god_mode_enabled) {

                        statement = "UPDATE users SET pet_god_mode_enabled = true WHERE UUID = '" + uuid + "';";
                        Database.connection.prepareStatement(statement).executeUpdate();
                        new Methods().sendConfigMessage(player, "Messages.PetGodModeEnabled");

                    } else {

                        statement = "UPDATE users SET pet_god_mode_enabled = false WHERE UUID = '" + uuid + "';";
                        Database.connection.prepareStatement(statement).executeUpdate();
                        new Methods().sendConfigMessage(player, "Messages.PetGodModeDisabled");

                    }

                    Database.reloadLoadedValues();

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

        if (!(event.getEntity() instanceof MyPetBukkitEntity)) return;
        MyPetBukkitEntity craftMyPet = (MyPetBukkitEntity) event.getEntity();
        MyPet myPet = craftMyPet.getMyPet();

        if (Database.petGodModeEnabledPlayers.contains(myPet.getOwner().getPlayer())) {
            event.setCancelled(true);
            myPet.setHealth(myPet.getMaxHealth());
        }

    }

}