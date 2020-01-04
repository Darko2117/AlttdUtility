package com.darko.main.utilities.other;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import com.sk89q.worldguard.bukkit.WorldGuardPlugin;

import me.ryanhamshire.GriefPrevention.GriefPrevention;
import net.luckperms.api.LuckPerms;

public class APIs extends JavaPlugin {

    public static void APIConnect() {
        if (GriefPreventionApi() != null) {
            GlobalVariables.GriefPreventionFound = true;
        }
        if (WorldGuardApi() != null) {
            GlobalVariables.WorldGuardFound = true;
        }
        if (LuckPermsApi() != null) {
            GlobalVariables.LuckPermsFound = true;
        }

        if (GlobalVariables.GriefPreventionFound) {
            System.out.print("[AlttdUtility] ");
            System.out.println(ConsoleColors.BLUE_BRIGHT + "GriefPrevention found!... " + ConsoleColors.RESET
                    + "Accesstrust is needed for /chair to work in claims.");
        } else {
            System.out.print("[AlttdUtility] ");
            System.out.println(ConsoleColors.RED + "GriefPrevention not found!... " + ConsoleColors.RESET
                    + "/chair will work in claims.");
        }
        if (GlobalVariables.WorldGuardFound) {
            System.out.print("[AlttdUtility] ");
            System.out.println(ConsoleColors.BLUE_BRIGHT + "WorldGuard found!... " + ConsoleColors.RESET
                    + "chair-sit custom flag allows players to sit in WG regions.");
        } else {
            System.out.print("[AlttdUtility] ");
            System.out.println(ConsoleColors.RED + "WorldGuard not found!... " + ConsoleColors.RESET
                    + "players will be able to sit in all regions.");
        }
        if (GlobalVariables.LuckPermsFound) {
            System.out.print("[AlttdUtility] ");
            System.out.println(ConsoleColors.BLUE_BRIGHT + "LuckPerms found!... " + ConsoleColors.RESET
                    + "/cooldown will show RTP and SuperCrate cooldowns.");
        } else {
            System.out.print("[AlttdUtility] ");
            System.out.println(ConsoleColors.RED + "LuckPerms not found!... " + ConsoleColors.RESET
                    + "/cooldown will be disabled.");
        }
    }

    public static GriefPrevention GriefPreventionApi() {
        Plugin plugin = Bukkit.getServer().getPluginManager().getPlugin("GriefPrevention");
        if (plugin instanceof GriefPrevention) {
            return (GriefPrevention) plugin;
        } else {
            return null;
        }
    }

    public static WorldGuardPlugin WorldGuardApi() {
        Plugin plugin = Bukkit.getServer().getPluginManager().getPlugin("WorldGuard");
        if (plugin instanceof WorldGuardPlugin) {
            return (WorldGuardPlugin) plugin;
        } else {
            return null;
        }
    }

    public static LuckPerms LuckPermsApi() {
        try {
            if (Class.forName("net.luckperms.api.LuckPerms") != null) {
                RegisteredServiceProvider<LuckPerms> provider = Bukkit.getServicesManager()
                        .getRegistration(LuckPerms.class);
                if (provider != null) {
                    LuckPerms api = provider.getProvider();
                    return api;
                } else {
                    return null;
                }
            }
        } catch (ClassNotFoundException e) {
        }
        return null;
    }

}
