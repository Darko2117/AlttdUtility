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

    private final int radius = AlttdUtility.getInstance().getConfig().getInt("FindItem.Radius");
    private final int allowedMilisecondsPerTick = AlttdUtility.getInstance().getConfig().getInt("FindItem.AllowedMilisecondsPerTick");

    private Player player = null;
    private List<Material> searchedMaterials = null;

    private Location playerEyeLocation = null;

    private final List<Container> containers = new ArrayList<>();

    private int processStepCount = 0;
    private long thisTickStartTime;

    // Step 0 - finding all containers in area
    private boolean stepZeroFirstRun = true;
    private int stepZeroXMin, stepZeroYMin, stepZeroZMin, stepZeroXMax, stepZeroYMax, stepZeroZMax;

    // Step 1 - remove unseeable containers
    private final List<Container> stepOneToRemove = new ArrayList<>();
    private int stepOneLastCheckedIndex = 0;

    // Step 2 - remove unopenable containers
    private final List<Container> stepTwoToRemove = new ArrayList<>();
    private int stepTwoLastCheckedIndex = 0;

    // Step 3 - remove not containing containers
    private final List<Container> stepThreeToRemove = new ArrayList<>();
    private int stepThreeLastCheckedIndex = 0;

    // Step 4 - searching done, finish up

    public FindItem() {}

    public FindItem(Player player, List<Material> searchedMaterials) {

        this.player = player;
        this.searchedMaterials = searchedMaterials;

        this.playerEyeLocation = player.getEyeLocation().clone();

        startSearchingProcess();

    }

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {

        if (!AlttdUtility.getInstance().getConfig().getBoolean("FeatureToggles.FindItemCommand"))
            return true;

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
        lastTimeUsedCommand.put(player, System.currentTimeMillis());

        if (strings.length < 1) {
            new Methods().sendConfigMessage(commandSender, "Messages.InvalidUsageFindItemCommand");
            return true;
        }

        List<Material> searchedMaterials = getMaterialsList(strings[0]);

        if (searchedMaterials.isEmpty()) {
            new Methods().sendConfigMessage(commandSender, "Messages.FindItemCommandInvalidItem");
            return true;
        }

        new FindItem(player, searchedMaterials);

        return true;

    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {

        if (!AlttdUtility.getInstance().getConfig().getBoolean("FeatureToggles.FindItemCommand"))
            return null;

        if (!(commandSender instanceof Player)) {
            new Methods().sendConfigMessage(commandSender, "Messages.PlayerOnlyCommandMessage");
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

                thisTickStartTime = System.currentTimeMillis();

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

    private boolean outOfThisTickTime() {

        return thisTickStartTime + allowedMilisecondsPerTick < System.currentTimeMillis();

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

        for (; stepZeroXMin <= stepZeroXMax; stepZeroXMin++, stepZeroYMin = playerEyeLocation.getBlockY() - radius) {
            for (; stepZeroYMin <= stepZeroYMax; stepZeroYMin++, stepZeroZMin = playerEyeLocation.getBlockZ() - radius) {
                for (; stepZeroZMin <= stepZeroZMax; stepZeroZMin++) {

                    Block block = playerEyeLocation.getWorld().getBlockAt(stepZeroXMin, stepZeroYMin, stepZeroZMin);

                    if (block.getState() instanceof Container container)
                        containers.add(container);

                    if (outOfThisTickTime()) {
                        stepZeroZMin++;
                        return;
                    }

                }
            }
        }

        processStepCount++;

    }

    private void removeUnseeableContainers() {

        for (; stepOneLastCheckedIndex < containers.size(); stepOneLastCheckedIndex++) {

            Container container = containers.get(stepOneLastCheckedIndex);

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
                stepOneToRemove.add(container);
            }

            if (outOfThisTickTime())
                return;

        }

        containers.removeAll(stepOneToRemove);
        processStepCount++;

    }

    private void removeUnopenableContainers() {

        for (; stepTwoLastCheckedIndex < containers.size(); stepTwoLastCheckedIndex++) {

            Container container = containers.get(stepTwoLastCheckedIndex);

            PlayerInteractEvent playerInteractEvent = new PlayerInteractEvent(player, Action.RIGHT_CLICK_BLOCK, new ItemStack(Material.AIR), container.getBlock(), BlockFace.UP, EquipmentSlot.HAND);
            playerInteractEvent.callEvent();

            if (playerInteractEvent.useInteractedBlock().equals(Event.Result.DENY) || playerInteractEvent.isCancelled()) {
                stepTwoToRemove.add(container);
            }

            if (outOfThisTickTime())
                return;

        }

        containers.removeAll(stepTwoToRemove);
        processStepCount++;

    }

    private void removeNotContainingContainers() {

        for (; stepThreeLastCheckedIndex < containers.size(); stepThreeLastCheckedIndex++) {

            Container container = containers.get(stepThreeLastCheckedIndex);

            boolean itemFound = false;

            for (ItemStack itemStack : container.getInventory()) {

                if (itemStack == null)
                    continue;

                for (Material material : searchedMaterials) {
                    if (itemStack.getType().equals(material)) {
                        itemFound = true;
                    }
                }

                if (itemStack.getItemMeta() instanceof BlockStateMeta blockStateMeta && blockStateMeta.getBlockState() instanceof ShulkerBox shulkerBox) {
                    for (ItemStack itemStackInShulker : shulkerBox.getInventory()) {
                        if (itemStackInShulker == null)
                            continue;
                        for (Material material : searchedMaterials) {
                            if (itemStackInShulker.getType().equals(material)) {
                                itemFound = true;
                            }
                        }
                    }
                }

            }

            if (!itemFound) {
                stepThreeToRemove.add(container);
            }

            if (outOfThisTickTime())
                return;

        }

        containers.removeAll(stepThreeToRemove);
        processStepCount++;

    }

    private void finishUp() {

        String containerOrContainers = (containers.size() == 1) ? "container" : "containers";
        player.sendMessage(ChatColor.YELLOW + "Found " + ChatColor.GOLD + containers.size() + ChatColor.YELLOW + " " + containerOrContainers + " containing " + ChatColor.GOLD + getMaterialsString(searchedMaterials) + ChatColor.YELLOW + " in a radius of " + ChatColor.GOLD + radius + ChatColor.YELLOW + " blocks.");

        ParticleBuilder particleBuilder = new ParticleBuilder(Particle.REDSTONE);
        particleBuilder.color(255, 255, 255);
        particleBuilder.receivers(player);
        particleBuilder.count(3);

        for (int i = 0; i < 10; i++) {
            new BukkitRunnable() {
                @Override
                public void run() {

                    for (Container container : containers) {

                        Block block = container.getBlock();

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

    }

    private Material getMaterialFromString(String material) {

        for (Material material1 : Material.values()) {
            if (material1.toString().equalsIgnoreCase(material))
                return material1;
        }

        return null;

    }

    private List<Material> getMaterialsList(String materialsString) {

        List<Material> materialsList = new ArrayList<>();

        for (String material : materialsString.split(",")) {
            Material material1 = getMaterialFromString(material);
            if (material1 != null)
                materialsList.add(material1);
        }

        while (materialsList.size() > AlttdUtility.getInstance().getConfig().getInt("FindItem.ItemLimit")) {
            materialsList.remove(materialsList.size() - 1);
        }

        return materialsList;

    }

    private String getMaterialsString(List<Material> materials) {

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
