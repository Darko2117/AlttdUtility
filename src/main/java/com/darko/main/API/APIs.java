package com.darko.main.API;

import com.Zrips.CMI.CMI;
import com.darko.main.Main;
import com.darko.main.other.ConsoleColors;
import com.gmail.filoghost.farmlimiter.FarmLimiter;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import de.Keyle.MyPet.MyPetPlugin;
import me.NoChance.PvPManager.PvPManager;
import me.ryanhamshire.GriefPrevention.GriefPrevention;
import net.luckperms.api.LuckPerms;
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
    public static Boolean mcMMOFound = false;
    public static Boolean FarmLimiterFound = false;
    public static Boolean PvPManagerFound = false;
    public static Boolean CMIApiFound = false;


    public static void APIConnect() {

        if (GriefPreventionApiCheck() != null)
            GriefPreventionFound = true;

        if (WorldGuardApiCheck() != null)
            WorldGuardFound = true;

        if (LuckPermsApiCheck() != null)
            LuckPermsFound = true;

        if (MyPetApiCheck() != null)
            MyPetFound = true;

        if (CrazyCratesApiCheck() != null)
            CrazyCratesFound = true;

        if (mcMMOApiCheck() != null)
            mcMMOFound = true;

        if (FarmLimiterApiCheck() != null)
            FarmLimiterFound = true;

        if (PvPManagerAPICheck() != null)
            PvPManagerFound = true;

        CMIApiFound = CMIApiCheck() != null; //Kappa why not do it like this?

        if (GriefPreventionFound)
            Main.getInstance().getLogger().info(ConsoleColors.BLUE_BRIGHT + "GriefPrevention found!... " + ConsoleColors.RESET);
        else
            Main.getInstance().getLogger().info(ConsoleColors.RED + "GriefPrevention not found!... " + ConsoleColors.RESET);

        if (WorldGuardFound)
            Main.getInstance().getLogger().info(ConsoleColors.BLUE_BRIGHT + "WorldGuard found!... " + ConsoleColors.RESET);
        else
            Main.getInstance().getLogger().info(ConsoleColors.RED + "WorldGuard not found!... " + ConsoleColors.RESET);

        if (LuckPermsFound)
            Main.getInstance().getLogger().info(ConsoleColors.BLUE_BRIGHT + "LuckPerms found!... " + ConsoleColors.RESET);
        else
            Main.getInstance().getLogger().info(ConsoleColors.RED + "LuckPerms not found!... " + ConsoleColors.RESET);

        if (MyPetFound)
            Main.getInstance().getLogger().info(ConsoleColors.BLUE_BRIGHT + "MyPet found!... " + ConsoleColors.RESET);
        else
            Main.getInstance().getLogger().info(ConsoleColors.RED + "MyPet not found!... " + ConsoleColors.RESET);

        if (CrazyCratesFound)
            Main.getInstance().getLogger().info(ConsoleColors.BLUE_BRIGHT + "CrazyCrates found!... " + ConsoleColors.RESET);
        else
            Main.getInstance().getLogger().info(ConsoleColors.RED + "CrazyCrates not found!... " + ConsoleColors.RESET);

        if (mcMMOFound)
            Main.getInstance().getLogger().info(ConsoleColors.BLUE_BRIGHT + "mcMMO found!... " + ConsoleColors.RESET);
        else
            Main.getInstance().getLogger().info(ConsoleColors.RED + "mcMMO not found!... " + ConsoleColors.RESET);

        if (FarmLimiterFound)
            Main.getInstance().getLogger().info(ConsoleColors.BLUE_BRIGHT + "FarmLimiter found!... " + ConsoleColors.RESET);
        else
            Main.getInstance().getLogger().info(ConsoleColors.RED + "FarmLimiter not found!... " + ConsoleColors.RESET);

        if (PvPManagerFound)
            Main.getInstance().getLogger().info(ConsoleColors.BLUE_BRIGHT + "PvPManager found!... " + ConsoleColors.RESET);
        else
            Main.getInstance().getLogger().info(ConsoleColors.RED + "PvPManager not found!... " + ConsoleColors.RESET);

        Main.getInstance().getLogger().info((CMIApiFound ? ConsoleColors.BLUE_BRIGHT : ConsoleColors.RED) + "CMI " + (CMIApiFound ? "" : "not ") + "found!... " + ConsoleColors.RESET); //Lol one liners! I wouldn't be surprised if this was less efficient tho

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

    public static LuckPerms LuckPermsApiCheck() {
        try {
            RegisteredServiceProvider<LuckPerms> provider = Bukkit.getServicesManager().getRegistration(LuckPerms.class);
            if (provider != null) {
                return provider.getProvider();
            }
        } catch (Throwable throwable) {
            return null;
        }
        return null;
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

    public static com.gmail.nossr50.mcMMO mcMMOApiCheck() {
        Plugin plugin = Bukkit.getServer().getPluginManager().getPlugin("mcMMO");
        if (plugin instanceof com.gmail.nossr50.mcMMO) {
            return (com.gmail.nossr50.mcMMO) plugin;
        } else {
            return null;
        }
    }

    public static FarmLimiter FarmLimiterApiCheck() {
        Plugin plugin = Bukkit.getServer().getPluginManager().getPlugin("FarmLimiter");
        if (plugin instanceof FarmLimiter) {
            return (FarmLimiter) plugin;
        } else {
            return null;
        }
    }

    public static PvPManager PvPManagerAPICheck() {
        Plugin plugin = Bukkit.getPluginManager().getPlugin("PvPManager");
        if (plugin instanceof PvPManager) {
            return (PvPManager) plugin;
        } else {
            return null;
        }
    }

    public static CMI CMIApiCheck() { //Less code!
        Plugin plugin = Bukkit.getPluginManager().getPlugin("CMI");
        return plugin instanceof CMI ? (CMI) plugin : null;
    }

}