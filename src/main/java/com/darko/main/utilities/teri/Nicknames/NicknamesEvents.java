package com.darko.main.utilities.teri.Nicknames;

import com.Zrips.CMI.utils.Util;
import com.darko.main.Main;
import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;
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
                    Nicknames.getInstance().setNick(player, null);
                    return;
                }

                String nickName = nick.getCurrentNick();
                final String cmiNick = Util.CMIChatColor.deColorize(Nicknames.getInstance().getNick(player));

                if (cmiNick != null) {
                    if (nickName == null) {
                        Nicknames.getInstance().resetNick(player);
                    }
                    else if (!nickName.equals(cmiNick)) {
                        Nicknames.getInstance().setNick(player, nickName);
                    }
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
        if(!subChannel.equals("NickNames")) {
            return;
        }

        try {
            short len = in.readShort();
            byte[] msgbytes = new byte[len];
            in.readFully(msgbytes);

            DataInputStream msgin = new DataInputStream(new ByteArrayInputStream(msgbytes));
            String playerName = msgin.readUTF();
            String notification = Utilities.applyColor(Main.getInstance().getConfig().getString("Messages.NickNewRequest")
                    .replace("%player", playerName));
            Main.getInstance().getServer().getOnlinePlayers().forEach(p ->{
                if (p.hasPermission("utility.nick.review")){
                    p.sendMessage(notification);
                }
            });
            Nicknames.getInstance().nickCacheUpdate = true;

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
