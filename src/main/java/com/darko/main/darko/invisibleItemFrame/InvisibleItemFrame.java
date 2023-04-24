package com.darko.main.darko.invisibleItemFrame;

import com.darko.main.AlttdUtility;
import com.darko.main.common.Methods;
import com.destroystokyo.paper.ParticleBuilder;
import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.ItemFrame;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class InvisibleItemFrame implements Listener, CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (!AlttdUtility.getInstance().getConfig().getBoolean("FeatureToggles.InvisibleItemFramesCommand"))
            return true;

        if (!(sender instanceof Player)) {
            Methods.sendConfigMessage(sender, "Messages.PlayerOnlyCommandMessage");
            return true;
        }

        Player player = (Player) sender;
        Location location = player.getLocation();
        Integer radius = 100;
        try {
            radius = Integer.parseInt(args[0]);
            if (radius > 100)
                radius = 100;
        } catch (Throwable ignored) {
        }

        List<Entity> nearbyEntities = (List<Entity>) location.getNearbyEntities(radius, radius, radius);
        List<ItemFrame> invisibleItemFrames = new ArrayList<>();

        for (Entity entity : nearbyEntities) {

            if (!(entity instanceof ItemFrame))
                continue;
            ItemFrame itemFrame = (ItemFrame) entity;
            if (itemFrame.isVisible())
                continue;
            invisibleItemFrames.add((ItemFrame) entity);

        }

        String message = ChatColor.YELLOW + "There's " + ChatColor.GOLD + invisibleItemFrames.size() + ChatColor.YELLOW + " invisible itemframes in a " + ChatColor.GOLD + radius + ChatColor.YELLOW + " block radius. Showing particles around them for the next 10 seconds.";
        player.sendMessage(message);

        for (ItemFrame itemFrame : invisibleItemFrames) {
            for (Integer i = 0; i < 200; i++) {
                new BukkitRunnable() {
                    @Override
                    public void run() {

                        ParticleBuilder particleBuilder = new ParticleBuilder(Particle.REDSTONE);
                        particleBuilder.receivers(player);
                        particleBuilder.color(new Random().nextInt(256), new Random().nextInt(256), new Random().nextInt(256));
                        particleBuilder.location(itemFrame.getLocation());
                        particleBuilder.offset(0.25, 0.25, 0.25);
                        particleBuilder.count(5);
                        particleBuilder.spawn();

                    }
                }.runTaskLater(AlttdUtility.getInstance(), i);
            }
        }

        return true;

    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
    public void onItemFrameClick(PlayerInteractEntityEvent event) {

        if (!event.getHand().equals(EquipmentSlot.HAND))
            return;
        if (!(event.getRightClicked() instanceof ItemFrame))
            return;

        ItemFrame itemFrame = (ItemFrame) event.getRightClicked();

        if (itemFrame.getItem().getType().equals(Material.AIR))
            return;

        if (!event.getPlayer().getInventory().getItemInMainHand().getType().equals(Material.DIAMOND))
            return;

        Boolean takeDiamond = itemFrame.isVisible();

        Player player = event.getPlayer();

        itemFrame.setVisible(!itemFrame.isVisible());

        if (takeDiamond && !player.getGameMode().equals(GameMode.CREATIVE))
            player.getInventory().getItemInMainHand().setAmount(player.getInventory().getItemInMainHand().getAmount() - 1);

        event.setCancelled(true);

    }

}
