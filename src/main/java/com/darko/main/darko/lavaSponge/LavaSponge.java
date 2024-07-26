package com.darko.main.darko.lavaSponge;

import com.darko.main.common.API.APIs;
import com.darko.main.AlttdUtility;
import com.destroystokyo.paper.ParticleBuilder;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldguard.LocalPlayer;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.flags.Flags;
import com.sk89q.worldguard.protection.regions.RegionContainer;
import com.sk89q.worldguard.protection.regions.RegionQuery;
import me.ryanhamshire.GriefPrevention.Claim;
import me.ryanhamshire.GriefPrevention.GriefPrevention;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.RayTraceResult;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class LavaSponge implements Listener {

    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
    public void onBlockPlace(BlockPlaceEvent event) {

        Block spongeBlock = event.getBlock();
        Player player = event.getPlayer();

        if (!spongeBlock.getType().equals(Material.SPONGE) && !spongeBlock.getType().equals(Material.WET_SPONGE))
            return;

        if (!checkOneBlockAroundSponge(spongeBlock))
            return;

        String typeDryWet;

        if (spongeBlock.getType().equals(Material.SPONGE))
            typeDryWet = "dry";
        else
            typeDryWet = "wet";

        Integer radius;
        Integer absorbLimit;

        if (typeDryWet.equals("dry"))
            radius = AlttdUtility.getInstance().getConfig().getInt("LavaSponge.DrySpongeRange");
        else
            radius = AlttdUtility.getInstance().getConfig().getInt("LavaSponge.WetSpongeRange");

        if (typeDryWet.equals("dry"))
            absorbLimit = AlttdUtility.getInstance().getConfig().getInt("LavaSponge.DrySpongeAbsorbLimit");
        else
            absorbLimit = AlttdUtility.getInstance().getConfig().getInt("LavaSponge.WetSpongeAbsorbLimit");

        List<Block> blocksToDelete = new ArrayList<>();

        loop: for (Integer i = 1; i <= radius; i++) {

            for (Integer x = -i; x <= i; x++) {
                for (Integer y = -i; y <= i; y++) {
                    for (Integer z = -i; z <= i; z++) {

                        Block lavaBlock = spongeBlock.getWorld().getBlockAt(spongeBlock.getLocation().clone().add(x, y, z));

                        if (!checkBlock(spongeBlock, lavaBlock, player))
                            continue;

                        if (!blocksToDelete.contains(lavaBlock))
                            blocksToDelete.add(lavaBlock);

                        if (blocksToDelete.size() >= absorbLimit)
                            break loop;

                    }
                }
            }

        }

        blocksToDelete.add(spongeBlock);

        for (Block block : blocksToDelete) {

            BlockData blockDataCopy = block.getBlockData();

            new BukkitRunnable() {
                @Override
                public void run() {

                    ParticleBuilder particleBuilderSmoke = new ParticleBuilder(Particle.DUST);
                    particleBuilderSmoke.color(59, 65, 74);
                    for (Integer i = 0; i < 20; i++) {
                        particleBuilderSmoke.location(block.getLocation().clone().add(getRandomParticleOffset() + 0.5d, getRandomParticleOffset() + 0.5d, getRandomParticleOffset() + 0.5d));
                        particleBuilderSmoke.count(1);
                        particleBuilderSmoke.spawn();
                    }


                    ParticleBuilder particleBuilderBlockCrack = new ParticleBuilder(Particle.BLOCK);
                    particleBuilderBlockCrack.data(blockDataCopy);
                    for (Integer i = 0; i < 10; i++) {
                        particleBuilderBlockCrack.location(block.getLocation().clone().add(getRandomParticleOffset() + 0.5d, getRandomParticleOffset() + 0.5d, getRandomParticleOffset() + 0.5d));
                        particleBuilderBlockCrack.count(1);
                        particleBuilderBlockCrack.spawn();
                    }

                }
            }.runTaskAsynchronously(AlttdUtility.getInstance());

            block.setType(Material.AIR);

        }

    }

    Double getRandomParticleOffset() {

        Double start = -0.5d;
        Double end = 0.5d;
        Double random = new Random().nextDouble();

        return start + (random * (end - start));

    }

    Boolean checkOneBlockAroundSponge(Block block) {

        for (Integer x = -1; x <= 1; x++) {
            for (Integer y = -1; y <= 1; y++) {
                for (Integer z = -1; z <= 1; z++) {

                    if (block.getLocation().getWorld().getBlockAt(block.getLocation().clone().add(x, y, z)).getType().equals(Material.LAVA))
                        return true;

                }
            }
        }

        return false;

    }

    Boolean checkBlock(Block spongeBlock, Block lavaBlock, Player player) {


        if (!lavaBlock.getType().equals(Material.LAVA))
            return false;

        if (!claimCheck(lavaBlock, player))
            return false;

        if (!regionCheck(lavaBlock, player))
            return false;

        Location lavaBlockCenter = lavaBlock.getLocation().clone().add(0.5, 0.5, 0.5);
        Location spongeBlockCenter = spongeBlock.getLocation().clone().add(0.5, 0.5, 0.5);

        Vector vectorBetweenPoints = spongeBlockCenter.toVector().subtract(lavaBlockCenter.toVector());

        RayTraceResult result = lavaBlock.getWorld().rayTraceBlocks(lavaBlockCenter, vectorBetweenPoints, 100, FluidCollisionMode.NEVER, false);

        if (result == null)
            return false;
        if (result.getHitPosition() == null)
            return false;
        return !(result.getHitPosition().toLocation(lavaBlock.getWorld()).distance(spongeBlockCenter) > 1);

    }

    Boolean claimCheck(Block block, Player player) {

        if (!APIs.isGriefPreventionFound()) {
            return true;
        }

        Claim claim = GriefPrevention.instance.dataStore.getClaimAt(block.getLocation(), false, null);

        if (claim == null)
            return true;

        return claim.allowBuild(player, block.getType()) == null;

    }

    Boolean regionCheck(Block block, Player player) {

        if (!APIs.isWorldGuardFound()) {
            return true;
        }

        com.sk89q.worldedit.util.Location blockLocation = BukkitAdapter.adapt(block.getLocation());
        LocalPlayer localPlayer = WorldGuardPlugin.inst().wrapPlayer(player);
        RegionContainer container = WorldGuard.getInstance().getPlatform().getRegionContainer();
        RegionQuery query = container.createQuery();

        return query.testState(blockLocation, localPlayer, Flags.BUILD);

    }

}
