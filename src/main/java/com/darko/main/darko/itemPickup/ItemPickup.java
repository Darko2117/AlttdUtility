package com.darko.main.darko.itemPickup;

import com.darko.main.AlttdUtility;
import com.darko.main.common.database.Database;
import com.darko.main.common.Methods;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.sql.ResultSet;

public class ItemPickup implements CommandExecutor, Listener {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (!AlttdUtility.getInstance().getConfig().getBoolean("FeatureToggles.ItemPickupCommand")) return true;

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

                    Boolean block_item_pickup_enabled = rs.getBoolean("block_item_pickup_enabled");

                    if (!block_item_pickup_enabled) {

                        statement = "UPDATE users SET block_item_pickup_enabled = true WHERE UUID = '" + uuid + "';";
                        Database.connection.prepareStatement(statement).executeUpdate();
                        new Methods().sendConfigMessage(player, "Messages.BlockItemPickupEnabledMessage");

                    } else {

                        statement = "UPDATE users SET block_item_pickup_enabled = false WHERE UUID = '" + uuid + "';";
                        Database.connection.prepareStatement(statement).executeUpdate();
                        new Methods().sendConfigMessage(player, "Messages.BlockItemPickupDisabledMessage");

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
    public void onItemPickup(EntityPickupItemEvent event) {

        if (Database.connection == null) return;

        if (!(event.getEntity() instanceof Player)) return;

        Player player = (Player) event.getEntity();

        if (Database.blockItemPickupEnabledPlayers.contains(player)) event.setCancelled(true);

    }

}
