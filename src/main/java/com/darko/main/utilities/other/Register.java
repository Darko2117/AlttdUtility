package com.darko.main.utilities.other;

import com.darko.main.utilities.chat.AtPlayers.NameInChatNotification;
import com.darko.main.utilities.config.ConfigReload;
import com.darko.main.utilities.deathMessage.DeathMessage;
import com.darko.main.utilities.destro.kickFromBungee.KickFromBungeeCommand;
import com.darko.main.utilities.flags.*;
import com.darko.main.utilities.logging.ItemsLogging.DroppedItemsLog;
import com.darko.main.utilities.logging.ItemsLogging.ItemPlacedInItemFrameLog;
import com.darko.main.utilities.logging.ItemsLogging.ItemTakenOutOfItemFrameLog;
import com.darko.main.utilities.logging.ItemsLogging.MCMMORepairUseLog;
import com.darko.main.utilities.permissionStuff.ItemPickup;
import com.darko.main.utilities.playerList.PlayerList;
import com.darko.main.utilities.playerList.PlayerListTabComplete;
import com.darko.main.utilities.prefixes.RemovePrefix;
import com.darko.main.utilities.prefixes.SetPrefix;
import com.darko.main.utilities.spawning.onEntitySpawn;
import org.bukkit.event.Listener;
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
import com.darko.main.utilities.destro.claimraids.RaidListener;
import com.darko.main.utilities.destro.petpickup.PetPickupListener;
import com.darko.main.utilities.destro.tamedexpire.onEntityInteractWithLead;
import com.darko.main.utilities.durability.AutoFix;
import com.darko.main.utilities.durability.onDurabilityUse;
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

    private final Main plugin;

    public Register(Main main) {
        this.plugin = main;
        RegisterEvents();
        RegisterCommands();
    }

    public static void RegisterEvents() {
        registerEvents(
                new onStairsRightClick(),
                new onPlayerDismount(),
                new onPlayerQuit(),
                new onBlockBreak(),
                new onChunkLoad(),
                //new onPlayerTeleportCommand(),
                new onAnvilClick(),
                new onEnchantmentTableClick(),
                new onPlayerTeleport(),
                new onEntityRename(),
                new onEntityInteractWithLead(),
                new onBoneMeal(),
                new onPayCommand(),
                new onPortalUse(),
                new onDurabilityUse(),
                new onPlayerMove(),
                new RaidListener(),
                //new DamageListener(),
                new EggLog(),
                //new onIronGolemDeath(),
                new onEntitySpawn(),
                new ItemPickup(),
                new DeathMessage(),
                new DroppedItemsLog(),
                new NameInChatNotification(),
                new ItemPlacedInItemFrameLog(),
                new ItemTakenOutOfItemFrameLog(),
                new MCMMORepairUseLog()
                //new onGuardianPathfind()
        );

        if (APIs.MyPetFound) {
            registerEvents(new PetPickupListener());
        }
        if (APIs.CrazyCratesFound) {
            registerEvents(new CratePrizeLog());
        }
        if (APIs.GriefPreventionFound) {
            registerEvents(
                    new ClaimCreatedLog(),
                    new ClaimDeletedLog(),
                    new ClaimExpiredLog(),
                    new ClaimModifiedLog()
            );
        }

    }

    public static void registerEvents(Listener... listeners) {
        for (Listener listener : listeners) {
            Main.getInstance().getServer().getPluginManager().registerEvents(listener, Main.getInstance());
        }
    }

    public void RegisterCommands() {
        plugin.getCommand("autofix").setExecutor(new AutoFix());
        plugin.getCommand("hat").setExecutor(new Hat());
        plugin.getCommand("servermsg").setExecutor(new Servermsg());
        plugin.getCommand("chair").setExecutor(new Chair());
        plugin.getCommand("utilityconfigreload").setExecutor(new ConfigReload());
        plugin.getCommand("list").setExecutor(new PlayerList());
        plugin.getCommand("list").setTabCompleter(new PlayerListTabComplete());
        plugin.getCommand("kickfrombungee").setExecutor(new KickFromBungeeCommand());
        plugin.getServer().getMessenger().registerOutgoingPluginChannel(Main.getInstance(), "BungeeCord");

        if (APIs.LuckPermsFound) {
            plugin.getCommand("cooldown").setExecutor(new Cooldown());
            plugin.getCommand("cooldown").setTabCompleter(new cooldownTabComplete());
        }

        if(APIs.LuckPermsFound){
            plugin.getCommand("setprefix").setExecutor(new SetPrefix());
            plugin.getCommand("removeprefix").setExecutor(new RemovePrefix());
            plugin.getCommand("prefixhistory").setExecutor(new Cooldown());
        }

        if (APIs.WorldGuardFound) {
            new Flags(plugin);
        }
    }
}
