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
import org.bukkit.event.player.*;
import org.bukkit.scheduler.BukkitRunnable;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.UUID;

public class FreezeMailPlayerListener implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerJoin (PlayerJoinEvent event) {

        new BukkitRunnable() {
            @Override
            public void run() {
                ArrayList<String> playerMail = getPlayerMail(event.getPlayer().getUniqueId());
                if (!playerMail.isEmpty()){
                    Node node = Node.builder("utility.dontfuckingmove").build();
                    LuckPermsProvider.get().getUserManager().modifyUser(event.getPlayer().getUniqueId(), (User user) -> {
                        DataMutateResult result = user.data().add(node);
                        if (!result.wasSuccessful()){
                            AlttdUtility.getInstance().getLogger().warning("Unable to give " + event.getPlayer().getName() + " the utility.dontfuckingmove permission.");
                        }
                    });
                    FileConfiguration config = AlttdUtility.getInstance().getConfig();
                    String title = ChatColor.translateAlternateColorCodes('&', config.getString("Messages.FreezeMailTitle"));
                    String subTitle = ChatColor.translateAlternateColorCodes('&', config.getString("Messages.FreezeMailSubTitle"));

                    event.getPlayer().sendTitle(title, subTitle, 20, 200, 20);
                    resendMessage(event.getPlayer());
                }
            }
        }.runTaskAsynchronously(AlttdUtility.getInstance());

    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerMove (PlayerMoveEvent event){

        if (!event.getPlayer().isOp() && event.getPlayer().hasPermission("utility.dontfuckingmove")
                && (event.getFrom().getX() != event.getTo().getX()
                || event.getFrom().getY() != event.getTo().getY()
                || event.getFrom().getZ() != event.getTo().getZ())){
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onBlockPlaceEvent(BlockPlaceEvent event){

        if (!event.getPlayer().isOp() && event.getPlayer().hasPermission("utility.dontfuckingmove")){
            event.setCancelled(true);
            resendMessage(event.getPlayer());
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onBlockBreakEvent(BlockBreakEvent event){

        if (!event.getPlayer().isOp() && event.getPlayer().hasPermission("utility.dontfuckingmove")){
            event.setCancelled(true);
            resendMessage(event.getPlayer());
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onDamageEntityEvent(DamageEntityEvent event){

        if (event.getCause().getFirstPlayer() != null && !event.getCause().getFirstPlayer().isOp() && event.getCause().getFirstPlayer().hasPermission("utility.dontfuckingmove")){
            event.setCancelled(true);
            resendMessage(event.getCause().getFirstPlayer());
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onEntityDamageByEntityEvent(EntityDamageByEntityEvent event){

        if (event.getEntity() instanceof Player){
            Player player = (Player) event.getEntity();
            if (!player.isOp() && player.hasPermission("utility.dontfuckingmove")) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onEntityAirChangeEvent (EntityAirChangeEvent event){

        if (event.getEntity() instanceof Player){
            Player player = (Player) event.getEntity();
            if (!player.isOp() && player.hasPermission("utility.dontfuckingmove")) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerCommand (PlayerCommandPreprocessEvent event){

        if (!event.getPlayer().isOp() && event.getPlayer().hasPermission("utility.dontfuckingmove")){
            event.setCancelled(true);
            resendMessage(event.getPlayer());
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerChatEvent (AsyncPlayerChatEvent event){

        if (!event.getPlayer().isOp() && event.getPlayer().hasPermission("utility.dontfuckingmove")){
            event.setCancelled(true);

            if (event.getMessage().equalsIgnoreCase("I read the message")){
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        Node node = Node.builder("utility.dontfuckingmove").build();
                        LuckPermsProvider.get().getUserManager().modifyUser(event.getPlayer().getUniqueId(), (User user) -> {
                            DataMutateResult result = user.data().remove(node);
                            if (!result.wasSuccessful()){
                                AlttdUtility.getInstance().getLogger().warning("Unable to remove the utility.dontfuckingmove permission from " + event.getPlayer().getName() + ".");
                            }
                        });
                        setMailRead(event.getPlayer().getUniqueId());
                        String messageToSend = ChatColor.translateAlternateColorCodes('&', AlttdUtility.getInstance().getConfig().getString("Messages.FreezeMailSuccessfullyCompleted"));
                        event.getPlayer().sendMessage(messageToSend.replace("%player%", event.getPlayer().getName()));
                    }
                }.runTaskAsynchronously(AlttdUtility.getInstance());
                return;
            }

            resendMessage(event.getPlayer());
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
