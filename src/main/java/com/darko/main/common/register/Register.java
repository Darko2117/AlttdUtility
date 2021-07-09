package com.darko.main.common.register;


import com.darko.main.common.API.APIs;
import com.darko.main.darko.chorusFruitInClaim.ChorusFruitInClaim;
import com.darko.main.darko.chorusFruitOnPlot.ChorusFruitOnPlot;
import com.darko.main.darko.claimPatrol.ClaimPatrol;
import com.darko.main.darko.eggThrowingInClaims.EggThrowingInClaims;
import com.darko.main.darko.invisibleItemFrame.InvisibleItemFrame;
import com.darko.main.darko.joinNotifications.JoinNotifications;
import com.darko.main.darko.sit.Sit;
import com.darko.main.common.database.Database;
import com.darko.main.darko.atPlayers.NameInChatNotification;
import com.darko.main.darko.commandOnJoin.CommandOnJoin;
import com.darko.main.darko.crash.Crash;
import com.darko.main.darko.customChatMessage.CustomChatMessage;
import com.darko.main.darko.deathMessage.DeathMessage;
import com.darko.main.darko.toggleScruff.ToggleScruff;
import com.darko.main.darko.tpPunch.TPPunch;
import com.darko.main.darko.trapped.Trapped;
import com.darko.main.darko.witherOutsideClaim.WitherOutsideClaim;
import com.darko.main.destro.pvpFishing.PvPFishing;
import com.darko.main.destro.claimanimals.DamageListener;
import com.darko.main.destro.kickFromBungee.KickFromBungeeCommand;
import com.darko.main.darko.flags.*;
import com.darko.main.destro.griefprevention.PublicChests;
import com.darko.main.destro.griefprevention.PublicTraders;
import com.darko.main.destro.griefprevention.TNTProtection;
import com.darko.main.darko.godMode.GodMode;
import com.darko.main.darko.lavaSponge.LavaSponge;
import com.darko.main.darko.logging.*;
import com.darko.main.darko.itemPickup.ItemPickup;
import com.darko.main.darko.namedMobClaimDamage.NamedMobClaimDamage;
import com.darko.main.darko.petGodMode.PetGodMode;
import com.darko.main.darko.playerList.PlayerList;
import com.darko.main.darko.prefixes.RemovePrefix;
import com.darko.main.darko.prefixes.SetPrefix;
import com.darko.main.darko.disablePvpOnLeave.DisablePvpOnLeave;
import com.darko.main.darko.ravagerInClaim.RavagerInClaim;
import com.darko.main.darko.rebootWhitelist.RebootWhitelist;
import com.darko.main.darko.reload.ReloadCommand;
import com.darko.main.darko.spawnLimiter.SpawnLimiter;
import com.darko.main.destro.shop.ShopSignListener;
import com.darko.main.teri.FreezeMail.FreezeMail;
import com.darko.main.teri.FreezeMail.FreezeMailPlayerListener;
import com.darko.main.teri.Nicknames.Nicknames;
import com.darko.main.teri.Nicknames.NicknamesEvents;
import com.darko.main.darko.toggleGC.ToggleGC;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import com.darko.main.AlttdUtility;
import com.darko.main.darko.hat.Hat;
import com.darko.main.darko.offlinePay.onPayCommand;
import com.darko.main.darko.cooldown.Cooldown;
import com.darko.main.destro.claimraids.RaidListener;
import com.darko.main.destro.petpickup.PetPickupListener;
import com.darko.main.destro.tamedexpire.onEntityInteractWithLead;
import com.darko.main.darko.autofix.AutoFix;
import com.darko.main.darko.servermsg.Servermsg;

public class Register extends JavaPlugin {

    public static void registerEvents() {

        HandlerList.unregisterAll(AlttdUtility.getInstance());

        registerEvents(new Database());
        registerEvents(new LoggingNoAPI());

        if (AlttdUtility.getInstance().getConfig().getBoolean("FeatureToggles.BlockOfflinePay"))
            registerEvents(new onPayCommand());

        if (AlttdUtility.getInstance().getConfig().getBoolean("FeatureToggles.AutofixCommand"))
            registerEvents(new AutoFix());

        if (AlttdUtility.getInstance().getConfig().getBoolean("FeatureToggles.SpawnLimiter"))
            registerEvents(new SpawnLimiter());

        if (AlttdUtility.getInstance().getConfig().getBoolean("FeatureToggles.ItemPickupCommand"))
            registerEvents(new ItemPickup());

        if (AlttdUtility.getInstance().getConfig().getBoolean("FeatureToggles.DeathMessage"))
            registerEvents(new DeathMessage());

        if (AlttdUtility.getInstance().getConfig().getBoolean("FeatureToggles.ChatAtPlayers"))
            registerEvents(new NameInChatNotification());

        if (AlttdUtility.getInstance().getConfig().getBoolean("FeatureToggles.InvisibleItemFrames"))
            registerEvents(new InvisibleItemFrame());

        if (AlttdUtility.getInstance().getConfig().getBoolean("FeatureToggles.RebootWhitelist"))
            registerEvents(new RebootWhitelist());

        if (AlttdUtility.getInstance().getConfig().getBoolean("FeatureToggles.LavaSponge"))
            registerEvents(new LavaSponge());

        if (AlttdUtility.getInstance().getConfig().getBoolean("FeatureToggles.Sit"))
            registerEvents(new Sit());

        if (AlttdUtility.getInstance().getConfig().getBoolean("FeatureToggles.CommandOnJoin"))
            registerEvents(new CommandOnJoin());

        if (AlttdUtility.getInstance().getConfig().getBoolean("FeatureToggles.GodModeCommand"))
            registerEvents(new GodMode());

        if (AlttdUtility.getInstance().getConfig().getBoolean("FeatureToggles.TrappedCommand"))
            registerEvents(new Trapped());

        if (APIs.MyPetFound) {

            registerEvents(new LoggingMyPet());

            if (AlttdUtility.getInstance().getConfig().getBoolean("FeatureToggles.BlockPetPickupInClaimWithoutContainerTrust"))
                registerEvents(new PetPickupListener());

            if (AlttdUtility.getInstance().getConfig().getBoolean("FeatureToggles.PetGodModeCommand"))
                registerEvents(new PetGodMode());

        }

        if (APIs.CrazyCratesFound) {

            registerEvents(new LoggingCrazyCrates());

        }

        if (APIs.GriefPreventionFound) {

            registerEvents(new LoggingGriefPrevention());

            if (AlttdUtility.getInstance().getConfig().getBoolean("FeatureToggles.UnclaimAnimalWithLead"))
                registerEvents(new onEntityInteractWithLead());

            if (AlttdUtility.getInstance().getConfig().getBoolean("FeatureToggles.BlockRaidsInClaimWithoutAccessTrust"))
                registerEvents(new RaidListener());

            if (AlttdUtility.getInstance().getConfig().getBoolean("FeatureToggles.BlockAnimalDamageInClaimWithoutTrust"))
                registerEvents(new DamageListener());

            if (AlttdUtility.getInstance().getConfig().getBoolean("FeatureToggles.AllowNamedPublicChests"))
                registerEvents(new PublicChests());

            if (AlttdUtility.getInstance().getConfig().getBoolean("FeatureToggles.AllowNamedPublicVillagers"))
                registerEvents(new PublicTraders());

            if (AlttdUtility.getInstance().getConfig().getBoolean("FeatureToggles.ProtectTNTArrowDamage"))
                registerEvents(new TNTProtection());

            if (AlttdUtility.getInstance().getConfig().getBoolean("FeatureToggles.NamedMobClaimDamageProtection"))
                registerEvents(new NamedMobClaimDamage());

            if (AlttdUtility.getInstance().getConfig().getBoolean("FeatureToggles.BlockRavagerDestroyingBlocksInClaim"))
                registerEvents(new RavagerInClaim());

            if (AlttdUtility.getInstance().getConfig().getBoolean("FeatureToggles.BlockEggThrowingInClaimsWithoutTrust")) {
                registerEvents(new EggThrowingInClaims());
            }

            if (AlttdUtility.getInstance().getConfig().getBoolean("FeatureToggles.BlockChorusFruitArrowBreakingInClaim")) {
                registerEvents(new ChorusFruitInClaim());
            }

            if (AlttdUtility.getInstance().getConfig().getBoolean("FeatureToggles.TPPunchCommand")) {
                registerEvents(new TPPunch());
            }

            if (AlttdUtility.getInstance().getConfig().getBoolean("FeatureToggles.BlockWitherBlockAndEntityDamageOutsideClaim")) {
                registerEvents(new WitherOutsideClaim());
            }

        }

        if (APIs.WorldGuardFound) {

            if (AlttdUtility.getInstance().getConfig().getBoolean("FeatureToggles.CustomWorldGuardFlags"))
                registerEvents(new Flags());

            if (AlttdUtility.getInstance().getConfig().getBoolean("FeatureToggles.FreezeMail")) {
                registerEvents(new FreezeMailPlayerListener());
            }

        }

        if (APIs.FarmLimiterFound) {

            registerEvents(new LoggingFarmLimiter());

        }

        if (APIs.PvPManagerFound) {

            if (AlttdUtility.getInstance().getConfig().getBoolean("FeatureToggles.PreventNoPvPFishing")) {
                registerEvents(new PvPFishing());
            }
            if (AlttdUtility.getInstance().getConfig().getBoolean("FeatureToggles.DisablePvpOnLeave")) {
                registerEvents(new DisablePvpOnLeave());
            }

        }

        if (APIs.CMIFound) {

            if (AlttdUtility.getInstance().getConfig().getBoolean("FeatureToggles.Nicknames")) {
                NicknamesEvents nicknamesEvents = new NicknamesEvents();
                AlttdUtility.getInstance().getServer().getMessenger().registerIncomingPluginChannel(AlttdUtility.getInstance(), "BungeeCord", nicknamesEvents);
                registerEvents(nicknamesEvents);
            }

            if (AlttdUtility.getInstance().getConfig().getBoolean("FeatureToggles.JoinNotifications")) {
                registerEvents(new JoinNotifications());
            }

        }

        if (APIs.ShopFound) {

            if (AlttdUtility.getInstance().getConfig().getBoolean("FeatureToggles.BlockSignEditIfShop")) {
                registerEvents(new ShopSignListener());
            }

        }

        if(APIs.PlotSquaredFound){

            if (AlttdUtility.getInstance().getConfig().getBoolean("FeatureToggles.BlockChorusFruitArrowBreakingOnPlot")) {
                registerEvents(new ChorusFruitOnPlot());
            }

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
        AlttdUtility.getInstance().getCommand("sit").setExecutor(new Sit());
        AlttdUtility.getInstance().getCommand("commandonjoin").setExecutor(new CommandOnJoin());
        AlttdUtility.getInstance().getCommand("ccm").setExecutor(new CustomChatMessage());
        AlttdUtility.getInstance().getCommand("godmode").setExecutor(new GodMode());
        AlttdUtility.getInstance().getCommand("petgodmode").setExecutor(new PetGodMode());
        AlttdUtility.getInstance().getCommand("freezemail").setExecutor(new FreezeMail());
        AlttdUtility.getInstance().getCommand("trapped").setExecutor(new Trapped());
        AlttdUtility.getInstance().getCommand("tppunch").setExecutor(new TPPunch());

        AlttdUtility.getInstance().getCommand("list").setTabCompleter(new PlayerList());
        AlttdUtility.getInstance().getCommand("searchlogs").setTabCompleter(new LoggingSearch());
        AlttdUtility.getInstance().getCommand("rebootwhitelist").setTabCompleter(new RebootWhitelist());
        AlttdUtility.getInstance().getCommand("commandonjoin").setTabCompleter(new CommandOnJoin());
        AlttdUtility.getInstance().getCommand("ccm").setTabCompleter(new CustomChatMessage());
        AlttdUtility.getInstance().getCommand("tppunch").setTabCompleter(new TPPunch());

        AlttdUtility.getInstance().getServer().getMessenger().registerOutgoingPluginChannel(AlttdUtility.getInstance(), "BungeeCord");

        if (APIs.GriefPreventionFound) {
            AlttdUtility.getInstance().getCommand("claimpatrol").setExecutor(new ClaimPatrol());
            AlttdUtility.getInstance().getCommand("claimpatrol").setTabCompleter(new ClaimPatrol());
        }

        if (APIs.LuckPermsFound) {
            AlttdUtility.getInstance().getCommand("cooldown").setExecutor(new Cooldown());
            AlttdUtility.getInstance().getCommand("togglescruff").setExecutor(new ToggleScruff());
            AlttdUtility.getInstance().getCommand("togglegc").setExecutor(new ToggleGC());

            AlttdUtility.getInstance().getCommand("cooldown").setTabCompleter(new Cooldown());
        }

        if (APIs.LuckPermsFound) {
            AlttdUtility.getInstance().getCommand("setprefix").setExecutor(new SetPrefix());
            AlttdUtility.getInstance().getCommand("removeprefix").setExecutor(new RemovePrefix());
        }

        if (APIs.CMIFound) {
            AlttdUtility.getInstance().getCommand("nick").setExecutor(new Nicknames());
        }

        if (APIs.WorldGuardFound) {
            Flags.FlagsEnable();
        }

    }

}