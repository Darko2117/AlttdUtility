package com.darko.main.utilities.other;

import com.darko.main.utilities.chat.AtPlayers.NameInChatNotification;
import com.darko.main.utilities.config.ConfigReload;
import com.darko.main.utilities.deathMessage.DeathMessage;
import com.darko.main.utilities.logging.ItemsLogging.DroppedItemsLog;
import com.darko.main.utilities.logging.ItemsLogging.ItemPlacedInItemFrameLog;
import com.darko.main.utilities.logging.ItemsLogging.MCMMORepairUseLog;
import com.darko.main.utilities.permissionStuff.ItemPickup;
import com.darko.main.utilities.playerList.PlayerList;
import com.darko.main.utilities.playerList.PlayerListTabComplete;
import com.darko.main.utilities.spawning.onEntitySpawn;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import com.darko.main.Main;
import com.darko.main.cosmetics.chair.Chair;
import com.darko.main.cosmetics.chair.onBlockBreak;
import com.darko.main.cosmetics.chair.onChunkLoad;
import com.darko.main.cosmetics.chair.onPlayerDismount;
import com.darko.main.cosmetics.chair.onPlayerQuit;
import com.darko.main.cosmetics.chair.onStairsRightClick;
import com.darko.main.cosmetics.hat.Hat;
import com.darko.main.utilities.CMI.onPayCommand.onPayCommand;
import com.darko.main.utilities.cooldown.Cooldown;
import com.darko.main.utilities.cooldown.cooldownTabComplete;
import com.darko.main.utilities.destro.claimanimals.DamageListener;
import com.darko.main.utilities.destro.claimraids.RaidListener;
import com.darko.main.utilities.destro.petpickup.PetPickupListener;
import com.darko.main.utilities.destro.tamedexpire.onEntityInteractWithLead;
import com.darko.main.utilities.durability.AutoFix;
import com.darko.main.utilities.durability.onDurabilityUse;
import com.darko.main.utilities.flags.Flags;
import com.darko.main.utilities.flags.onAnvilClick;
import com.darko.main.utilities.flags.onBoneMeal;
import com.darko.main.utilities.flags.onEnchantmentTableClick;
import com.darko.main.utilities.flags.onEntityRename;
import com.darko.main.utilities.permissionStuff.onPlayerMove;
import com.darko.main.utilities.logging.ClaimLogging.ClaimCreatedLog;
import com.darko.main.utilities.logging.ClaimLogging.ClaimDeletedLog;
import com.darko.main.utilities.logging.ClaimLogging.ClaimExpiredLog;
import com.darko.main.utilities.logging.ClaimLogging.ClaimModifiedLog;
import com.darko.main.utilities.logging.EggLogging.EggLog;
import com.darko.main.utilities.logging.PrizeLogging.CratePrizeLog;
import com.darko.main.utilities.portal.onPortalUse;
import com.darko.main.utilities.servermsg.Servermsg;
import com.darko.main.utilities.teleport.onPlayerTeleport;

public class Register extends JavaPlugin {

    public static void RegisterEvents() {
        Bukkit.getPluginManager().registerEvents(new onStairsRightClick(), Main.getInstance());
        Bukkit.getPluginManager().registerEvents(new onPlayerDismount(), Main.getInstance());
        Bukkit.getPluginManager().registerEvents(new onPlayerQuit(), Main.getInstance());
        Bukkit.getPluginManager().registerEvents(new onBlockBreak(), Main.getInstance());
        Bukkit.getPluginManager().registerEvents(new onChunkLoad(), Main.getInstance());
        // Bukkit.getPluginManager().registerEvents(new onPlayerTeleportCommand(),
        // Main.getInstance());
        Bukkit.getPluginManager().registerEvents(new onAnvilClick(), Main.getInstance());
        Bukkit.getPluginManager().registerEvents(new onEnchantmentTableClick(), Main.getInstance());
        Bukkit.getPluginManager().registerEvents(new onPlayerTeleport(), Main.getInstance());
        Bukkit.getPluginManager().registerEvents(new onEntityRename(), Main.getInstance());
        Bukkit.getPluginManager().registerEvents(new onEntityInteractWithLead(), Main.getInstance());
        Bukkit.getPluginManager().registerEvents(new onBoneMeal(), Main.getInstance());
        Bukkit.getPluginManager().registerEvents(new onPayCommand(), Main.getInstance());
        Bukkit.getPluginManager().registerEvents(new onPortalUse(), Main.getInstance());
        Bukkit.getPluginManager().registerEvents(new onDurabilityUse(), Main.getInstance());
        Bukkit.getPluginManager().registerEvents(new onPlayerMove(), Main.getInstance());
        Bukkit.getPluginManager().registerEvents(new RaidListener(), Main.getInstance());
        Bukkit.getPluginManager().registerEvents(new PetPickupListener(), Main.getInstance());
        Bukkit.getPluginManager().registerEvents(new CratePrizeLog(), Main.getInstance());
        //Bukkit.getPluginManager().registerEvents(new DamageListener(), Main.getInstance());
        Bukkit.getPluginManager().registerEvents(new EggLog(), Main.getInstance());
        Bukkit.getPluginManager().registerEvents(new ClaimCreatedLog(), Main.getInstance());
        Bukkit.getPluginManager().registerEvents(new ClaimDeletedLog(), Main.getInstance());
        Bukkit.getPluginManager().registerEvents(new ClaimExpiredLog(), Main.getInstance());
        Bukkit.getPluginManager().registerEvents(new ClaimModifiedLog(), Main.getInstance());
        //Bukkit.getPluginManager().registerEvents(new onIronGolemDeath(), Main.getInstance());
        Bukkit.getPluginManager().registerEvents(new onEntitySpawn(), Main.getInstance());
        Bukkit.getPluginManager().registerEvents(new ItemPickup(), Main.getInstance());
        Bukkit.getPluginManager().registerEvents(new DeathMessage(), Main.getInstance());
        Bukkit.getPluginManager().registerEvents(new DroppedItemsLog(), Main.getInstance());
        Bukkit.getPluginManager().registerEvents(new NameInChatNotification(), Main.getInstance());
        Bukkit.getPluginManager().registerEvents(new ItemPlacedInItemFrameLog(), Main.getInstance());
        Bukkit.getPluginManager().registerEvents(new MCMMORepairUseLog(), Main.getInstance());

    }

    public static void RegisterCommands() {
        Main.getInstance().getCommand("autofix").setExecutor(new AutoFix());
        Main.getInstance().getCommand("hat").setExecutor(new Hat());
        Main.getInstance().getCommand("servermsg").setExecutor(new Servermsg());
        Main.getInstance().getCommand("chair").setExecutor(new Chair());
        Main.getInstance().getCommand("utilityconfigreload").setExecutor(new ConfigReload());
        Main.getInstance().getCommand("list").setExecutor(new PlayerList());
        Main.getInstance().getCommand("list").setTabCompleter(new PlayerListTabComplete());

        if (APIs.LuckPermsFound) {
            Main.getInstance().getCommand("cooldown").setExecutor(new Cooldown());
            Main.getInstance().getCommand("cooldown").setTabCompleter(new cooldownTabComplete());
        }

        if (APIs.WorldGuardFound) {
            Flags.FlagsEnable();
        }
    }
}
