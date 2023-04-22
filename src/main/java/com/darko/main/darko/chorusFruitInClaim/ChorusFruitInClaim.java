package com.darko.main.darko.chorusFruitInClaim;

import com.darko.main.AlttdUtility;
import me.ryanhamshire.GriefPrevention.Claim;
import me.ryanhamshire.GriefPrevention.GriefPrevention;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.scheduler.BukkitRunnable;

public class ChorusFruitInClaim implements Listener {

    @EventHandler(ignoreCancelled = true, priority = EventPriority.LOWEST)
    public void onProjectileHit(ProjectileHitEvent event) {

        if (event.getHitBlock() == null)
            return;
        if (!event.getHitBlock().getType().equals(Material.CHORUS_FLOWER))
            return;

        Projectile arrow = event.getEntity();
        if (!(arrow.getShooter() instanceof Player))
            return;
        Player shooter = (Player) arrow.getShooter();
        Block chorus = event.getHitBlock();

        Claim claim = GriefPrevention.instance.dataStore.getClaimAt(chorus.getLocation(), true, null);
        if (claim == null)
            return;

        if (claim.allowBuild(shooter, chorus.getType()) == null)
            return;

        arrow.remove();
        chorus.setType(Material.AIR);

        new BukkitRunnable() {
            @Override
            public void run() {
                chorus.setType(Material.CHORUS_FLOWER);
                shooter.sendMessage(ChatColor.translateAlternateColorCodes('&', AlttdUtility.getInstance().getConfig().getString("Messages.GriefPreventionNoBuildPermMessage").replace("%player%", claim.getOwnerName())));
            }
        }.runTaskLater(AlttdUtility.getInstance(), 0);
        // I know it's disgusting but cancelling the event is not built in, this is the quickest effective
        // fix I came up with

    }

}
