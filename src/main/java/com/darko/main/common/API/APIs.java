package com.darko.main.common.API;

import com.Zrips.CMI.CMI;
import com.alttd.VillagerUI;
import com.darko.main.AlttdUtility;
import com.darko.main.common.ConsoleColors;
//import com.plotsquared.bukkit.BukkitMain;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import de.Keyle.MyPet.MyPetPlugin;
import me.NoChance.PvPManager.PvPManager;
import me.filoghost.farmlimiter.FarmLimiter;
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
    public static Boolean CMIFound = false;
    public static Boolean VillagerShopUIFound = false;
//    public static Boolean PlotSquaredFound = false;

    public static void APIConnect() {

        GriefPreventionFound = GriefPreventionApiCheck() != null;
        WorldGuardFound = WorldGuardApiCheck() != null;
        LuckPermsFound = LuckPermsApiCheck() != null;
        MyPetFound = MyPetApiCheck() != null;
        CrazyCratesFound = CrazyCratesApiCheck() != null;
        mcMMOFound = mcMMOApiCheck() != null;
        FarmLimiterFound = FarmLimiterApiCheck() != null;
        PvPManagerFound = PvPManagerAPICheck() != null;
        CMIFound = CMIApiCheck() != null;
        VillagerShopUIFound = VillagerUICheck() != null;

//        PlotSquaredFound = PlotSquaredApiCheck() != null;

        if (GriefPreventionFound)
            AlttdUtility.getInstance().getLogger().info(ConsoleColors.BLUE_BRIGHT + "GriefPrevention found!... " + ConsoleColors.RESET);
        else
            AlttdUtility.getInstance().getLogger().info(ConsoleColors.RED + "GriefPrevention not found!... " + ConsoleColors.RESET);

        if (WorldGuardFound)
            AlttdUtility.getInstance().getLogger().info(ConsoleColors.BLUE_BRIGHT + "WorldGuard found!... " + ConsoleColors.RESET);
        else
            AlttdUtility.getInstance().getLogger().info(ConsoleColors.RED + "WorldGuard not found!... " + ConsoleColors.RESET);

        if (LuckPermsFound)
            AlttdUtility.getInstance().getLogger().info(ConsoleColors.BLUE_BRIGHT + "LuckPerms found!... " + ConsoleColors.RESET);
        else
            AlttdUtility.getInstance().getLogger().info(ConsoleColors.RED + "LuckPerms not found!... " + ConsoleColors.RESET);

        if (MyPetFound)
            AlttdUtility.getInstance().getLogger().info(ConsoleColors.BLUE_BRIGHT + "MyPet found!... " + ConsoleColors.RESET);
        else
            AlttdUtility.getInstance().getLogger().info(ConsoleColors.RED + "MyPet not found!... " + ConsoleColors.RESET);

        if (CrazyCratesFound)
            AlttdUtility.getInstance().getLogger().info(ConsoleColors.BLUE_BRIGHT + "CrazyCrates found!... " + ConsoleColors.RESET);
        else
            AlttdUtility.getInstance().getLogger().info(ConsoleColors.RED + "CrazyCrates not found!... " + ConsoleColors.RESET);

        if (mcMMOFound)
            AlttdUtility.getInstance().getLogger().info(ConsoleColors.BLUE_BRIGHT + "mcMMO found!... " + ConsoleColors.RESET);
        else
            AlttdUtility.getInstance().getLogger().info(ConsoleColors.RED + "mcMMO not found!... " + ConsoleColors.RESET);

        if (FarmLimiterFound)
            AlttdUtility.getInstance().getLogger().info(ConsoleColors.BLUE_BRIGHT + "FarmLimiter found!... " + ConsoleColors.RESET);
        else
            AlttdUtility.getInstance().getLogger().info(ConsoleColors.RED + "FarmLimiter not found!... " + ConsoleColors.RESET);

        if (PvPManagerFound)
            AlttdUtility.getInstance().getLogger().info(ConsoleColors.BLUE_BRIGHT + "PvPManager found!... " + ConsoleColors.RESET);
        else
            AlttdUtility.getInstance().getLogger().info(ConsoleColors.RED + "PvPManager not found!... " + ConsoleColors.RESET);

        if (CMIFound)
            AlttdUtility.getInstance().getLogger().info(ConsoleColors.BLUE_BRIGHT + "CMI found!... " + ConsoleColors.RESET);
        else
            AlttdUtility.getInstance().getLogger().info(ConsoleColors.RED + "CMI not found!... " + ConsoleColors.RESET);

        if (VillagerShopUIFound)
            AlttdUtility.getInstance().getLogger().info(ConsoleColors.BLUE_BRIGHT + "VillagerShopUI found!... " + ConsoleColors.RESET);
        else
            AlttdUtility.getInstance().getLogger().info(ConsoleColors.RED + "VillagerShopUI not found!... " + ConsoleColors.RESET);

//        if (PlotSquaredFound)
//            AlttdUtility.getInstance().getLogger().info(ConsoleColors.BLUE_BRIGHT + "PlotSquared found!... " + ConsoleColors.RESET);
//        else
//            AlttdUtility.getInstance().getLogger().info(ConsoleColors.RED + "PlotSquared not found!... " + ConsoleColors.RESET);

    }

    public static GriefPrevention GriefPreventionApiCheck() {
        try {
            Plugin plugin = Bukkit.getServer().getPluginManager().getPlugin("GriefPrevention");
            if (plugin instanceof GriefPrevention) {
                return (GriefPrevention) plugin;
            } else {
                return null;
            }
        } catch (Throwable throwable) {
            throwable.printStackTrace();
            return null;
        }
    }

    public static WorldGuardPlugin WorldGuardApiCheck() {
        try {
            Plugin plugin = Bukkit.getServer().getPluginManager().getPlugin("WorldGuard");
            if (plugin instanceof WorldGuardPlugin) {
                return (WorldGuardPlugin) plugin;
            } else {
                return null;
            }
        } catch (Throwable throwable) {
            throwable.printStackTrace();
            return null;
        }
    }

    public static LuckPerms LuckPermsApiCheck() {
        try {
            RegisteredServiceProvider<LuckPerms> provider = Bukkit.getServicesManager().getRegistration(LuckPerms.class);
            if (provider != null) {
                return provider.getProvider();
            } else {
                return null;
            }
        } catch (Throwable throwable) {
            throwable.printStackTrace();
            return null;
        }
    }

    public static MyPetPlugin MyPetApiCheck() {
        try {
            Plugin plugin = Bukkit.getServer().getPluginManager().getPlugin("MyPet");
            if (plugin instanceof MyPetPlugin) {
                return (MyPetPlugin) plugin;
            } else {
                return null;
            }
        } catch (Throwable throwable) {
            throwable.printStackTrace();
            return null;
        }
    }

    public static com.badbones69.crazycrates.CrazyCrates CrazyCratesApiCheck() {
        try {
            Plugin plugin = Bukkit.getServer().getPluginManager().getPlugin("CrazyCrates");
            if (plugin instanceof com.badbones69.crazycrates.CrazyCrates) {
                return (com.badbones69.crazycrates.CrazyCrates) plugin;
            } else {
                return null;
            }
        } catch (Throwable throwable) {
            throwable.printStackTrace();
            return null;
        }
    }

    public static com.gmail.nossr50.mcMMO mcMMOApiCheck() {
        try {
            Plugin plugin = Bukkit.getServer().getPluginManager().getPlugin("mcMMO");
            if (plugin instanceof com.gmail.nossr50.mcMMO) {
                return (com.gmail.nossr50.mcMMO) plugin;
            } else {
                return null;
            }
        } catch (Throwable throwable) {
            throwable.printStackTrace();
            return null;
        }
    }

    public static FarmLimiter FarmLimiterApiCheck() {
        try {
            Plugin plugin = Bukkit.getServer().getPluginManager().getPlugin("FarmLimiter");
            if (plugin instanceof FarmLimiter) {
                return (FarmLimiter) plugin;
            } else {
                return null;
            }
        } catch (Throwable throwable) {
            throwable.printStackTrace();
            return null;
        }
    }

    public static PvPManager PvPManagerAPICheck() {
        try {
            Plugin plugin = Bukkit.getPluginManager().getPlugin("PvPManager");
            if (plugin instanceof PvPManager) {
                return (PvPManager) plugin;
            } else {
                return null;
            }
        } catch (Throwable throwable) {
            throwable.printStackTrace();
            return null;
        }
    }

    public static CMI CMIApiCheck() {
        try {
            Plugin plugin = Bukkit.getPluginManager().getPlugin("CMI");
            if (plugin instanceof CMI) {
                return (CMI) plugin;
            } else {
                return null;
            }
        } catch (Throwable throwable) {
            throwable.printStackTrace();
            return null;
        }
    }

    public static VillagerUI VillagerUICheck() {
        try {
            Plugin plugin = Bukkit.getPluginManager().getPlugin("VillagerShopUI");
            if (plugin instanceof VillagerUI) {
                return (VillagerUI) plugin;
            } else {
                return null;
            }
        } catch (Throwable throwable) {
            throwable.printStackTrace();
            return null;
        }
    }

//    public static MyPlotPlugin PlotSquaredApiCheck() {
//        try {
//            Plugin plugin = Bukkit.getPluginManager().getPlugin("PlotSquared");
//            if (plugin instanceof BukkitMain) {
//                return (BukkitMain) plugin;
//            } else {
//                return null;
//            }
//        } catch (Throwable throwable) {
//            throwable.printStackTrace();
//            return null;
//        }
//    }

}