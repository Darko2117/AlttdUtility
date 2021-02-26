package com.darko.main.teri.FreezeMail;

import com.darko.main.AlttdUtility;
import com.darko.main.database.Database;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.player.*;
import org.bukkit.scheduler.BukkitRunnable;

import java.sql.ResultSet;
import java.util.ArrayList;

public class FreezeMailPlayerListener implements Listener {

    static boolean on = false;

    static String freezeMailTitle = ChatColor.translateAlternateColorCodes('&', AlttdUtility.getInstance().getConfig().getString("Messages.FreezeMailTitle"));
    static String freezeMailSubTitle = ChatColor.translateAlternateColorCodes('&', AlttdUtility.getInstance().getConfig().getString("Messages.FreezeMailSubTitle"));
    static String freezeMailSuccessfullyCompleted = ChatColor.translateAlternateColorCodes('&', AlttdUtility.getInstance().getConfig().getString("Messages.FreezeMailSuccessfullyCompleted"));

    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
    public void onPlayerJoin(PlayerJoinEvent event) {

        //Delayed by 20 ticks because the query loading it from the database is async

        new BukkitRunnable() {
            @Override
            public void run() {

                if (!on) return;

                Player player = event.getPlayer();

                if (!Database.unreadFreezemailPlayers.contains(player)) return;

                resendFreezeMailTitle(player);
                resendFreezeMailMessage(player);

            }
        }.runTaskLater(AlttdUtility.getInstance(), 20);

    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
    public void onPlayerMove(PlayerMoveEvent event) {

        if (!on) return;

        Player player = event.getPlayer();

        if (!Database.unreadFreezemailPlayers.contains(player)) return;

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

        if (!Database.unreadFreezemailPlayers.contains(player)) return;

        event.setCancelled(true);

    }


    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
    public void onBlockBreakEvent(BlockBreakEvent event) {

        if (!on) return;

        Player player = event.getPlayer();

        if (!Database.unreadFreezemailPlayers.contains(player)) return;

        event.setCancelled(true);

    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
    public void onEntityDamage(EntityDamageByEntityEvent event) {

        if (!on) return;

        if (event.getEntity() instanceof Player) {

            Player player = (Player) event.getEntity();
            if (Database.unreadFreezemailPlayers.contains(player)) {
                event.setCancelled(true);
            }

        }
        if (event.getDamager() instanceof Player) {

            Player player = (Player) event.getDamager();
            if (Database.unreadFreezemailPlayers.contains(player)) {
                event.setCancelled(true);
            }

        }

    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
    public void onPlayerCommand(PlayerCommandPreprocessEvent event) {

        if (!on) return;

        Player player = event.getPlayer();

        if (!Database.unreadFreezemailPlayers.contains(player)) return;

        event.setCancelled(true);

    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
    public void onPlayerDropItem(PlayerDropItemEvent event) {

        if (!on) return;

        Player player = event.getPlayer();

        if (!Database.unreadFreezemailPlayers.contains(player)) return;

        event.setCancelled(true);

    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
    public void onPlayerAttemptPickupItem(PlayerAttemptPickupItemEvent event) {

        if (!on) return;

        Player player = event.getPlayer();

        if (!Database.unreadFreezemailPlayers.contains(player)) return;

        event.setCancelled(true);

    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
    public void onPlayerInteract(PlayerInteractEvent event) {

        if (!on) return;

        Player player = event.getPlayer();

        if (!Database.unreadFreezemailPlayers.contains(player)) return;

        event.setCancelled(true);

    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
    public void onProjectileLaunch(ProjectileLaunchEvent event) {

        if (!on) return;

        if(!(event.getEntity().getShooter() instanceof Player)) return;

        Player player = (Player) event.getEntity().getShooter();

        if (!Database.unreadFreezemailPlayers.contains(player)) return;

        event.setCancelled(true);

    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
    public void onPlayerChatEvent(AsyncPlayerChatEvent event) {

        if (!on) return;

        Player player = event.getPlayer();

        if (Database.unreadFreezemailPlayers.contains(player)) {

            event.setCancelled(true);

            if (event.getMessage().equalsIgnoreCase("I read the message")) {

                setMailRead(player);
                player.sendMessage(freezeMailSuccessfullyCompleted.replace("%player%", player.getName()));

            }

        }

    }

    public static void refreshON() {

        on = !Database.unreadFreezemailPlayers.isEmpty();

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

        String query = "UPDATE freeze_message SET IsRead = 1 WHERE uuid = '" + player.getUniqueId() + "' AND IsRead = 0;";

        try {

            Database.connection.prepareStatement(query).executeUpdate();
            Database.reloadLoadedValues();

        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }

    }

    public static void startFreezemailRepeater() {

        new BukkitRunnable() {
            @Override
            public void run() {

                for (Player player : Database.unreadFreezemailPlayers) {
                    new FreezeMailPlayerListener().resendFreezeMailTitle(player);
                    new FreezeMailPlayerListener().resendFreezeMailMessage(player);
                }

            }
        }.runTaskTimer(AlttdUtility.getInstance(), 240, 240);

    }

}
