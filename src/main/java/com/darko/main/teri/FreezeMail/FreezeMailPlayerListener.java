package com.darko.main.teri.FreezeMail;

import com.darko.main.AlttdUtility;
import com.darko.main.database.Database;
import com.sk89q.worldguard.bukkit.event.entity.DamageEntityEvent;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.model.data.DataMutateResult;
import net.luckperms.api.model.user.User;
import net.luckperms.api.node.Node;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityAirChangeEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.UUID;

public class FreezeMailPlayerListener implements Listener {

    static boolean on = false;
    static ArrayList<UUID> players = new ArrayList<>();

    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
    public void onPlayerJoin (PlayerJoinEvent event) {

        new BukkitRunnable() {
            @Override
            public void run() {
                Player player = event.getPlayer();
                ArrayList<String> playerMail = getPlayerMail(player.getUniqueId());
                if (!playerMail.isEmpty()){
                    Node node = Node.builder("utility.dontfuckingmove").build();
                    LuckPermsProvider.get().getUserManager().modifyUser(player.getUniqueId(), (User user) -> {
                        DataMutateResult result = user.data().add(node);
                        if (!result.wasSuccessful()){
                            AlttdUtility.getInstance().getLogger().warning("Unable to give " + player.getName() + " the utility.dontfuckingmove permission.");
                        } else {
                            on = true;
                            players.add(player.getUniqueId());
                        }
                    });
                    FileConfiguration config = AlttdUtility.getInstance().getConfig();
                    String title = ChatColor.translateAlternateColorCodes('&', config.getString("Messages.FreezeMailTitle"));
                    String subTitle = ChatColor.translateAlternateColorCodes('&', config.getString("Messages.FreezeMailSubTitle"));

                    player.sendTitle(title, subTitle, 20, 200, 20);
                    resendMessage(player);
                }
            }
        }.runTaskAsynchronously(AlttdUtility.getInstance());

    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
    public void onPlayerQuit (PlayerQuitEvent event){

        if (on) {
            final Player player = event.getPlayer();

            if (!player.isOp() && player.hasPermission("utility.dontfuckingmove")) {
                players.remove(player.getUniqueId());
                on = !players.isEmpty();
            }
        }
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
    public void onPlayerMove (PlayerMoveEvent event){

        if (on){
            final Player player = event.getPlayer();

            if (!player.isOp() && player.hasPermission("utility.dontfuckingmove")
                    && (event.getFrom().getX() != event.getTo().getX()
                    || event.getFrom().getY() != event.getTo().getY()
                    || event.getFrom().getZ() != event.getTo().getZ())){
                event.setCancelled(true);
            }
        }
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
    public void onBlockPlaceEvent(BlockPlaceEvent event){

        if (on) {
            final Player player = event.getPlayer();

            if (!player.isOp() && player.hasPermission("utility.dontfuckingmove")) {
                event.setCancelled(true);
                resendMessage(player);
            }
        }
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
    public void onBlockBreakEvent(BlockBreakEvent event){

        if (on) {
            final Player player = event.getPlayer();

            if (!player.isOp() && player.hasPermission("utility.dontfuckingmove")) {
                event.setCancelled(true);
                resendMessage(player);
            }
        }
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
    public void onDamageEntityEvent(DamageEntityEvent event){

        if (on){
            if (event.getEntity() instanceof Player) {
                final Player player = (Player) event.getEntity();
                if (!player.isOp() && player.hasPermission("utility.dontfuckingmove")) {
                    event.setCancelled(true);
                }
            }
            if (event.getCause().getFirstPlayer() != null && !event.getCause().getFirstPlayer().isOp() && event.getCause().getFirstPlayer().hasPermission("utility.dontfuckingmove")){
                event.setCancelled(true);
                resendMessage(event.getCause().getFirstPlayer());
            }
        }
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
    public void onEntityDamageByEntityEvent(EntityDamageByEntityEvent event){

        if (on && event.getEntity() instanceof Player){
            final Player player = (Player) event.getEntity();
            if (!player.isOp() && player.hasPermission("utility.dontfuckingmove")) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
    public void onEntityAirChangeEvent (EntityAirChangeEvent event){

        if (on && event.getEntity() instanceof Player){
            final Player player = (Player) event.getEntity();

            if (!player.isOp() && player.hasPermission("utility.dontfuckingmove")) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
    public void onPlayerCommand (PlayerCommandPreprocessEvent event){

        if (on) {
            final Player player = event.getPlayer();

            if (!player.isOp() && player.hasPermission("utility.dontfuckingmove")) {
                event.setCancelled(true);
                resendMessage(player);
            }
        }
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
    public void onPlayerChatEvent (AsyncPlayerChatEvent event){

        if (on) {
            final Player player = event.getPlayer();

            if (!player.isOp() && player.hasPermission("utility.dontfuckingmove")) {
                event.setCancelled(true);

                if (event.getMessage().equalsIgnoreCase("I read the message")) {
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            Node node = Node.builder("utility.dontfuckingmove").build();
                            LuckPermsProvider.get().getUserManager().modifyUser(player.getUniqueId(), (User user) -> {
                                DataMutateResult result = user.data().remove(node);
                                if (!result.wasSuccessful()) {
                                    AlttdUtility.getInstance().getLogger().warning("Unable to remove the utility.dontfuckingmove permission from " + player.getName() + ".");
                                }
                            });
                            setMailRead(player.getUniqueId());
                            String messageToSend = ChatColor.translateAlternateColorCodes('&', AlttdUtility.getInstance().getConfig().getString("Messages.FreezeMailSuccessfullyCompleted"));
                            player.sendMessage(messageToSend.replace("%player%", player.getName()));
                        }
                    }.runTaskAsynchronously(AlttdUtility.getInstance());
                    return;
                }

                resendMessage(player);
            }
        }

    }

    private void resendMessage(Player player) {
        new BukkitRunnable() {
            @Override
            public void run() {
                ArrayList<String> playerMail = getPlayerMail(player.getUniqueId());
                if (playerMail.isEmpty()){
                    Node node = Node.builder("utility.dontfuckingmove").build();
                    LuckPermsProvider.get().getUserManager().modifyUser(player.getUniqueId(), (User user) -> {
                        DataMutateResult result = user.data().remove(node);
                        if (!result.wasSuccessful()){
                            AlttdUtility.getInstance().getLogger().warning("Unable to remove the utility.dontfuckingmove permission from " + player.getName() + ".");
                        }
                    });
                    return;
                }

                StringBuilder finalMessage = new StringBuilder();

                finalMessage.append(AlttdUtility.getInstance().getConfig().getString("Messages.FreezeMailNotAcceptedYet")).append("\n");
                int i = 1;
                for (String message : playerMail){
                    finalMessage.append(i).append(". ").append(message).append("\n");
                    i++;
                }

                player.sendMessage(ChatColor.translateAlternateColorCodes('&', finalMessage.toString()).replace("%player%", player.getName()));
            }
        }.runTaskAsynchronously(AlttdUtility.getInstance());
    }

    private void setMailRead(UUID uuid) {
        String query = "UPDATE freeze_message SET IsRead = 1 WHERE uuid = ? AND IsRead = 0";

        try {
            PreparedStatement preparedStatement = Database.connection.prepareStatement(query);
            preparedStatement.setString(1, uuid.toString());

            preparedStatement.execute();

        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
    }

    private ArrayList<String> getPlayerMail(UUID uuid){
        ArrayList<String> messages = new ArrayList<>();
        String query = "SELECT Message FROM freeze_message WHERE uuid = ? AND IsRead = 0";

        try {
            PreparedStatement preparedStatement = Database.connection.prepareStatement(query);
            preparedStatement.setString(1, uuid.toString());

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()){
                messages.add(resultSet.getString("Message"));
            }

        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }

        return messages;
    }

}
