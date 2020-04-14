package com.darko.main.utilities.other;

import com.darko.main.Main;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import de.Keyle.MyPet.MyPetPlugin;
import me.ryanhamshire.GriefPrevention.GriefPrevention;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

public class APIs extends JavaPlugin {

    public static Boolean GriefPreventionFound = false;
    public static Boolean WorldGuardFound = false;
    public static Boolean LuckPermsFound = false;
    public static Boolean MyPetFound = false;
    public static Boolean CrazyCratesFound = false;


    public static void APIConnect() {

        if (GriefPreventionApiCheck() != null) {
            GriefPreventionFound = true;
        }
        if (WorldGuardApiCheck() != null) {
            WorldGuardFound = true;
        }
        if (LuckPermsApiCheck() != null) {
            LuckPermsFound = true;
        }
        if (MyPetApiCheck() != null) {
            MyPetFound = true;
        }
        if (CrazyCratesApiCheck() != null) {
            CrazyCratesFound = true;
        }

        if (GriefPreventionFound) {
            Main.getInstance().getLogger().info(ConsoleColors.BLUE_BRIGHT + "GriefPrevention found!... "
                    + ConsoleColors.RESET + "Accesstrust is needed for /chair to work in claims. If it's enabled in the config, claim info is getting logged.");
        } else {
            Main.getInstance().getLogger().info(ConsoleColors.RED + "GriefPrevention not found!... "
                    + ConsoleColors.RESET + "/chair will work in claims.");
        }
        if (WorldGuardFound) {
            Main.getInstance().getLogger().info(ConsoleColors.BLUE_BRIGHT + "WorldGuard found!... "
                    + ConsoleColors.RESET + "chair-sit custom flag allows players to sit in WG regions.");
        } else {
            Main.getInstance().getLogger().info(ConsoleColors.RED + "WorldGuard not found!... " + ConsoleColors.RESET
                    + "players will be able to sit in all regions.");
        }
        if (LuckPermsFound) {
            Main.getInstance().getLogger().info(ConsoleColors.BLUE_BRIGHT + "LuckPerms found!... " + ConsoleColors.RESET
                    + "/cooldown will show RTP and SuperCrate cooldowns.");
        } else {
            Main.getInstance().getLogger().info(ConsoleColors.RED + "LuckPerms not found!... " + ConsoleColors.RESET
                    + "/cooldown will be disabled.");
        }
        if (MyPetFound) {
            Main.getInstance().getLogger().info(ConsoleColors.BLUE_BRIGHT + "MyPet found!... " + ConsoleColors.RESET
                    + "If GriefPrevention is also found, accesstrust is needed for petpickup to work in claims.");
        } else {
            Main.getInstance().getLogger().info(ConsoleColors.RED + "MyPet not found!... " + ConsoleColors.RESET);
        }
        if (CrazyCratesFound) {
            Main.getInstance().getLogger().info(ConsoleColors.BLUE_BRIGHT + "CrazyCrates found!... " + ConsoleColors.RESET
                    + "If it's enabled in the config, prizes from crates are logged.");
        } else {
            Main.getInstance().getLogger().info(ConsoleColors.RED + "CrazyCrates not found!... " + ConsoleColors.RESET);
        }
    }

    public static LuckPerms LuckPermsApiCheck() {
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

    public static GriefPrevention GriefPreventionApiCheck() {
        Plugin plugin = Bukkit.getServer().getPluginManager().getPlugin("GriefPrevention");
        if (plugin instanceof GriefPrevention) {
            return (GriefPrevention) plugin;
        } else {
            return null;
        }
    }

    public static WorldGuardPlugin WorldGuardApiCheck() {
        Plugin plugin = Bukkit.getServer().getPluginManager().getPlugin("WorldGuard");
        if (plugin instanceof WorldGuardPlugin) {
            return (WorldGuardPlugin) plugin;
        } else {
            return null;
        }
    }

    public static MyPetPlugin MyPetApiCheck() {
        Plugin plugin = Bukkit.getServer().getPluginManager().getPlugin("MyPet");
        if (plugin instanceof MyPetPlugin) {
            return (MyPetPlugin) plugin;
        } else {
            return null;
        }
    }

    public static me.badbones69.crazycrates.Main CrazyCratesApiCheck() {
        Plugin plugin = Bukkit.getServer().getPluginManager().getPlugin("CrazyCrates");
        if (plugin instanceof me.badbones69.crazycrates.Main) {
            return (me.badbones69.crazycrates.Main) plugin;
        } else {
            return null;
        }
    }

}
