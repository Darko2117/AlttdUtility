package com.darko.main.destro.pvpFishing;

import me.chancesd.pvpmanager.player.CombatPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerFishEvent;

/**
 * https://discordapp.com/channels/141644560005595136/677219092717109289/775204325126963241
 */
public class PvPFishing implements Listener {

    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
    public void onPlayerFish(PlayerFishEvent event) {
        if (event.getCaught() instanceof Player) {
            CombatPlayer pvplayer = CombatPlayer.get((Player) event.getCaught());
            if (pvplayer == null) return;

            if (!pvplayer.hasPvPEnabled()) {
                event.setCancelled(true);
                event.getHook().remove();
                return;
            }
        }
    }
}
