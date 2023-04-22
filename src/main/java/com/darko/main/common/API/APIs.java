package com.darko.main.common.API;

import com.Zrips.CMI.CMI;
import com.alttd.VillagerUI;
import com.alttd.chat.ChatAPI;
import com.alttd.playershops.PlayerShops;
import com.comphenix.protocol.ProtocolLib;
import com.darko.main.AlttdUtility;
import com.darko.main.common.ConsoleColors;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import de.Keyle.MyPet.MyPetPlugin;
import lombok.Getter;
import me.NoChance.PvPManager.PvPManager;
import me.filoghost.farmlimiter.FarmLimiter;
import me.ryanhamshire.GriefPrevention.GriefPrevention;
import net.luckperms.api.LuckPerms;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

@Getter
public class APIs extends JavaPlugin {

    @Getter
    private static boolean griefPreventionFound = false;
    @Getter
    private static boolean worldGuardFound = false;
    @Getter
    private static boolean luckPermsFound = false;
    @Getter
    private static boolean myPetFound = false;
    @Getter
    private static boolean crazyCratesFound = false;
    @Getter
    private static boolean MCMMOFound = false;
    @Getter
    private static boolean farmLimiterFound = false;
    @Getter
    private static boolean PvPManagerFound = false;
    @Getter
    private static boolean CMIFound = false;
    @Getter
    private static boolean villagerShopUIFound = false;
    @Getter
    private static boolean protocolLibFound = false;
    @Getter
    private static boolean playerShopsFound = false;
    @Getter
    private static boolean chatPluginFound = false;

    public static void connectGriefPreventionAPI() {
        griefPreventionFound = getGriefPreventionAPI() != null;
        sendFoundMessage("GriefPrevention", griefPreventionFound);
    }

    public static void connectWorldGuardAPI() {
        worldGuardFound = getWorldGuardAPI() != null;
        sendFoundMessage("WorldGuard", worldGuardFound);
    }

    public static void connectLuckPermsAPI() {
        luckPermsFound = getLuckPermsAPI() != null;
        sendFoundMessage("LuckPerms", luckPermsFound);
    }

    public static void connectMyPetAPI() {
        myPetFound = getMyPetAPI() != null;
        sendFoundMessage("MyPet", myPetFound);
    }

    public static void connectCrazyCratesAPI() {
        crazyCratesFound = getCrazyCratesAPI() != null;
        sendFoundMessage("CrazyCrates", crazyCratesFound);
    }

    public static void connectMCMMOAPI() {
        MCMMOFound = getMCMMOAPI() != null;
        sendFoundMessage("MCMMO", MCMMOFound);
    }

    public static void connectFarmLimiterAPI() {
        farmLimiterFound = getFarmLimiterAPI() != null;
        sendFoundMessage("FarmLimiter", farmLimiterFound);
    }

    public static void connectPvPManagerAPI() {
        PvPManagerFound = getPvPManagerAPI() != null;
        sendFoundMessage("PvPManager", PvPManagerFound);
    }

    public static void connectCMIAPI() {
        CMIFound = getCMIAPI() != null;
        sendFoundMessage("CMI", CMIFound);
    }

    public static void connectVillagerShopUIAPI() {
        villagerShopUIFound = getVillagerUIAPI() != null;
        sendFoundMessage("VillagerShopUI", villagerShopUIFound);
    }

    public static void connectProtocolLibAPI() {
        protocolLibFound = getProtocolLibAPI() != null;
        sendFoundMessage("ProtocolLib", protocolLibFound);
    }

    public static void connectPlayerShopsAPI() {
        playerShopsFound = getPlayerShopsAPI() != null;
        sendFoundMessage("PlayerShops", playerShopsFound);
    }

    public static void connectChatPluginAPI() {
        chatPluginFound = getChatPluginAPI() != null;
        sendFoundMessage("ChatPlugin", chatPluginFound);
    }

    public static void APIConnect() {

        connectGriefPreventionAPI();
        connectWorldGuardAPI();
        connectLuckPermsAPI();
        connectMyPetAPI();
        connectCrazyCratesAPI();
        connectMCMMOAPI();
        connectFarmLimiterAPI();
        connectPvPManagerAPI();
        connectCMIAPI();
        connectVillagerShopUIAPI();
        connectProtocolLibAPI();
        connectPlayerShopsAPI();
        connectChatPluginAPI();

    }

    private static void sendFoundMessage(String pluginName, boolean found) {

        if (found)
            AlttdUtility.getInstance().getLogger().info(ConsoleColors.BLUE_BRIGHT + pluginName + " found!");
        else
            AlttdUtility.getInstance().getLogger().info(ConsoleColors.RED + pluginName + " not found!");

    }

    public static GriefPrevention getGriefPreventionAPI() {
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

    public static WorldGuardPlugin getWorldGuardAPI() {
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

    public static LuckPerms getLuckPermsAPI() {
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

    public static MyPetPlugin getMyPetAPI() {
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

    public static com.badbones69.crazycrates.CrazyCrates getCrazyCratesAPI() {
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

    public static com.gmail.nossr50.mcMMO getMCMMOAPI() {
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

    public static FarmLimiter getFarmLimiterAPI() {
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

    public static PvPManager getPvPManagerAPI() {
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

    public static CMI getCMIAPI() {
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

    public static VillagerUI getVillagerUIAPI() {
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

    public static ProtocolLib getProtocolLibAPI() {
        try {
            Plugin plugin = Bukkit.getPluginManager().getPlugin("ProtocolLib");
            if (plugin instanceof ProtocolLib) {
                return (ProtocolLib) plugin;
            } else {
                return null;
            }
        } catch (Throwable throwable) {
            throwable.printStackTrace();
            return null;
        }
    }

    public static PlayerShops getPlayerShopsAPI() {
        try {
            Plugin plugin = Bukkit.getPluginManager().getPlugin("PlayerShops");
            if (plugin instanceof PlayerShops) {
                return (PlayerShops) plugin;
            } else {
                return null;
            }
        } catch (Throwable throwable) {
            throwable.printStackTrace();
            return null;
        }
    }

    public static com.alttd.chat.ChatAPI getChatPluginAPI() {
        try {
            Plugin plugin = Bukkit.getPluginManager().getPlugin("ChatPlugin");
            if (plugin instanceof ChatAPI) {
                return (ChatAPI) plugin;
            } else {
                return null;
            }
        } catch (Throwable throwable) {
            throwable.printStackTrace();
            return null;
        }
    }

}
