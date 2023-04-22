package com.darko.main.darko.disablePvpOnLeave;

import me.NoChance.PvPManager.PvPlayer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class DisablePvpOnLeave implements Listener {

    @EventHandler(ignoreCancelled = true, priority = EventPriority.LOWEST)
    public void onPlayerQuit(PlayerQuitEvent event) {

        Player player = Bukkit.getPlayerExact(event.getPlayer().getName());
        PvPlayer pvplayer = PvPlayer.get(player);

        if (pvplayer.hasPvPEnabled())
            pvplayer.setPvP(false);

    }

}
