package com.darko.main.darko.disablePvpOnLeave;

import me.NoChance.PvPManager.PvPlayer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class DisablePvpOnLeave implements Listener {

    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
    public void onPlayerQuit(PlayerQuitEvent event){

        PvPlayer pvplayer = PvPlayer.get(event.getPlayer());

        pvplayer.setPvP(false);

    }

}
