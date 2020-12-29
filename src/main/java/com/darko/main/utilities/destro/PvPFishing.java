package com.darko.main.utilities.destro;

import com.darko.main.Main;
import me.NoChance.PvPManager.PvPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerFishEvent;

/**
 * https://discordapp.com/channels/141644560005595136/677219092717109289/775204325126963241
 */
public class PvPFishing implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerFish(PlayerFishEvent event) {
        if (!Main.getInstance().getConfig().getBoolean("FeatureToggles.PreventNoPvPFishing", false)) return;
        if(event.isCancelled()) return;
        if(event.getCaught() instanceof Player) {
            PvPlayer pvplayer = PvPlayer.get((Player) event.getCaught());
            if(!pvplayer.hasPvPEnabled()) {
                event.setCancelled(true);
                event.getHook().remove();
                return;
            }
        }
    }
}
