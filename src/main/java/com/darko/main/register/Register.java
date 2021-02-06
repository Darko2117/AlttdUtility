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
import com.darko.main.utilities.godMode.GodMode;
import com.darko.main.utilities.lavaSponge.LavaSponge;
import com.darko.main.utilities.logging.*;
import com.darko.main.utilities.itemPickup.ItemPickup;
import com.darko.main.utilities.namedMobClaimDamage.NamedMobClaimDamage;
import com.darko.main.utilities.petGodMode.PetGodMode;
import com.darko.main.utilities.playerList.PlayerList;
import com.darko.main.utilities.prefixes.RemovePrefix;
import com.darko.main.utilities.prefixes.SetPrefix;
import com.darko.main.utilities.rebootWhitelist.RebootWhitelist;
import com.darko.main.utilities.reload.ReloadCommand;
import com.darko.main.utilities.spawnLimiter.SpawnLimiter;
import com.darko.main.teri.FreezeMail.FreezeMail;
import com.darko.main.teri.FreezeMail.FreezeMailPlayerListener;
import com.darko.main.teri.Nicknames.Nicknames;
import com.darko.main.teri.Nicknames.NicknamesEvents;
import com.darko.main.utilities.toggleGC.ToggleGC;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import com.darko.main.AlttdUtility;
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
                new TNTProtection(),
                new GodMode()
        );

        if (APIs.MyPetFound) {
            registerEvents(
                    new PetPickupListener(),
                    new LoggingMyPet(),
                    new PetGodMode()
            );
        }
        if (APIs.CrazyCratesFound) {
            registerEvents(
                    new LoggingCrazyCrates()
            );
        }
        if (APIs.GriefPreventionFound) {
            registerEvents(
                    new LoggingGriefPrevention(),
                    new NamedMobClaimDamage()
            );
        }
        if (APIs.WorldGuardFound) {
            registerEvents(
                    new Flags()
            );

            if (AlttdUtility.getInstance().getConfig().getBoolean("FeatureToggles.FreezeMail")){
                registerEvents(new FreezeMailPlayerListener());
            }
        }
        if (APIs.FarmLimiterFound) {
            registerEvents(
                    new LoggingFarmLimiter()
            );
        }
        if (APIs.PvPManagerFound) {
            registerEvents(
                    new PvPFishing()
            );
        }
        if (APIs.CMIFound  && AlttdUtility.getInstance().getConfig().getBoolean("FeatureToggles.Nicknames")) {
            NicknamesEvents nicknamesEvents = new NicknamesEvents();
            AlttdUtility.getInstance().getServer().getMessenger().registerIncomingPluginChannel(AlttdUtility.getInstance(), "BungeeCord", nicknamesEvents);
            registerEvents(
                    nicknamesEvents
            );
        }

    }

    private static void registerEvents(Listener... listeners) {
        for (Listener listener : listeners) {
            AlttdUtility.getInstance().getServer().getPluginManager().registerEvents(listener, AlttdUtility.getInstance());
        }
    }

    public static void registerCommands() {

        AlttdUtility.getInstance().getCommand("autofix").setExecutor(new AutoFix());
        AlttdUtility.getInstance().getCommand("blockitempickup").setExecutor(new ItemPickup());
        AlttdUtility.getInstance().getCommand("hat").setExecutor(new Hat());
        AlttdUtility.getInstance().getCommand("servermsg").setExecutor(new Servermsg());
        AlttdUtility.getInstance().getCommand("alttdutilityreload").setExecutor(new ReloadCommand());
        AlttdUtility.getInstance().getCommand("list").setExecutor(new PlayerList());
        AlttdUtility.getInstance().getCommand("kickfrombungee").setExecutor(new KickFromBungeeCommand());
        AlttdUtility.getInstance().getCommand("crash").setExecutor(new Crash());
        AlttdUtility.getInstance().getCommand("searchlogs").setExecutor(new LoggingSearch());
        AlttdUtility.getInstance().getCommand("invisitemframes").setExecutor(new InvisibleItemFrame());
        AlttdUtility.getInstance().getCommand("rebootwhitelist").setExecutor(new RebootWhitelist());
        AlttdUtility.getInstance().getCommand("togglegc").setExecutor(new ToggleGC());
        AlttdUtility.getInstance().getCommand("sit").setExecutor(new Sit());
        AlttdUtility.getInstance().getCommand("commandonjoin").setExecutor(new CommandOnJoin());
        AlttdUtility.getInstance().getCommand("ccm").setExecutor(new CustomChatMessage());
        AlttdUtility.getInstance().getCommand("godmode").setExecutor(new GodMode());
        AlttdUtility.getInstance().getCommand("petgodmode").setExecutor(new PetGodMode());

        if (AlttdUtility.getInstance().getConfig().getBoolean("FeatureToggles.FreezeMail")){
            AlttdUtility.getInstance().getCommand("freezemail").setExecutor(new FreezeMail());
        }

        AlttdUtility.getInstance().getCommand("list").setTabCompleter(new PlayerList());
        AlttdUtility.getInstance().getCommand("searchlogs").setTabCompleter(new LoggingSearch());
        AlttdUtility.getInstance().getCommand("rebootwhitelist").setTabCompleter(new RebootWhitelist());
        AlttdUtility.getInstance().getCommand("commandonjoin").setTabCompleter(new CommandOnJoin());
        AlttdUtility.getInstance().getCommand("ccm").setTabCompleter(new CustomChatMessage());

        AlttdUtility.getInstance().getServer().getMessenger().registerOutgoingPluginChannel(AlttdUtility.getInstance(), "BungeeCord");

        if (APIs.LuckPermsFound) {
            AlttdUtility.getInstance().getCommand("cooldown").setExecutor(new Cooldown());
            AlttdUtility.getInstance().getCommand("cooldown").setTabCompleter(new Cooldown());
        }

        if (APIs.LuckPermsFound) {
            AlttdUtility.getInstance().getCommand("setprefix").setExecutor(new SetPrefix());
            AlttdUtility.getInstance().getCommand("removeprefix").setExecutor(new RemovePrefix());
            //Main.getInstance().getCommand("prefixhistory").setExecutor(new Cooldown());
        }

        if (APIs.WorldGuardFound) {
            Flags.FlagsEnable();
        }

        if (APIs.CMIFound && AlttdUtility.getInstance().getConfig().getBoolean("FeatureToggles.Nicknames")) {
            AlttdUtility.getInstance().getCommand("nick").setExecutor(new Nicknames());
        }

    }

}