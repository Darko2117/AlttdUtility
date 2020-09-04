package com.darko.main.register;

import com.darko.main.API.APIs;
import com.darko.main.database.Database;
import com.darko.main.utilities.atPlayers.NameInChatNotification;
import com.darko.main.utilities.crash.Crash;
import com.darko.main.utilities.deathMessage.DeathMessage;
import com.darko.main.utilities.destro.claimanimals.DamageListener;
import com.darko.main.utilities.destro.kickFromBungee.KickFromBungeeCommand;
import com.darko.main.utilities.flags.*;
import com.darko.main.utilities.logging.uiclicklogging.UIClicksLog;
import com.darko.main.utilities.logging.itemslogging.*;
import com.darko.main.utilities.logging.claimlogging.ClaimCreatedLog;
import com.darko.main.utilities.logging.claimlogging.ClaimDeletedLog;
import com.darko.main.utilities.logging.claimlogging.ClaimExpiredLog;
import com.darko.main.utilities.logging.claimlogging.ClaimModifiedLog;
import com.darko.main.utilities.logging.egglogging.EggLog;
import com.darko.main.utilities.itemPickup.ItemPickup;
import com.darko.main.utilities.playerList.PlayerList;
import com.darko.main.utilities.prefixes.RemovePrefix;
import com.darko.main.utilities.prefixes.SetPrefix;
import com.darko.main.utilities.reload.ReloadCommand;
import com.darko.main.utilities.spawnLimiter.SpawnLimiter;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import com.darko.main.Main;
import com.darko.main.cosmetics.chair.Chair;
import com.darko.main.cosmetics.hat.Hat;
import com.darko.main.utilities.offlinePay.onPayCommand;
import com.darko.main.utilities.cooldown.Cooldown;
import com.darko.main.utilities.destro.claimraids.RaidListener;
import com.darko.main.utilities.destro.petpickup.PetPickupListener;
import com.darko.main.utilities.destro.tamedexpire.onEntityInteractWithLead;
import com.darko.main.utilities.autofix.AutoFix;
import com.darko.main.utilities.logging.prizelogging.CratePrizeLog;
import com.darko.main.utilities.servermsg.Servermsg;

public class Register extends JavaPlugin {

    public static void registerEvents() {
        registerEvents(
                new onEntityInteractWithLead(),
                new onPayCommand(),
                new AutoFix(),
                new RaidListener(),
                new DamageListener(),
                new EggLog(),
                new SpawnLimiter(),
                new ItemPickup(),
                new DeathMessage(),
                new DroppedItemsLog(),
                new NameInChatNotification(),
                new ItemsPlacedInItemFramesLog(),
                new ItemTakenOutOfItemFrameLog(),
                new MCMMORepairUseLog(),
                new PickedUpItemsLog(),
                new UIClicksLog(),
                new Database(),
                new Chair()
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
        if(APIs.WorldGuardFound){
            registerEvents(new Flags());
        }

    }

    private static void registerEvents(Listener... listeners) {
        for (Listener listener : listeners) {
            Main.getInstance().getServer().getPluginManager().registerEvents(listener, Main.getInstance());
        }
    }

    public static void registerCommands() {
        Main.getInstance().getCommand("autofix").setExecutor(new AutoFix());
        Main.getInstance().getCommand("blockitempickup").setExecutor(new ItemPickup());
        Main.getInstance().getCommand("hat").setExecutor(new Hat());
        Main.getInstance().getCommand("servermsg").setExecutor(new Servermsg());
        Main.getInstance().getCommand("chair").setExecutor(new Chair());
        Main.getInstance().getCommand("alttdutilityreload").setExecutor(new ReloadCommand());
        Main.getInstance().getCommand("list").setExecutor(new PlayerList());
        Main.getInstance().getCommand("list").setTabCompleter(new PlayerList());
        Main.getInstance().getCommand("kickfrombungee").setExecutor(new KickFromBungeeCommand());
        Main.getInstance().getCommand("crash").setExecutor(new Crash());
        Main.getInstance().getServer().getMessenger().registerOutgoingPluginChannel(Main.getInstance(), "BungeeCord");

        if (APIs.LuckPermsFound) {
            Main.getInstance().getCommand("cooldown").setExecutor(new Cooldown());
            Main.getInstance().getCommand("cooldown").setTabCompleter(new Cooldown());
        }

        if (APIs.LuckPermsFound) {
            Main.getInstance().getCommand("setprefix").setExecutor(new SetPrefix());
            Main.getInstance().getCommand("removeprefix").setExecutor(new RemovePrefix());
            //Main.getInstance().getCommand("prefixhistory").setExecutor(new Cooldown());
        }

        if (APIs.WorldGuardFound) {
            Flags.FlagsEnable();
        }
    }
}
