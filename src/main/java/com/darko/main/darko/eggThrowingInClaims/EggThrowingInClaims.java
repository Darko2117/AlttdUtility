package com.darko.main.darko.eggThrowingInClaims;

import com.darko.main.AlttdUtility;
import me.ryanhamshire.GriefPrevention.Claim;
import me.ryanhamshire.GriefPrevention.GriefPrevention;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileLaunchEvent;

public class EggThrowingInClaims implements Listener {

    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
    public void onProjectileLaunch(ProjectileLaunchEvent event) {

        if (!(event.getEntity().getShooter() instanceof Player))
            return;

        if (!event.getEntity().getType().equals(EntityType.EGG))
            return;

        Player shooter = (Player) event.getEntity().getShooter();
        Claim claim = GriefPrevention.instance.dataStore.getClaimAt(shooter.getLocation(), true, null);

        if (claim == null)
            return;

        if (claim.allowBuild(shooter, Material.STONE) == null)
            return;

        shooter.sendMessage(ChatColor.translateAlternateColorCodes('&', AlttdUtility.getInstance().getConfig().getString("Messages.SeatNoClaimPerm")).replace("%player%", claim.getOwnerName()));
        event.setCancelled(true);

    }

}
