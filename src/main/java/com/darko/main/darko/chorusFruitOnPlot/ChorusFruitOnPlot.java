package com.darko.main.darko.chorusFruitOnPlot;

import com.darko.main.AlttdUtility;
import com.plotsquared.core.PlotSquared;
import com.plotsquared.core.location.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.scheduler.BukkitRunnable;

public class ChorusFruitOnPlot implements Listener {

    @EventHandler(ignoreCancelled = true, priority = EventPriority.LOWEST)
    public void onProjectileHit(ProjectileHitEvent event) {

        if (event.getHitBlock() == null) return;
        if (!event.getHitBlock().getType().equals(Material.CHORUS_FLOWER)) return;

        Projectile arrow = event.getEntity();
        if (!(arrow.getShooter() instanceof Player)) return;
        Player shooter = (Player) arrow.getShooter();
        Block chorus = event.getHitBlock();

        Location chorusLocation = new Location(chorus.getLocation().getWorld().getName(), chorus.getLocation().getBlockX(), chorus.getLocation().getBlockY(), chorus.getLocation().getBlockZ());

        if (PlotSquared.get().getApplicablePlotArea(chorusLocation).getPlot(chorusLocation).getTrusted().contains(shooter.getUniqueId()))
            return;
        if (PlotSquared.get().getApplicablePlotArea(chorusLocation).getPlot(chorusLocation).getOwner().equals(shooter.getUniqueId()))
            return;

        arrow.remove();
        chorus.setType(Material.AIR);

        new BukkitRunnable() {
            @Override
            public void run() {
                chorus.setType(Material.CHORUS_FLOWER);
            }
        }.runTaskLater(AlttdUtility.getInstance(), 0);

    }

}