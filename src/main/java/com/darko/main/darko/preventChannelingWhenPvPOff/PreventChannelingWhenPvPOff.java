package com.darko.main.darko.preventChannelingWhenPvPOff;

import me.chancesd.pvpmanager.player.CombatPlayer;
import org.bukkit.entity.LightningStrike;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class PreventChannelingWhenPvPOff implements Listener {

    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
    public void onEntityDamageByEntityEvent(EntityDamageByEntityEvent entityDamageByEntityEvent) {

        if (!(entityDamageByEntityEvent.getEntity() instanceof Player player))
            return;
        if (!(entityDamageByEntityEvent.getDamager() instanceof LightningStrike lightningStrike))
            return;
        if (lightningStrike.getCausingEntity() == null)
            return;

        CombatPlayer attacked = CombatPlayer.get(player);
        CombatPlayer attacker = CombatPlayer.get((Player) lightningStrike.getCausingEntity());

        if (attacked == null || attacker == null) return;
        
        if (!(attacked.hasPvPEnabled() && attacker.hasPvPEnabled())) {
            entityDamageByEntityEvent.setCancelled(true);
        }

    }

}
