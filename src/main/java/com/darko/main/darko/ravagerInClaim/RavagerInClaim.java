package com.darko.main.darko.ravagerInClaim;

import com.destroystokyo.paper.ParticleBuilder;
import me.ryanhamshire.GriefPrevention.Claim;
import me.ryanhamshire.GriefPrevention.GriefPrevention;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityChangeBlockEvent;

public class RavagerInClaim implements Listener {

    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
    public void onRavagerGriefEvent(EntityChangeBlockEvent event) {

        if (!event.getEntityType().equals(EntityType.RAVAGER))
            return;

        Location location = event.getBlock().getLocation();

        Claim claim = GriefPrevention.instance.dataStore.getClaimAt(location, true, null);

        if (claim == null)
            return;

        event.setCancelled(true);
        ParticleBuilder particleBuilder = new ParticleBuilder(Particle.REDSTONE);
        particleBuilder.color(74, 74, 74);
        particleBuilder.count(10);
        particleBuilder.location(location.add(0.5, 0.5, 0.5));
        particleBuilder.offset(0.3, 0.3, 0.3);
        particleBuilder.spawn();

    }

}
