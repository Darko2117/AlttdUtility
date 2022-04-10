package com.darko.main.darko.magnet;

import com.darko.main.AlttdUtility;
import com.darko.main.common.BukkitTasksCache;
import com.darko.main.common.Methods;
import com.darko.main.common.database.Database;
import com.destroystokyo.paper.ParticleBuilder;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Magnet implements CommandExecutor, Listener {

    private static final List<Player> enabledPlayers = new ArrayList<>();

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (!AlttdUtility.getInstance().getConfig().getBoolean("FeatureToggles.MagnetCommand")) return true;

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

                    if (!rs.getBoolean("magnet_enabled")) {

                        statement = "UPDATE users SET magnet_enabled = true WHERE UUID = '" + uuid + "';";
                        Database.connection.prepareStatement(statement).executeUpdate();
                        new Methods().sendConfigMessage(player, "Messages.MagnetEnabled");

                    } else {

                        statement = "UPDATE users SET magnet_enabled = false WHERE UUID = '" + uuid + "';";
                        Database.connection.prepareStatement(statement).executeUpdate();
                        new Methods().sendConfigMessage(player, "Messages.MagnetDisabled");

                    }

                    cachePlayer(player);

                } catch (Throwable throwable) {
                    throwable.printStackTrace();
                }
            }
        }.runTaskAsynchronously(AlttdUtility.getInstance());

        return true;

    }

    public static void initiate() {
        BukkitTasksCache.addTask(new BukkitRunnable() {
            @Override
            public void run() {

                if (Database.connection == null) return;

                for (Player player : enabledPlayers) {

                    if (player.getGameMode().equals(GameMode.SPECTATOR)) continue;

                    for (Entity entity : player.getNearbyEntities(10, 10, 10)) {

                        if (!(entity instanceof Item)) continue;

                        int originalAmount = ((Item) entity).getItemStack().getAmount();

                        HashMap<Integer, ItemStack> remainingItems = player.getInventory().addItem(((Item) entity).getItemStack().clone());

                        int remainingAmount = 0;
                        if (!remainingItems.isEmpty()) {
                            remainingAmount = remainingItems.get(0).getAmount();
                        }

                        if (remainingAmount == 0) {

                            Bukkit.getPluginManager().callEvent(new EntityPickupItemEvent(player, (Item) entity, remainingAmount));

                            entity.remove();

                            player.playSound(player.getLocation(), Sound.ENTITY_ITEM_PICKUP, 1, 5);

                        } else if (originalAmount != remainingAmount) {

                            ItemStack itemStackClone = ((Item) entity).getItemStack().clone();
                            itemStackClone.setAmount(originalAmount - remainingAmount);
                            ((Item) entity).setItemStack(itemStackClone);
                            Bukkit.getPluginManager().callEvent(new EntityPickupItemEvent(player, (Item) entity, remainingAmount));

                            ((Item) entity).setItemStack(remainingItems.get(0));

                            player.playSound(player.getLocation(), Sound.ENTITY_ITEM_PICKUP, 1, 5);

                        } else continue;

                        ParticleBuilder particleBuilder = new ParticleBuilder(Particle.REDSTONE);
                        particleBuilder.location(entity.getLocation());
                        particleBuilder.location().setY(particleBuilder.location().getY() + 0.25);
                        particleBuilder.offset(0.1, 0.1, 0.1);
                        particleBuilder.color(255, 255, 255);
                        particleBuilder.count(30);
                        particleBuilder.receivers(30);
                        particleBuilder.spawn();

                    }

                }

            }
        }.runTaskTimer(AlttdUtility.getInstance(), 5, 5));
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

    //Caches a player from the database, if the player has magnet enabled adds them to the list, removes them otherwise
    public static void cachePlayer(Player player) {
        try {

            if (Database.connection == null) return;

            String statement = "SELECT magnet_enabled FROM users WHERE UUID = '" + player.getUniqueId() + "';";
            ResultSet rs = Database.connection.prepareStatement(statement).executeQuery();
            if (!rs.next()) return;

            if (rs.getBoolean("magnet_enabled")) {
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
