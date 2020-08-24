package com.darko.main.utilities.itemPickup;

import com.darko.main.Main;
import com.darko.main.database.Database;
import com.darko.main.other.Methods;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.sql.ResultSet;

public class ItemPickup implements CommandExecutor, Listener {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (!Main.getInstance().getConfig().getBoolean("FeatureToggles.ItemPickupCommand")) return true;

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

                    Boolean block_item_pickup_enabled = rs.getBoolean("block_item_pickup_enabled");

                    if (!block_item_pickup_enabled) {

                        statement = "UPDATE users SET block_item_pickup_enabled = true WHERE UUID = '" + uuid + "';";
                        Database.connection.prepareStatement(statement).executeUpdate();
                        Methods.sendConfigMessage(player, "Messages.BlockItemPickupEnabledMessage");

                    } else {

                        statement = "UPDATE users SET block_item_pickup_enabled = false WHERE UUID = '" + uuid + "';";
                        Database.connection.prepareStatement(statement).executeUpdate();
                        Methods.sendConfigMessage(player, "Messages.BlockItemPickupDisabledMessage");

                    }

                    Database.reloadLoadedValues();

                } catch (Throwable throwable) {
                    throwable.printStackTrace();
                }
            }
        }.runTaskAsynchronously(Main.getInstance());

        return true;

    }

    @EventHandler
    public void onItemPickup(EntityPickupItemEvent event) {

        if (!Main.getInstance().getConfig().getBoolean("FeatureToggles.ItemPickupCommand")) return;

        if (Database.connection == null) return;

        if (!(event.getEntity() instanceof Player)) return;

        Player player = (Player) event.getEntity();

        if (Database.blockItemPickupEnabledPlayers.contains(player)) event.setCancelled(true);

    }

}
