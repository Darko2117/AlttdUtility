package com.darko.main.darko.sit;

import com.darko.main.common.API.APIs;
import com.Zrips.CMI.events.CMIAsyncPlayerTeleportEvent;
import com.darko.main.AlttdUtility;
import com.darko.main.common.BukkitTasksCache;
import com.darko.main.common.Methods;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldguard.LocalPlayer;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.regions.RegionContainer;
import com.sk89q.worldguard.protection.regions.RegionQuery;
import me.ryanhamshire.GriefPrevention.Claim;
import me.ryanhamshire.GriefPrevention.GriefPrevention;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDismountEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.world.ChunkLoadEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Sit implements CommandExecutor, Listener {

    private static final HashMap<Location, Entity> aliveSeats = new HashMap<>();
    private static final String seatName = "There is a 35 character limit on a name tag.";

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (!AlttdUtility.getInstance().getConfig().getBoolean("FeatureToggles.Sit"))
            return true;

        if (!(sender instanceof Player player)) {
            Methods.sendConfigMessage(sender, "Messages.PlayerOnlyCommandMessage");
            return true;
        }

        Location playerLocation = player.getLocation().clone();

        Block block = playerLocation.getWorld().getBlockAt(playerLocation.subtract(0, 0.00001, 0));

        if (!blockAboveCheck(block)) {
            Methods.sendConfigMessage(player, "Messages.SeatInvalidBlockAbove");
            return true;
        }
        if (!blockBelowCheck(block)) {
            Methods.sendConfigMessage(player, "Messages.SeatInvalidBlockBelow");
            return true;
        }
        if (!((Entity) player).isOnGround()) {
            Methods.sendConfigMessage(player, "Messages.SitCommandNotOnGroundMessage");
            return true;
        }
        if (!occupiedCheck(player)) {
            Methods.sendConfigMessage(player, "Messages.SeatOccupiedMessage");
            return false;
        }
        if (getSmallestNearCenterBlockHeight(block) == -1) {
            Methods.sendConfigMessage(player, "Messages.SeatInvalidBlock");
            return true;
        }
        // //if (!claimCheck(player, block)) return true;
        if (!regionCheck(player, block)) {
            Methods.sendConfigMessage(player, "Messages.SeatNoRegionPerm");
            return false;
        }

        Location location = block.getLocation().add(0.5, 0, 0.5);
        location.setY(getSmallestNearCenterBlockHeight(block) - 1.95);

        location.setYaw(player.getLocation().getYaw());

        ArmorStand seat = (ArmorStand) Bukkit.getWorld(player.getWorld().getName()).spawnEntity(location, EntityType.ARMOR_STAND);
        seat.setCustomName(seatName);
        seat.setVisible(false);
        seat.setAI(false);
        seat.setInvulnerable(true);
        seat.setGravity(false);
        seat.setCanMove(false);
        seat.setCanTick(false);
        seat.setCollidable(false);
        seat.addPassenger(player);

        aliveSeats.put(seat.getWorld().getBlockAt(seat.getLocation().clone().add(-0.5, 1.75 - 0.00001, -0.5)).getLocation(), seat);

        return true;

    }

    // Deleting seats if they don't get properly deleted when dismounting
    @EventHandler
    public void onChunkLoad(ChunkLoadEvent event) {

        Chunk chunk = event.getChunk();

        for (Entity entity : chunk.getEntities()) {

            if (entity instanceof ArmorStand) {

                ArmorStand armorStand = (ArmorStand) entity;

                if (armorStand.getCustomName() != null && armorStand.getCustomName().equals(seatName)) {

                    armorStand.remove();

                    AlttdUtility.getInstance().getLogger().info("Bugged armorstand from /sit removed when chunk loading at " + armorStand.getLocation());

                }

            }

        }

    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
    public void onBlockBreak(BlockBreakEvent event) {

        if (aliveSeats.containsKey(event.getBlock().getLocation())) {
            if (event.getPlayer().hasPermission("utility.forcedismount")) {
                aliveSeats.get(event.getBlock().getLocation()).eject();
            } else {
                event.setCancelled(true);
            }
        }

    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
    public void onPlayerInteractAtEntity(PlayerInteractAtEntityEvent event) {

        if (aliveSeats.containsValue(event.getRightClicked())) {
            event.setCancelled(true);
        }

    }

    @EventHandler(ignoreCancelled = false, priority = EventPriority.HIGHEST)
    public void onEntityDismount(EntityDismountEvent event) {

        Entity dismounted = event.getDismounted();
        Entity entity = event.getEntity();

        if (dismounted.getCustomName() == null)
            return;
        if (!dismounted.getCustomName().equals(seatName))
            return;

        for (Map.Entry<Location, Entity> entry : aliveSeats.entrySet()) {
            if (entry.getValue().equals(dismounted)) {
                aliveSeats.remove(entry.getKey());
                break;
            }
        }
        dismounted.remove();

        new BukkitRunnable() {
            public void run() {
                if (entity.getVehicle() == null) {
                    Location dismountLocation = entity.getLocation();
                    dismountLocation.setY(dismounted.getLocation().getY() + 1.95);
                    entity.teleport(dismountLocation);
                }
            }
        }.runTaskLater(AlttdUtility.getInstance(), 1);

    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
    public void onPlayerQuit(PlayerQuitEvent event) {

        Player player = event.getPlayer();

        if (!player.isInsideVehicle())
            return;

        if (player.getVehicle().getCustomName() == null)
            return;

        if (player.getVehicle().getCustomName().equals(seatName)) {
            player.getVehicle().eject();
        }

    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
    public void onCMIPlayerTeleportEvent(CMIAsyncPlayerTeleportEvent cmiAsyncPlayerTeleportEvent) {

        Player player = cmiAsyncPlayerTeleportEvent.getPlayer();

        if (player.isInsideVehicle()) {

            cmiAsyncPlayerTeleportEvent.setCancelled(true);
            player.sendMessage(ChatColor.WHITE + "You " + ChatColor.RED + "can't" + ChatColor.WHITE + " teleport while sitting.");

        }

    }

    // @EventHandler(ignoreCancelled = true, priority = EventPriority.LOWEST)
    // public void onPlayerCommandPreprocess(PlayerCommandPreprocessEvent event) {
    //
    // Player player = event.getPlayer();
    //
    // if (!player.isInsideVehicle()) return;
    // if (player.getVehicle().getCustomName() == null) return;
    // if (!player.getVehicle().getCustomName().equals(seatName)) return;
    //
    // String command = event.getMessage();
    // String[] tpCommands = {"/tp", "/ptp", "/spawn", "/warp", "/home", "/back"};
    //
    // for (String tpCommand : tpCommands) {
    // if (command.startsWith(tpCommand)) {
    // event.setCancelled(true);
    // player.sendMessage(ChatColor.WHITE + "You " + ChatColor.RED + "can't" + ChatColor.WHITE + "
    // teleport while sitting.");
    // }
    // }
    //
    // }

    boolean blockAboveCheck(Block block) {

        Location blockLocation = block.getLocation().clone();

        Block blockAbove = blockLocation.getWorld().getBlockAt(blockLocation.add(0, 1, 0));

        return blockAbove.getBlockData().getMaterial().isAir();

    }

    boolean blockBelowCheck(Block block) {

        Location blockLocation = block.getLocation().clone();

        Block blockBelow = blockLocation.getWorld().getBlockAt(blockLocation.subtract(0, 1, 0));

        return blockBelow.getType().isSolid();

    }

    boolean occupiedCheck(Player player) {

        return !aliveSeats.containsKey(player.getLocation());

    }

    Boolean claimCheck(Player player, Block block) {

        if (!APIs.isGriefPreventionFound()) {
            return true;
        }

        Claim claim = GriefPrevention.instance.dataStore.getClaimAt(block.getLocation(), true, null);

        if (claim == null)
            return true;

        if (claim.allowAccess(player) == null) {
            return true;
        } else {
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', AlttdUtility.getInstance().getConfig().getString("Messages.SeatNoClaimPerm").replace("%player%", claim.getOwnerName())));
            return false;
        }

    }

    Boolean regionCheck(Player player, Block block) {

        if (!APIs.isWorldGuardFound()) {
            return true;
        }

        com.sk89q.worldedit.util.Location blockLocation = BukkitAdapter.adapt(block.getLocation());
        LocalPlayer localPlayer = WorldGuardPlugin.inst().wrapPlayer(player);
        RegionContainer container = WorldGuard.getInstance().getPlatform().getRegionContainer();
        RegionQuery query = container.createQuery();
        return query.testState(blockLocation, localPlayer, com.darko.main.darko.flags.Flags.SIT);

    }

    double getSmallestNearCenterBlockHeight(Block block) {

        Location startingPoint = block.getLocation().clone().add(0.5, 1.01, 0.5);
        Vector directionVector = new Vector(0, -1, 0);

        List<Double> points = new ArrayList<>();

        if (startingPoint.getWorld().rayTraceBlocks(startingPoint, directionVector, 1, FluidCollisionMode.NEVER, true) != null) {
            points.add(startingPoint.getWorld().rayTraceBlocks(startingPoint, directionVector, 1, FluidCollisionMode.NEVER, true).getHitPosition().getY());
        }

        for (double x = -0.25; x <= 0.25; x += 0.5) {
            for (double z = -0.25; z <= 0.25; z += 0.5) {

                Location startingPointTemp = startingPoint.clone();

                startingPointTemp.add(x, 0, z);

                if (startingPointTemp.getWorld().rayTraceBlocks(startingPointTemp, directionVector, 1, FluidCollisionMode.NEVER, true) != null) {
                    points.add(startingPointTemp.getWorld().rayTraceBlocks(startingPointTemp, directionVector, 1, FluidCollisionMode.NEVER, true).getHitPosition().getY());
                }

            }
        }

        double smallest = -1;

        for (double point : points) {
            if (smallest == -1)
                smallest = point;
            if (point < smallest)
                smallest = point;
        }

        return smallest;

    }

    public static void startCheckingSeats() {
        BukkitTasksCache.addTask(new BukkitRunnable() {
            @Override
            public void run() {

                if (aliveSeats.isEmpty())
                    return;

                for (Player player : Bukkit.getOnlinePlayers()) {

                    if (!player.isInsideVehicle())
                        continue;
                    if (player.getVehicle().getCustomName() == null)
                        continue;
                    if (!player.getVehicle().getCustomName().equals(seatName))
                        continue;

                    ArmorStand seat = (ArmorStand) player.getVehicle();

                    seat.setRotation(player.getLocation().getYaw(), seat.getLocation().getPitch());

                    if (seat.getLocation().getWorld().getBlockAt(seat.getLocation().clone().add(0, 1.75 - 0.00001, 0)).getType().equals(Material.AIR))
                        seat.eject();

                }

            }
        }.runTaskTimer(AlttdUtility.getInstance(), 20, 20));
    }

}
