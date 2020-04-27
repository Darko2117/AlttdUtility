package com.darko.main.utilities.flags;

import com.darko.main.API.APIs;
import com.destroystokyo.paper.event.entity.EntityPathfindEvent;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.flags.StateFlag;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import com.sk89q.worldguard.protection.regions.RegionContainer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class onGuardianPathfind implements Listener {

    @EventHandler
    public void onGuardianPathfind(EntityPathfindEvent e) {

        Entity ent = e.getEntity();

        if (ent.getType().equals(EntityType.GUARDIAN)) {

            if (APIs.WorldGuardFound) {

                com.sk89q.worldedit.world.World locationWorld = BukkitAdapter.adapt(ent.getLocation().getWorld());
                BlockVector3 location = BlockVector3.at(ent.getLocation().getBlockX(), ent.getLocation().getBlockY(), ent.getLocation().getBlockZ());
                RegionContainer container = WorldGuard.getInstance().getPlatform().getRegionContainer();
                RegionManager regions = container.get(locationWorld);
                ApplicableRegionSet set = regions.getApplicableRegions(location);

                for (ProtectedRegion r : set) {
                    if (r.getFlags().containsKey(Flags.ALLOW_GUARDIAN_PATHFINDING)) {
                        if (r.getFlags().get(Flags.ALLOW_GUARDIAN_PATHFINDING).equals(StateFlag.State.DENY)) {
                            e.setCancelled(true);
                        }
                    }
                }
            }
        }
    }
}
