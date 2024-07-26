package com.darko.main.darko.finditem;

import com.darko.main.AlttdUtility;
import com.darko.main.common.Methods;
import com.destroystokyo.paper.ParticleBuilder;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.World;
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
import org.bukkit.inventory.DoubleChestInventory;
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
import java.util.HashSet;
import java.util.List;

public class FindItem implements CommandExecutor, TabCompleter {

    private static final HashMap<Player, Long> lastTimeUsedCommand = new HashMap<>();

    private final int defaultRadius = AlttdUtility.getInstance().getConfig().getInt("FindItem.Radius");
    private final long allowedNanosecondsPerTick = AlttdUtility.getInstance().getConfig().getInt("FindItem.AllowedMilisecondsPerTick") * 1000000;

    private Player player = null;
    private HashSet<Material> searchedMaterials = null;
    private int radius;

    private Location playerEyeLocation = null;

    private final ArrayList<Container> containers = new ArrayList<>();
    private final ArrayList<Container> containersToRemove = new ArrayList<>();

    private int processStepCount = 0;
    private int processProgressTracker = 0;

    private long thisTickStartTime;

    private int totalItemsFound = 0;

    // Step 0 - finding all containers in area
    private boolean stepZeroFirstRun = true;
    private int stepZeroXMin, stepZeroYMin, stepZeroZMin, stepZeroXMax, stepZeroYMax, stepZeroZMax;

    // Step 1 - remove unseeable containers

    // Step 2 - remove unopenable containers

    // Step 3 - remove not containing containers
    private HashSet<String> checkedDoubleChests = new HashSet<>();

    // Step 4 - searching done, finish up

    public FindItem() {}

    public FindItem(Player player, HashSet<Material> searchedMaterials, int radius) {

        this.player = player;
        this.searchedMaterials = searchedMaterials;
        this.radius = radius;

        this.playerEyeLocation = player.getEyeLocation().clone();

        startSearchingProcess();

    }

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {

        if (!AlttdUtility.getInstance().getConfig().getBoolean("FeatureToggles.FindItemCommand"))
            return true;

        if (!(commandSender instanceof Player player)) {
            Methods.sendConfigMessage(commandSender, "Messages.PlayerOnlyCommandMessage");
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
        lastTimeUsedCommand.put(player, System.currentTimeMillis());

        if (strings.length < 1) {
            Methods.sendConfigMessage(commandSender, "Messages.InvalidUsageFindItemCommand");
            return true;
        }

        HashSet<Material> searchedMaterials = getMaterialsHashSet(strings[0]);

        if (searchedMaterials.isEmpty()) {
            Methods.sendConfigMessage(commandSender, "Messages.FindItemCommandInvalidItem");
            return true;
        }

        int radius = defaultRadius;

        if (commandSender.hasPermission("utility.finditem-customradius")) {

            try {

                radius = Math.max(1, Math.min(Integer.parseInt(strings[1]), 100));

            } catch (Throwable ignored) {
            }

        }

        new FindItem(player, searchedMaterials, radius);

        return true;

    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {

        if (!AlttdUtility.getInstance().getConfig().getBoolean("FeatureToggles.FindItemCommand"))
            return null;

        if (!(commandSender instanceof Player)) {
            Methods.sendConfigMessage(commandSender, "Messages.PlayerOnlyCommandMessage");
            return null;
        }

        if (strings.length == 1) {

            String lastMaterial = strings[0];

            if (lastMaterial.contains(","))
                lastMaterial = lastMaterial.substring(lastMaterial.lastIndexOf(",") + 1);

            List<String> results = new ArrayList<>();

            for (Material material : Material.values()) {

                if (material.toString().toLowerCase().contains(lastMaterial.toLowerCase()))
                    results.add(strings[0].substring(0, strings[0].lastIndexOf(lastMaterial)) + material.toString().toLowerCase());

            }

            return results;

        }

        return null;

    }

    private void startSearchingProcess() {

        new BukkitRunnable() {
            @Override
            public void run() {

                thisTickStartTime = System.nanoTime();

                if (processStepCount == 0) {
                    findAreaContainers();
                }

                if (processStepCount == 1) {
                    removeUnseeableContainers();
                }

                if (processStepCount == 2) {
                    removeUnopenableContainers();
                }

                if (processStepCount == 3) {
                    removeNotContainingContainers();
                }

                if (processStepCount == 4) {
                    finishUp();
                } else {
                    startSearchingProcess();
                }

            }
        }.runTaskLater(AlttdUtility.getInstance(), 1);

    }

    private void findAreaContainers() {

        if (stepZeroFirstRun) {
            stepZeroXMin = playerEyeLocation.getBlockX() - radius;
            stepZeroYMin = playerEyeLocation.getBlockY() - radius;
            stepZeroZMin = playerEyeLocation.getBlockZ() - radius;
            stepZeroXMax = playerEyeLocation.getBlockX() + radius;
            stepZeroYMax = playerEyeLocation.getBlockY() + radius;
            stepZeroZMax = playerEyeLocation.getBlockZ() + radius;
            stepZeroFirstRun = false;
        }

        World world = playerEyeLocation.getWorld();

        for (; stepZeroXMin <= stepZeroXMax; stepZeroXMin++, stepZeroYMin = playerEyeLocation.getBlockY() - radius) {
            for (; stepZeroYMin <= stepZeroYMax; stepZeroYMin++, stepZeroZMin = playerEyeLocation.getBlockZ() - radius) {
                for (; stepZeroZMin <= stepZeroZMax; stepZeroZMin++) {

                    if (isOutOfThisTickTime())
                        return;

                    Block block = world.getBlockAt(stepZeroXMin, stepZeroYMin, stepZeroZMin);

                    if (!block.getType().equals(Material.AIR) && block.getState() instanceof Container container)
                        containers.add(container);

                }
            }
        }

        processStepCount++;

    }

    private void removeUnseeableContainers() {

        if (radius != defaultRadius) {
            processStepCount++;
            return;
        }

        for (; processProgressTracker < containers.size(); processProgressTracker++) {

            if (isOutOfThisTickTime())
                return;

            Container container = containers.get(processProgressTracker);

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

            if (!blockHit) {
                containersToRemove.add(container);
            }

        }

        performEndOfStep();

    }

    private void removeUnopenableContainers() {

        if (radius != defaultRadius) {
            processStepCount++;
            return;
        }

        for (; processProgressTracker < containers.size(); processProgressTracker++) {

            if (isOutOfThisTickTime())
                return;

            Container container = containers.get(processProgressTracker);

            PlayerInteractEvent playerInteractEvent = new PlayerInteractEvent(player, Action.RIGHT_CLICK_BLOCK, new ItemStack(Material.AIR), container.getBlock(), BlockFace.UP, EquipmentSlot.HAND);
            playerInteractEvent.callEvent();

            if (playerInteractEvent.useInteractedBlock().equals(Event.Result.DENY) || playerInteractEvent.isCancelled()) {
                containersToRemove.add(container);
            }

        }

        performEndOfStep();

    }

    private void removeNotContainingContainers() {

        for (; processProgressTracker < containers.size(); processProgressTracker++) {

            if (isOutOfThisTickTime())
                return;

            Container container = containers.get(processProgressTracker);

            boolean isDoubleChest = container.getInventory() instanceof DoubleChestInventory;

            String doubleChestLocationString = null;

            if (isDoubleChest) {

                doubleChestLocationString = ((DoubleChestInventory) container.getInventory()).getHolder().getLocation().toString();

            }

            boolean itemFound = false;

            for (ItemStack itemStack : container.getInventory()) {

                if (itemStack == null)
                    continue;

                if (searchedMaterials.contains(itemStack.getType())) {

                    if (!isDoubleChest || !checkedDoubleChests.contains(doubleChestLocationString))
                        totalItemsFound += itemStack.getAmount();

                    itemFound = true;

                }

                if (itemStack.getItemMeta() instanceof BlockStateMeta blockStateMeta && blockStateMeta.getBlockState() instanceof ShulkerBox shulkerBox) {
                    for (ItemStack itemStackInShulker : shulkerBox.getInventory()) {

                        if (itemStackInShulker == null)
                            continue;

                        if (searchedMaterials.contains(itemStackInShulker.getType())) {

                            if (!isDoubleChest || !checkedDoubleChests.contains(doubleChestLocationString))
                                totalItemsFound += itemStackInShulker.getAmount();

                            itemFound = true;

                        }

                    }
                }

            }

            if (isDoubleChest) {
                checkedDoubleChests.add(doubleChestLocationString);
            }

            if (!itemFound) {
                containersToRemove.add(container);
            }

        }

        performEndOfStep();

    }

    private void finishUp() {

        String containerOrContainers = (containers.size() == 1) ? "container" : "containers";

        player.sendMessage(ChatColor.YELLOW + "Found " + ChatColor.GOLD + totalItemsFound + " " + getMaterialsString(searchedMaterials) + ChatColor.YELLOW + " in " + ChatColor.GOLD + containers.size() + ChatColor.YELLOW + " " + containerOrContainers + " in a radius of " + ChatColor.GOLD + radius + ChatColor.YELLOW + " blocks.");

        ParticleBuilder particleBuilder = new ParticleBuilder(Particle.DUST);
        particleBuilder.color(255, 255, 255);
        particleBuilder.receivers(player);
        particleBuilder.count(1);

        for (int i = 0; i < 10; i++) {
            new BukkitRunnable() {
                @Override
                public void run() {

                    for (Container container : containers) {

                        Block block = container.getBlock();

                        Location centerOfBlock = block.getLocation().clone().toCenterLocation();

                        ArrayList<Location> particleLocations = new ArrayList<>();

                        // Corners

                        particleLocations.add(centerOfBlock.clone().add(0.5, 0.5, 0.5));
                        particleLocations.add(centerOfBlock.clone().add(0.5, 0.5, -0.5));
                        particleLocations.add(centerOfBlock.clone().add(-0.5, 0.5, 0.5));
                        particleLocations.add(centerOfBlock.clone().add(-0.5, 0.5, -0.5));
                        particleLocations.add(centerOfBlock.clone().add(0.5, -0.5, 0.5));
                        particleLocations.add(centerOfBlock.clone().add(0.5, -0.5, -0.5));
                        particleLocations.add(centerOfBlock.clone().add(-0.5, -0.5, 0.5));
                        particleLocations.add(centerOfBlock.clone().add(-0.5, -0.5, -0.5));

                        // Face centers

                        Location upFace = centerOfBlock.clone().add(0, 0.5, 0);
                        Location downFace = centerOfBlock.clone().add(0, -0.5, 0);
                        Location northFace = centerOfBlock.clone().add(0, 0, -0.5);
                        Location southFace = centerOfBlock.clone().add(0, 0, 0.5);
                        Location westFace = centerOfBlock.clone().add(-0.5, 0, 0);
                        Location eastFace = centerOfBlock.clone().add(0.5, 0, 0);

                        // 9 on faces

                        for (double x = -0.25; x <= 0.25; x += 0.25) {
                            for (double z = -0.25; z <= 0.25; z += 0.25) {
                                particleLocations.add(upFace.clone().add(x, 0.05, z));
                            }
                        }
                        for (double x = -0.25; x <= 0.25; x += 0.25) {
                            for (double z = -0.25; z <= 0.25; z += 0.25) {
                                particleLocations.add(downFace.clone().add(x, -0.05, z));
                            }
                        }
                        for (double x = -0.25; x <= 0.25; x += 0.25) {
                            for (double y = -0.25; y <= 0.25; y += 0.25) {
                                particleLocations.add(northFace.clone().add(x, y, -0.05));
                            }
                        }
                        for (double x = -0.25; x <= 0.25; x += 0.25) {
                            for (double y = -0.25; y <= 0.25; y += 0.25) {
                                particleLocations.add(southFace.clone().add(x, y, 0.05));
                            }
                        }
                        for (double y = -0.25; y <= 0.25; y += 0.25) {
                            for (double z = -0.25; z <= 0.25; z += 0.25) {
                                particleLocations.add(westFace.clone().add(-0.05, y, z));
                            }
                        }
                        for (double y = -0.25; y <= 0.25; y += 0.25) {
                            for (double z = -0.25; z <= 0.25; z += 0.25) {
                                particleLocations.add(eastFace.clone().add(0.05, y, z));
                            }
                        }

                        for (Location location : particleLocations) {
                            particleBuilder.location(location);
                            particleBuilder.spawn();
                        }

                    }

                }
            }.runTaskLater(AlttdUtility.getInstance(), i * 10);
        }

    }

    private void performEndOfStep() {

        containers.removeAll(containersToRemove);
        containersToRemove.clear();
        processStepCount++;
        processProgressTracker = 0;

    }

    private boolean isOutOfThisTickTime() {

        return thisTickStartTime + allowedNanosecondsPerTick < System.nanoTime();

    }

    private Material getMaterialFromString(String material) {

        try {
            return Material.valueOf(material.toUpperCase());
        } catch (Throwable throwable) {
            return null;
        }

    }

    private HashSet<Material> getMaterialsHashSet(String materialsString) {

        HashSet<Material> materialsHashSet = new HashSet<>();

        for (String materialString : materialsString.split(",")) {

            Material material = getMaterialFromString(materialString);

            if (material != null)
                materialsHashSet.add(material);

        }

        while (materialsHashSet.size() > AlttdUtility.getInstance().getConfig().getInt("FindItem.ItemLimit"))
            materialsHashSet.remove(materialsHashSet.toArray()[0]);

        return materialsHashSet;

    }

    private String getMaterialsString(HashSet<Material> materials) {

        StringBuilder string = new StringBuilder();

        if (materials.isEmpty())
            return string.toString();

        for (Material material : materials) {
            string.append(material.toString().toLowerCase()).append(", ");
        }

        string = new StringBuilder(string.substring(0, string.length() - 2));

        return string.toString();

    }

}
