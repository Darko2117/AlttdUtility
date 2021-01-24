package com.darko.main.utilities.teri.Nicknames;

import com.Zrips.CMI.utils.Util;
import com.darko.main.Main;
import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.event.EventHandler;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.messaging.PluginMessageListener;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.util.UUID;

import static com.darko.main.utilities.teri.Nicknames.Nicknames.format;

public class NicknamesEvents implements Listener, PluginMessageListener
{
    @EventHandler
    public void onPlayerJoin(final PlayerJoinEvent e){

        new BukkitRunnable() {
            @Override
            public void run() {
                if (Nicknames.instance.NickCache.isEmpty()){
                    DatabaseQueries.getNicknamesList().forEach(nick -> Nicknames.instance.NickCache.put(nick.getUuid(), nick));
                }

                final Player player = e.getPlayer();
                final Nick nick = DatabaseQueries.getNick(player.getUniqueId());

                if (nick == null){
                    Nicknames.getInstance().resetNick(player);
                    return;
                }

                String nickName = nick.getCurrentNick();
                final String cmiNick = Util.CMIChatColor.deColorize(Nicknames.getInstance().getNick(player));

                if (nickName == null) {
                    Nicknames.getInstance().resetNick(player);
                }
                else if (!nickName.equals(cmiNick)) {
                    Nicknames.getInstance().setNick(player, nickName);
                }

                Nicknames.getInstance().NickCache.put(e.getPlayer().getUniqueId(), nick);
            }
        }.runTaskAsynchronously(Main.getInstance());
    }

    @EventHandler
    public void onPlayerQuit(final PlayerQuitEvent e){
        final UUID uuid = e.getPlayer().getUniqueId();

        if (Nicknames.getInstance().NickCache.containsKey(uuid) && ! Nicknames.getInstance().NickCache.get(uuid).hasRequest()){
            Nicknames.getInstance().NickCache.remove(uuid);
        }
    }

    @Override
    public void onPluginMessageReceived(String channel, Player player, byte[] message) {
        if(!channel.equals("BungeeCord")) {
            return;
        }
        ByteArrayDataInput in = ByteStreams.newDataInput(message);
        String subChannel = in.readUTF();
        if(!subChannel.equals("NickNameRequest") && !subChannel.equals("NickNameAccepted")
                && !subChannel.equals("NickNameDenied") && !subChannel.equals("NickNameSet")) {
            return;
        }
        UUID playerUUID;
        OfflinePlayer offlinePlayer;
        String name;
        try {
            short len = in.readShort();
            byte[] msgbytes = new byte[len];
            in.readFully(msgbytes);

            DataInputStream msgin = new DataInputStream(new ByteArrayInputStream(msgbytes));
            playerUUID = UUID.fromString(msgin.readUTF());
            offlinePlayer = Main.getInstance().getServer().getOfflinePlayer(playerUUID);
            name = offlinePlayer.getName();

        } catch (Exception e) {
            e.printStackTrace();
            return;
        }

        switch (subChannel){
            case "NickNameRequest":
                String notification = Utilities.applyColor(Main.getInstance().getConfig().getString("Messages.NickNewRequest")
                        .replace("%player%", name == null ? playerUUID.toString() : name));
                Main.getInstance().getServer().getOnlinePlayers().forEach(p ->{
                    if (p.hasPermission("utility.nick.review")){
                        p.sendMessage(notification);
                    }
                });
                Nicknames.getInstance().nickCacheUpdate.add(playerUUID);

                if (offlinePlayer.isOnline()){
                    Nick nick = DatabaseQueries.getNick(playerUUID);
                    if (nick != null && nick.getCurrentNick() != null) {
                        Nicknames.getInstance().setNick(offlinePlayer.getPlayer(), nick.getCurrentNick());
                    }
                }
                break;
            case "NickNameAccepted":
                //No break on purpose
            case "NickNameSet":
                Nicknames.getInstance().nickCacheUpdate.add(playerUUID);
                if (offlinePlayer.isOnline()){
                    Nick nick = DatabaseQueries.getNick(playerUUID);
                    Player target = Bukkit.getPlayer(playerUUID);
                    if (target != null && nick != null && nick.getCurrentNick() != null) {
                        Nicknames.getInstance().setNick(target, nick.getCurrentNick());
                        target.sendMessage(format(Main.getInstance().getConfig().getString("Messages.NickChanged")
                                .replace("%nickname%", nick.getNewNick())));
                    }
                }
                break;
            case "NickNameDenied":
                Nicknames.getInstance().NickCache.remove(playerUUID);
                break;
        }
    }
    public static String format(final String m) {
        return Utilities.applyColor(m);
    }
}
