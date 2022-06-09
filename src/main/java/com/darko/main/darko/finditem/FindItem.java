package com.darko.main.darko.finditem;

import com.darko.main.AlttdUtility;
import com.darko.main.common.Methods;
import com.destroystokyo.paper.ParticleBuilder;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Container;
import org.bukkit.block.ShulkerBox;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BlockStateMeta;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.RayTraceResult;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class FindItem implements CommandExecutor, TabCompleter {

    private static final HashMap<Player, Long> lastTimeUsedCommand = new HashMap<>();

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {

        if (!AlttdUtility.getInstance().getConfig().getBoolean("FeatureToggles.FindItemCommand")) return true;

        if (!(commandSender instanceof Player player)) {
            new Methods().sendConfigMessage(commandSender, "Messages.PlayerOnlyCommandMessage");
            return true;
        }

        if (!player.hasPermission("utility.finditem-bypasscooldown")) {
            if (lastTimeUsedCommand.containsKey(player)) {

                long cooldown = AlttdUtility.getInstance().getConfig().getInt("FindItem.Cooldown") * 1000L;
                long onCooldownUntil = lastTimeUsedCommand.get(player) + cooldown;
                double remainingCooldown = (onCooldownUntil - System.currentTimeMillis()) / 1000d;

                if (remainingCooldown > 0) {
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', AlttdUtility.getInstance().getConfig().getString("Messages.FindItemCommandOnCooldown")).replace("%seconds%", new DecimalFormat("#.#").format(remainingCooldown)));
                    return true;
                }

            }
        }

        if (strings.length < 1) {
            new Methods().sendConfigMessage(commandSender, "Messages.InvalidUsageFindItemCommand");
            return true;
        }

        Material searchedItem = getMaterialFromStringCaseInsensitive(strings[0]);

        if (searchedItem == null) {
            new Methods().sendConfigMessage(commandSender, "Messages.FindItemCommandInvalidItem");
            return true;
        }

        int radius = AlttdUtility.getInstance().getConfig().getInt("FindItem.Radius");

        List<Container> containers = getContainers(player.getLocation(), radius);

        List<Block> blocksContaining = new ArrayList<>();

        containerLoop:
        for (Container container : containers) {

            PlayerInteractEvent playerInteractEvent = new PlayerInteractEvent(player, Action.RIGHT_CLICK_BLOCK, new ItemStack(Material.AIR), container.getBlock(), BlockFace.UP, EquipmentSlot.HAND);
            playerInteractEvent.callEvent();
            if (playerInteractEvent.useInteractedBlock().equals(Event.Result.DENY) || playerInteractEvent.isCancelled()) {
                continue containerLoop;
            }

            Location playerEyeLocation = player.getEyeLocation();

            Location centerOfBlockLocation = container.getBlock().getLocation().toCenterLocation();

            Location[] sidesAndCornersOfBlock = new Location[14];
            sidesAndCornersOfBlock[0] = centerOfBlockLocation.clone().add(0.5, 0.5, 0.5);
            sidesAndCornersOfBlock[1] = centerOfBlockLocation.clone().add(0.5, 0.5, -0.5);
            sidesAndCornersOfBlock[2] = centerOfBlockLocation.clone().add(-0.5, 0.5, 0.5);
            sidesAndCornersOfBlock[3] = centerOfBlockLocation.clone().add(-0.5, 0.5, -0.5);
            sidesAndCornersOfBlock[4] = centerOfBlockLocation.clone().add(0.5, -0.5, 0.5);
            sidesAndCornersOfBlock[5] = centerOfBlockLocation.clone().add(0.5, -0.5, -0.5);
            sidesAndCornersOfBlock[6] = centerOfBlockLocation.clone().add(-0.5, -0.5, 0.5);
            sidesAndCornersOfBlock[7] = centerOfBlockLocation.clone().add(-0.5, -0.5, -0.5);
            sidesAndCornersOfBlock[8] = centerOfBlockLocation.clone().add(0, -0.5, 0);
            sidesAndCornersOfBlock[9] = centerOfBlockLocation.clone().add(0, 0.5, 0);
            sidesAndCornersOfBlock[10] = centerOfBlockLocation.clone().add(0.5, 0, 0);
            sidesAndCornersOfBlock[11] = centerOfBlockLocation.clone().add(-0.5, 0, 0);
            sidesAndCornersOfBlock[12] = centerOfBlockLocation.clone().add(0, 0, 0.5);
            sidesAndCornersOfBlock[13] = centerOfBlockLocation.clone().add(0, 0, -0.5);

            boolean blockHit = false;
            for (Location location : sidesAndCornersOfBlock) {

                Vector rayTraceDirection = location.toVector().subtract(playerEyeLocation.toVector());

                RayTraceResult rayTraceResult = playerEyeLocation.getWorld().rayTraceBlocks(playerEyeLocation, rayTraceDirection, radius);

                if (rayTraceResult == null || rayTraceResult.getHitBlock().equals(container.getBlock())) {
                    blockHit = true;
                    break;
                }

            }
            if (!blockHit) continue containerLoop;

            for (ItemStack itemStack : container.getInventory()) {

                if (itemStack == null) continue;

                if (itemStack.getItemMeta() instanceof BlockStateMeta blockStateMeta && blockStateMeta.getBlockState() instanceof ShulkerBox shulkerBox) {
                    for (ItemStack itemStackInShulker : shulkerBox.getInventory()) {
                        if (itemStackInShulker == null) continue;
                        if (itemStackInShulker.getType().equals(searchedItem)) {
                            blocksContaining.add(container.getBlock());
                            continue containerLoop;
                        }
                    }
                }

                if (itemStack.getType().equals(searchedItem)) {
                    blocksContaining.add(container.getBlock());
                    continue containerLoop;
                }

            }
        }

        player.sendMessage(ChatColor.YELLOW + "Found " + ChatColor.GOLD + blocksContaining.size() + ChatColor.YELLOW + " containers containing " + ChatColor.GOLD + strings[0] + ChatColor.YELLOW + " in a radius of " + ChatColor.GOLD + radius + ChatColor.YELLOW + " blocks.");

        ParticleBuilder particleBuilder = new ParticleBuilder(Particle.REDSTONE);
        particleBuilder.color(255, 255, 255);
        particleBuilder.receivers(player);
        particleBuilder.count(3);

        for (int i = 0; i < 10; i++) {
            new BukkitRunnable() {
                @Override
                public void run() {

                    for (Block block : blocksContaining) {

                        Location centerOfBlock = block.getLocation().clone().toCenterLocation();

                        Location[] cornersOfBlock = new Location[8];
                        cornersOfBlock[0] = centerOfBlock.clone().add(0.5, 0.5, 0.5);
                        cornersOfBlock[1] = centerOfBlock.clone().add(0.5, 0.5, -0.5);
                        cornersOfBlock[2] = centerOfBlock.clone().add(-0.5, 0.5, 0.5);
                        cornersOfBlock[3] = centerOfBlock.clone().add(-0.5, 0.5, -0.5);
                        cornersOfBlock[4] = centerOfBlock.clone().add(0.5, -0.5, 0.5);
                        cornersOfBlock[5] = centerOfBlock.clone().add(0.5, -0.5, -0.5);
                        cornersOfBlock[6] = centerOfBlock.clone().add(-0.5, -0.5, 0.5);
                        cornersOfBlock[7] = centerOfBlock.clone().add(-0.5, -0.5, -0.5);

                        for (Location location : cornersOfBlock) {
                            particleBuilder.location(location);
                            particleBuilder.spawn();
                        }

                    }

                }
            }.runTaskLater(AlttdUtility.getInstance(), i * 10);
        }

        lastTimeUsedCommand.put(player, System.currentTimeMillis());

        return true;

    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {

        if (!AlttdUtility.getInstance().getConfig().getBoolean("FeatureToggles.FindItemCommand")) return null;

        if (!(commandSender instanceof Player)) {
            new Methods().sendConfigMessage(commandSender, "Messages.PlayerOnlyCommandMessage");
            return null;
        }

        if (strings.length == 1) {

            List<String> results = new ArrayList<>();

            for (Material material : Material.values()) {

                if (material.toString().toLowerCase().contains(strings[0].toLowerCase()))
                    results.add(material.toString().toLowerCase());

            }

            return results;

        }

        return null;

    }

    private List<Container> getContainers(Location location, int radius) {

        int xMin = location.getBlockX() - radius, yMin = location.getBlockY() - radius, zMin = location.getBlockZ() - radius;
        int xMax = location.getBlockX() + radius, yMax = location.getBlockY() + radius, zMax = location.getBlockZ() + radius;

        List<Container> containers = new ArrayList<>();

        for (int x = xMin; x <= xMax; x++) {
            for (int y = yMin; y <= yMax; y++) {
                for (int z = zMin; z <= zMax; z++) {

                    Block block = location.getWorld().getBlockAt(x, y, z);

                    if (!(block.getState() instanceof Container container)) continue;

                    containers.add(container);

                }
            }
        }

        return containers;

    }

    private Material getMaterialFromStringCaseInsensitive(String material) {

        for (Material material1 : Material.values()) {
            if (material1.toString().equalsIgnoreCase(material)) return material1;
        }

        return null;

    }

}
