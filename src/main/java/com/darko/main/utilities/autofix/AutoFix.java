package com.darko.main.utilities.autofix;

import com.darko.main.other.Methods;
import com.darko.main.database.Database;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.darko.main.Main;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemDamageEvent;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

import java.sql.ResultSet;

public class AutoFix implements CommandExecutor, Listener {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (!Main.getInstance().getConfig().getBoolean("FeatureToggles.AutofixCommand")) return true;

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

                    Boolean autofix_enabled = rs.getBoolean("autofix_enabled");

                    if (!autofix_enabled) {

                        statement = "UPDATE users SET autofix_enabled = true WHERE UUID = '" + uuid + "';";
                        Database.connection.prepareStatement(statement).executeUpdate();
                        new Methods().sendConfigMessage(player, "Messages.AutofixEnabled");

                    } else {

                        statement = "UPDATE users SET autofix_enabled = false WHERE UUID = '" + uuid + "';";
                        Database.connection.prepareStatement(statement).executeUpdate();
                        new Methods().sendConfigMessage(player, "Messages.AutofixDisabled");

                    }

                    Database.reloadLoadedValues();

                } catch (Throwable throwable) {
                    throwable.printStackTrace();
                }
            }
        }.runTaskAsynchronously(Main.getInstance());

        return true;

    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onDurabilityUse(PlayerItemDamageEvent event) {

        if (event.isCancelled()) return;
        if (!Main.getInstance().getConfig().getBoolean("FeatureToggles.AutofixCommand")) return;

        if (Database.connection == null) return;

        Player player = event.getPlayer();

        if (Database.autofixEnabledPlayers.contains(player)) {

            Damageable durability = (Damageable) event.getItem().getItemMeta();
            durability.setDamage(0);
            event.getItem().setItemMeta((ItemMeta) durability);
            event.setCancelled(true);

        }

    }

}
