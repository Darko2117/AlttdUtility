package com.darko.main.teri.FreezeMail;

import com.darko.main.AlttdUtility;
import com.darko.main.common.BukkitTasksCache;
import com.darko.main.common.database.Database;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.player.*;
import org.bukkit.scheduler.BukkitRunnable;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class FreezeMailPlayerListener implements Listener {

    static boolean on = false;

    static String freezeMailTitle = ChatColor.translateAlternateColorCodes('&', AlttdUtility.getInstance().getConfig().getString("Messages.FreezeMailTitle"));
    static String freezeMailSubTitle = ChatColor.translateAlternateColorCodes('&', AlttdUtility.getInstance().getConfig().getString("Messages.FreezeMailSubTitle"));
    static String freezeMailSuccessfullyCompleted = ChatColor.translateAlternateColorCodes('&', AlttdUtility.getInstance().getConfig().getString("Messages.FreezeMailSuccessfullyCompleted"));

    private static final List<Player> unreadFreezemailPlayers = new ArrayList<>();

    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
    public void onPlayerJoin(PlayerJoinEvent event) {

        //Delayed by 20 ticks because the query loading it from the database is async

        new BukkitRunnable() {
            @Override
            public void run() {

                if (!on) return;

                Player player = event.getPlayer();

                if (!unreadFreezemailPlayers.contains(player)) return;

                resendFreezeMailTitle(player);
                resendFreezeMailMessage(player);

            }
        }.runTaskLater(AlttdUtility.getInstance(), 20);

    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
    public void onPlayerMove(PlayerMoveEvent event) {

        if (!on) return;

        Player player = event.getPlayer();

        if (!unreadFreezemailPlayers.contains(player)) return;

        if (event.getFrom().getX() != event.getTo().getX()
                || event.getFrom().getY() != event.getTo().getY()
                || event.getFrom().getZ() != event.getTo().getZ()) {
            event.setCancelled(true);
        }

    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
    public void onBlockPlaceEvent(BlockPlaceEvent event) {

        if (!on) return;

        Player player = event.getPlayer();

        if (!unreadFreezemailPlayers.contains(player)) return;

        event.setCancelled(true);

    }


    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
    public void onBlockBreakEvent(BlockBreakEvent event) {

        if (!on) return;

        Player player = event.getPlayer();

        if (!unreadFreezemailPlayers.contains(player)) return;

        event.setCancelled(true);

    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {

        if (!on) return;

        if (!(event.getDamager() instanceof Player)) return;

        Player player = (Player) event.getDamager();

        if (!unreadFreezemailPlayers.contains(player)) return;

        event.setCancelled(true);

    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
    public void onEntityDamage(EntityDamageEvent event) {

        if (!on) return;

        if (!(event.getEntity() instanceof Player)) return;

        Player player = (Player) event.getEntity();

        if (!unreadFreezemailPlayers.contains(player)) return;

        event.setCancelled(true);

    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
    public void onPlayerCommand(PlayerCommandPreprocessEvent event) {

        if (!on) return;

        Player player = event.getPlayer();

        if (!unreadFreezemailPlayers.contains(player)) return;

        event.setCancelled(true);

    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
    public void onPlayerDropItem(PlayerDropItemEvent event) {

        if (!on) return;

        Player player = event.getPlayer();

        if (!unreadFreezemailPlayers.contains(player)) return;

        event.setCancelled(true);

    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
    public void onPlayerAttemptPickupItem(PlayerAttemptPickupItemEvent event) {

        if (!on) return;

        Player player = event.getPlayer();

        if (!unreadFreezemailPlayers.contains(player)) return;

        event.setCancelled(true);

    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
    public void onPlayerInteract(PlayerInteractEvent event) {

        if (!on) return;

        Player player = event.getPlayer();

        if (!unreadFreezemailPlayers.contains(player)) return;

        event.setCancelled(true);

    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
    public void onProjectileLaunch(ProjectileLaunchEvent event) {

        if (!on) return;

        if (!(event.getEntity().getShooter() instanceof Player)) return;

        Player player = (Player) event.getEntity().getShooter();

        if (!unreadFreezemailPlayers.contains(player)) return;

        event.setCancelled(true);

    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
    public void onPlayerChatEvent(AsyncPlayerChatEvent event) {

        if (!on) return;

        Player player = event.getPlayer();

        if (unreadFreezemailPlayers.contains(player)) {

            event.setCancelled(true);

            if (event.getMessage().equalsIgnoreCase("I read the message")) {

                setMailRead(player);
                player.sendMessage(freezeMailSuccessfullyCompleted.replace("%player%", player.getName()));

            }

        }

    }

    public void resendFreezeMailMessage(Player player) {

        ArrayList<String> playerMail = getPlayerMail(player);

        if (playerMail.isEmpty()) return;

        StringBuilder finalMessage = new StringBuilder();

        finalMessage.append(AlttdUtility.getInstance().getConfig().getString("Messages.FreezeMailNotAcceptedYet")).append("\n");
        int i = 1;
        for (String message : playerMail) {
            finalMessage.append(i).append(". ").append(message).append("\n");
            i++;
        }

        player.sendMessage(ChatColor.translateAlternateColorCodes('&', finalMessage.toString()).replace("%player%", player.getName()));

    }

    public void resendFreezeMailTitle(Player player) {

        player.sendTitle(freezeMailTitle, freezeMailSubTitle, 20, 200, 20);

    }

    private ArrayList<String> getPlayerMail(Player player) {

        ArrayList<String> messages = new ArrayList<>();
        String query = "SELECT Message FROM freeze_message WHERE uuid = '" + player.getUniqueId() + "' AND IsRead = 0;";

        try {

            ResultSet resultSet = Database.connection.prepareStatement(query).executeQuery();

            while (resultSet.next()) {
                messages.add(resultSet.getString("Message"));
            }

        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }

        return messages;

    }

    private void setMailRead(Player player) {
        new BukkitRunnable() {
            @Override
            public void run() {
                try {

                    String query = "UPDATE freeze_message SET IsRead = 1 WHERE uuid = '" + player.getUniqueId() + "' AND IsRead = 0;";
                    Database.connection.prepareStatement(query).executeUpdate();
                    cachePlayer(player);

                } catch (Throwable throwable) {
                    throwable.printStackTrace();
                }
            }
        }.runTaskAsynchronously(AlttdUtility.getInstance());
    }

    public static void startFreezemailRepeater() {

        BukkitTasksCache.addTask(new BukkitRunnable() {
            @Override
            public void run() {

                for (Player player : unreadFreezemailPlayers) {
                    new FreezeMailPlayerListener().resendFreezeMailTitle(player);
                    new FreezeMailPlayerListener().resendFreezeMailMessage(player);
                }

            }
        }.runTaskTimerAsynchronously(AlttdUtility.getInstance(), 240, 240));

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
        unreadFreezemailPlayers.remove(event.getPlayer());
    }

    //Caches a player from the database, if the player has IsRead = 0 adds them to the list, removes them otherwise
    public static void cachePlayer(Player player) {
        try {

            if (Database.connection == null) return;

            String statement = "SELECT IsRead FROM freeze_message WHERE UUID = '" + player.getUniqueId() + "' AND IsRead = 0;";
            ResultSet rs = Database.connection.prepareStatement(statement).executeQuery();

            if (rs.next()) {
                unreadFreezemailPlayers.add(player);
            } else {
                unreadFreezemailPlayers.remove(player);
            }

            on = !unreadFreezemailPlayers.isEmpty();

        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
    }

    public static void cacheAllPlayers() {
        unreadFreezemailPlayers.clear();
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
