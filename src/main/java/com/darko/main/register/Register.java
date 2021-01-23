package com.darko.main.register;

import com.darko.main.API.APIs;
import com.darko.main.cosmetics.invisibleItemFrame.InvisibleItemFrame;
import com.darko.main.cosmetics.sit.Sit;
import com.darko.main.database.Database;
import com.darko.main.utilities.atPlayers.NameInChatNotification;
import com.darko.main.utilities.commandOnJoin.CommandOnJoin;
import com.darko.main.utilities.crash.Crash;
import com.darko.main.utilities.customChatMessage.CustomChatMessage;
import com.darko.main.utilities.deathMessage.DeathMessage;
import com.darko.main.destro.pvpFishing.PvPFishing;
import com.darko.main.destro.claimanimals.DamageListener;
import com.darko.main.destro.kickFromBungee.KickFromBungeeCommand;
import com.darko.main.utilities.flags.*;
import com.darko.main.destro.griefprevention.PublicChests;
import com.darko.main.destro.griefprevention.PublicTraders;
import com.darko.main.destro.griefprevention.TNTProtection;
import com.darko.main.utilities.lavaSponge.LavaSponge;
import com.darko.main.utilities.logging.*;
import com.darko.main.utilities.itemPickup.ItemPickup;
import com.darko.main.utilities.namedMobClaimDamage.NamedMobClaimDamage;
import com.darko.main.utilities.playerList.PlayerList;
import com.darko.main.utilities.prefixes.RemovePrefix;
import com.darko.main.utilities.prefixes.SetPrefix;
import com.darko.main.utilities.rebootWhitelist.RebootWhitelist;
import com.darko.main.utilities.reload.ReloadCommand;
import com.darko.main.utilities.spawnLimiter.SpawnLimiter;
import com.darko.main.utilities.teri.FreezeMail.FreezeMail;
import com.darko.main.utilities.teri.FreezeMail.FreezeMailPlayerListener;
import com.darko.main.utilities.teri.Nicknames.Nicknames;
import com.darko.main.utilities.teri.Nicknames.NicknamesEvents;
import com.darko.main.utilities.teri.Nicknames.NicknamesGui;
import com.darko.main.utilities.toggleGC.ToggleGC;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import com.darko.main.Main;
import com.darko.main.cosmetics.hat.Hat;
import com.darko.main.utilities.offlinePay.onPayCommand;
import com.darko.main.utilities.cooldown.Cooldown;
import com.darko.main.destro.claimraids.RaidListener;
import com.darko.main.destro.petpickup.PetPickupListener;
import com.darko.main.destro.tamedexpire.onEntityInteractWithLead;
import com.darko.main.utilities.autofix.AutoFix;
import com.darko.main.utilities.servermsg.Servermsg;

public class Register extends JavaPlugin {

    public static void registerEvents() {

        registerEvents(
                new onEntityInteractWithLead(),
                new onPayCommand(),
                new AutoFix(),
                new RaidListener(),
                new DamageListener(),
                new SpawnLimiter(),
                new ItemPickup(),
                new DeathMessage(),
                new NameInChatNotification(),
                new Database(),
                new LoggingNoAPI(),
                new InvisibleItemFrame(),
                new RebootWhitelist(),
                new LavaSponge(),
                new Sit(),
                new CommandOnJoin(),
                new PublicChests(),
                new PublicTraders(),
                new TNTProtection()
        );

        if (Main.getInstance().getConfig().getBoolean("FeatureToggles.FreezeMail")) {
            registerEvents(new FreezeMailPlayerListener());
        }
        if (APIs.MyPetFound) {
            registerEvents(new PetPickupListener());
        }
        if (APIs.CrazyCratesFound) {
            registerEvents(new LoggingCrazyCrates());
        }
        if (APIs.GriefPreventionFound) {
            registerEvents(
                    new LoggingGriefPrevention(),
                    new NamedMobClaimDamage()
            );
        }
        if (APIs.WorldGuardFound) {
            registerEvents(new Flags());
        }
        if(APIs.FarmLimiterFound){
            registerEvents(new LoggingFarmLimiter());
        }
        if(APIs.PvPManagerFound) {
            registerEvents(new PvPFishing());
        }
        if(APIs.CMIApiFound && Main.getInstance().getConfig().getBoolean("FeatureToggles.Nicknames")){
            registerEvents(new NicknamesEvents());
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
        Main.getInstance().getCommand("alttdutilityreload").setExecutor(new ReloadCommand());
        Main.getInstance().getCommand("list").setExecutor(new PlayerList());
        Main.getInstance().getCommand("kickfrombungee").setExecutor(new KickFromBungeeCommand());
        Main.getInstance().getCommand("crash").setExecutor(new Crash());
        Main.getInstance().getCommand("searchlogs").setExecutor(new LoggingSearch());
        Main.getInstance().getCommand("invisitemframes").setExecutor(new InvisibleItemFrame());
        Main.getInstance().getCommand("rebootwhitelist").setExecutor(new RebootWhitelist());
        Main.getInstance().getCommand("togglegc").setExecutor(new ToggleGC());
        Main.getInstance().getCommand("sit").setExecutor(new Sit());
        Main.getInstance().getCommand("commandonjoin").setExecutor(new CommandOnJoin());
        Main.getInstance().getCommand("ccm").setExecutor(new CustomChatMessage());

        Main.getInstance().getCommand("list").setTabCompleter(new PlayerList());
        Main.getInstance().getCommand("searchlogs").setTabCompleter(new LoggingSearch());
        Main.getInstance().getCommand("rebootwhitelist").setTabCompleter(new RebootWhitelist());
        Main.getInstance().getCommand("commandonjoin").setTabCompleter(new CommandOnJoin());
        Main.getInstance().getCommand("ccm").setTabCompleter(new CustomChatMessage());

        Main.getInstance().getCommand("freezemail").setExecutor(new FreezeMail());//This does not need to be disabled since it's just to send the mail which can be done from anywhere

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

        if(APIs.CMIApiFound && Main.getInstance().getConfig().getBoolean("FeatureToggles.Nicknames")){
            Main.getInstance().getCommand("nick").setExecutor(new Nicknames());
        }

    }

}