package com.darko.main.config;

import java.io.File;
import java.util.*;

import com.darko.main.other.Methods;
import com.darko.main.utilities.logging.Logging;
import org.bukkit.configuration.file.FileConfiguration;

import com.darko.main.Main;

public class ConfigSetup {

    private enum Messages {

        NoPermission("Messages.NoPermission", "&cYou do not have permission to do this."),
        //        ChairEnabled("Messages.ChairEnabled", "&aChair mode enabled, right click on any stairs to sit on them."),
//        ChairDisabled("Messages.ChairDisabled", "&cChair mode disabled."),
//        InvalidChairBlock("Messages.InvalidChairBlock", "&cInvalid block found above/below the stairs."),
        SeatNoClaimPerm("Messages.SeatNoClaimPerm", "&cYou don't have %player%'s permission to use that."),
        SeatNoRegionPerm("Messages.SeatNoRegionPerm", "&cYou can't sit in this region."),
        HatNoItem("Messages.HatNoItem", "&eHold the item that you wish to put on your head."),
        HatEquipped("Messages.HatEquipped", "&aItem equipped."),
        HatSwapped("Messages.HatSwapped", "&aItems swapped."),
        HatCurseOfBinding("Messages.HatCurseOfBinding", "&cCan't take off an item that has the &d&oCurse of Binding&r&c."),
        OfflinePlayerPayment("Messages.OfflinePlayerPayment", "&cYou can't send money to offline players."),
        ListGroup("Messages.ListGroup", "&eOnline players for the group &b%group%&6:"),
        AutofixEnabled("Messages.AutofixEnabled", "&aAutofix enabled."),
        AutofixDisabled("Messages.AutofixDisabled", "&cAutofix disabled."),
        PlayerNotFound("Messages.PlayerNotFound", "&cPlayer not found."),
        GroupNotFound("Messages.GroupNotFound", "&cNo online players in that group."),
        DeathMessage("Messages.DeathMessage", "&cUse &6/dback&c to go back to your previous death location."),
        InvalidPrefixLengthMessage("Messages.InvalidPrefixLengthMessage", "&cA prefix cannot be longer than 10 characters."),
        PrefixSetConfirmedMessage("Messages.PrefixSetConfirmedMessage", "&ePrefix &6%prefix%&e set for &6%player%&e."),
        PrefixRemovedConfirmedMessage("Messages.PrefixRemovedConfirmedMessage", "&ePrefix removed for &6%player%&e."),
        ServermsgInvalidUsage("Messages.ServermsgInvalidUsage", "&cUsage of this command is /servermsg <player/permission> <message>. You can use color codes and \\n for a new line."),
        NoDatabaseConnectionMessage("Messages.NoDatabaseConnectionMessage", "&cThis feature is unavailable when there is no database connection."),
        PlayerOnlyCommandMessage("Messages.PlayerOnlyCommandMessage", "&cThis command can only be performed by a player."),
        BlockItemPickupEnabledMessage("Messages.BlockItemPickupEnabledMessage", "&aBlock item pickup enabled."),
        BlockItemPickupDisabledMessage("Messages.BlockItemPickupDisabledMessage", "&cBlock item pickup disabled."),
        CrashCommandInvalidUsage("Messages.CrashCommandInvalidUsage", "&cUsage of this command is /crash <player>."),
        CrashCommandOfflinePlayer("Messages.CrashCommandOfflinePlayer", "&cThe player you are trying to crash is offline."),
        RebootWhitelistKickMessage("Messages.RebootWhitelistKickMessage", "&fThe server is rebooting, you will be able to join shortly."),
        IncorrectUsageSearchLogsCommand("Messages.IncorrectUsageSearchLogsCommand", "&cUsage of this command is /searchlogs <normal/special/additional>."),
        IncorrectUsageSearchNormalLogsCommand("Messages.IncorrectUsageSearchNormalLogsCommand", "&cUsage of this command is /searchlogs normal <numberOfDays> <search-string>."),
        IncorrectUsageSearchSpecialLogsCommand("Messages.IncorrectUsageSearchSpecialLogsCommand", "&cUsage of this command is /searchlogs special <logName> <numberOfDays> <Argument1Name: Argument1> <Argument2Name: Argument2> <Argument3Name: Argument3>... Just follow what tab complete is giving you or check out the drive document."),
        IncorrectUsageSearchAdditionalLogsCommand("Messages.IncorrectUsageSearchAdditionalLogsCommand", "&cUsage of this command is /searchlogs additional <logName> <numberOfDays> <search-string>."),
        SitCommandNotOnGroundMessage("Messages.SitCommandNotOnGroundMessage", "&cYou must be standing on the ground to do this command."),
        SeatOccupiedMessage("Messages.SeatOccupiedMessage", "&cThat seat is occupied."),
        SeatInvalidBlock("Messages.SeatInvalidBlock", "&cYou can't sit on that block."),
        GriefPreventionNoBuildPermMessage("Messages.GriefPreventionNoBuildPermMessage", "&cYou don't have %player%'s permission to build here."),
        GriefPreventionThatBelongsToMessage("Messages.GriefPreventionThatBelongsToMessage", "&cThat belongs to %player%."),
        InvalidUsageCommandOnJoinMessage("Messages.InvalidUsageCommandOnJoinMessage", "&cUsage of this command is /commandonjoin <player> <command>."),
        CommandOnJoinSetMessage("Messages.CommandOnJoinSetMessage", "&aSet command:%command% for player:%player%."),
        CustomChatMessagesUsage("Messages.CustomChatMessagesUsage", "&cUsage of this command is\n/ccm <add/remove/edit> <messageName> <message>\nor\n/ccm <messageName>.");

        private final String path;
        private final String message;

        Messages(String path, String message) {
            this.path = path;
            this.message = message;
        }

    }

    public static void configSetup() {

        FileConfiguration config = Main.getInstance().getConfig();

        //Toggles

        List<String> toggles = new ArrayList<>();
//        toggles.add("Chair");
        toggles.add("Hat");
        toggles.add("ChatAtPlayers");
        toggles.add("BlockOfflinePay");
        toggles.add("CooldownCommand");
        toggles.add("DeathMessage");
        toggles.add("BlockAnimalDamageInClaimWithoutTrust");
        toggles.add("BlockRaidsInClaimWithoutAccessTrust");
        toggles.add("KickFromBungeeCommand");
        toggles.add("BlockPetPickupInClaimWithoutContainerTrust");
        toggles.add("UnclaimAnimalWithLead");
        toggles.add("AutofixCommand");
        toggles.add("CustomWorldGuardFlags");
        toggles.add("ItemPickupCommand");
        toggles.add("PlayerListCommand");
        toggles.add("RemovePrefixCommand");
        toggles.add("SetPrefixCommand");
        toggles.add("ServerMsgCommand");
        toggles.add("CrashCommand");
        toggles.add("SpawnLimiter");
        toggles.add("SearchNormalLogsCommand");
        toggles.add("SearchSpecialLogsCommand");
        toggles.add("SearchAdditionalLogsCommand");
        toggles.add("InvisibleItemFrames");
        toggles.add("InvisibleItemFramesCommand");
        toggles.add("RebootWhitelist");
        toggles.add("LavaSponge");
        toggles.add("Sit");
        toggles.add("ToggleGCCommand");
        toggles.add("CommandOnJoin");
        toggles.add("NamedMobClaimDamageProtection");
        toggles.add("CustomChatMessagesCommand");

        for (String string : toggles) {
            if (!config.contains("FeatureToggles." + string)) {
                config.set("FeatureToggles." + string, true);
                notFoundInConfigMessage("FeatureToggles." + string);
            }
        }

        // ----------------------------------------------------------------------------------------------------

        // Messages

        for (Messages message : Messages.values()) {
            if (!config.contains(message.path)) {
                config.set(message.path, message.message);
                notFoundInConfigMessage(message.path);
            }
        }

        // ----------------------------------------------------------------------------------------------------

        //LavaSponge

        if (!config.contains("LavaSponge.DrySpongeRange")) {
            config.set("LavaSponge.DrySpongeRange", 6);
            notFoundInConfigMessage("LavaSponge.DrySpongeRange");
        }
        if (!config.contains("LavaSponge.DrySpongeAbsorbLimit")) {
            config.set("LavaSponge.DrySpongeAbsorbLimit", 55);
            notFoundInConfigMessage("LavaSponge.DrySpongeAbsorbLimit");
        }
        if (!config.contains("LavaSponge.WetSpongeRange")) {
            config.set("LavaSponge.WetSpongeRange", 7);
            notFoundInConfigMessage("LavaSponge.WetSpongeRange");
        }
        if (!config.contains("LavaSponge.WetSpongeAbsorbLimit")) {
            config.set("LavaSponge.WetSpongeAbsorbLimit", 65);
            notFoundInConfigMessage("LavaSponge.WetSpongeAbsorbLimit");
        }

        //-----------------------------------------------------------------------------------------------------

        // Cooldown command

        if (!config.contains("CooldownCommandPermissions")) {

            List<String> permissions = new ArrayList<>();
            permissions.add("Permission:rtp.no Name:RTP");
            permissions.add("Permission:keyshop.buy Name:SuperCrate");

            config.set("CooldownCommandPermissions", permissions);

            notFoundInConfigMessage("CooldownCommandPermissions");

        }

        // ----------------------------------------------------------------------------------------------------

        // List command

        if (!config.contains("ListGroups")) {

            List<String> Owner = new ArrayList<>();
            Owner.add("owner");

            List<String> Staff = new ArrayList<>();
            Staff.add("trainee");
            Staff.add("moderator");
            Staff.add("headmod");
            Staff.add("admin");
            Staff.add("manager");

            List<String> Event_Team = new ArrayList<>();
            Event_Team.add("eventteam");
            Event_Team.add("eventleader");

            List<String> Donors = new ArrayList<>();
            Donors.add("count");
            Donors.add("viceroy");
            Donors.add("duke");
            Donors.add("archduke");

            List<String> Other = new ArrayList<>();
            Other.add("default");
            Other.add("nomad");
            Other.add("peddler");
            Other.add("settler");
            Other.add("resident");
            Other.add("esquire");
            Other.add("knight");
            Other.add("baron");

            config.set("ListGroups.Owner", Owner);
            config.set("ListGroups.Staff", Staff);
            config.set("ListGroups.Event Team", Event_Team);
            config.set("ListGroups.Donors", Donors);
            config.set("ListGroups.Other", Other);

            notFoundInConfigMessage("ListGroups");

        }

        // ----------------------------------------------------------------------------------------------------

        // List groups

        if (!config.contains("PrefixAvailableGroups")) {

            List<String> groups = new ArrayList<>();

            groups.add("archduke");

            config.set("PrefixAvailableGroups", groups);

            notFoundInConfigMessage("PrefixAvailableGroups");

        }

        // ----------------------------------------------------------------------------------------------------

        // SpawnLimiter

        if (!config.contains("SpawnLimiter")) {

            config.createSection("SpawnLimiter.IRON_GOLEM");

            Integer spawnRadius = 208;
            Integer spawnTimeLimit = 60;
            Integer spawnLimit = 10;

            config.set("SpawnLimiter.IRON_GOLEM.RadiusLimit", spawnRadius);
            config.set("SpawnLimiter.IRON_GOLEM.TimeLimit", spawnTimeLimit);
            config.set("SpawnLimiter.IRON_GOLEM.SpawnLimit", spawnLimit);

            notFoundInConfigMessage("SpawnLimiter");

        }

        // ----------------------------------------------------------------------------------------------------

        // Logging

        for (Map.Entry<String, String> entry : Logging.logNamesAndConfigPaths.entrySet()) {

            if (!config.contains(entry.getValue())) {

                config.set(entry.getValue() + ".Enabled", true);
                config.set(entry.getValue() + ".NumberOfLogsToKeep", 365);

                notFoundInConfigMessage(entry.getValue());

            }

        }

        // ----------------------------------------------------------------------------------------------------

        // SearchLogs

        if (!config.contains("SearchLogs.OutputPath")) {
            config.set("SearchLogs.OutputPath", new File(Main.getInstance().getDataFolder() + "/search-output/").getAbsolutePath());
            notFoundInConfigMessage("SearchLogs.OutputPath");
        }
        if (!config.contains("SearchLogs.MaxFileSizeWithoutCompression")) {
            config.set("SearchLogs.MaxFileSizeWithoutCompression", 8);
            notFoundInConfigMessage("SearchLogs.MaxFileSizeWithoutCompression");
        }
        if (!config.contains("SearchLogs.NormalSearchBlacklistedStrings")) {
            List<String> blacklistedStrings = new ArrayList<>();
            config.set("SearchLogs.NormalSearchBlacklistedStrings", blacklistedStrings);
            notFoundInConfigMessage("SearchLogs.NormalSearchBlacklistedStrings");
        }

        // ----------------------------------------------------------------------------------------------------

        // DatabaseInitiate

        if (!config.contains("Database.driver")) {
            config.set("Database.driver", "mariadb");
            notFoundInConfigMessage("Database.driver");
        }
        if (!config.contains("Database.ip")) {
            config.set("Database.ip", "localhost");
            notFoundInConfigMessage("Database.ip");
        }
        if (!config.contains("Database.port")) {
            config.set("Database.port", "3306");
            notFoundInConfigMessage("Database.port");
        }
        if (!config.contains("Database.name")) {
            config.set("Database.name", "alttdutility");
            notFoundInConfigMessage("Database.name");
        }
        if (!config.contains("Database.username")) {
            config.set("Database.username", "root");
            notFoundInConfigMessage("Database.username");
        }
        if (!config.contains("Database.password")) {
            config.set("Database.password", "");
            notFoundInConfigMessage("Database.password");
        }

        // ----------------------------------------------------------------------------------------------------

        // RebootWhitelist

        if (!config.contains("RebootWhitelist.CommandsOnEnable")) {
            List<String> commandsOnEnable = new ArrayList<>();
            commandsOnEnable.add("mpdb saveandkick");
            config.set("RebootWhitelist.CommandsOnEnable", commandsOnEnable);
            notFoundInConfigMessage("RebootWhitelist.CommandsOnEnable");
        }
        if (!config.contains("RebootWhitelist.DisableTimeAfterBoot")) {
            config.set("RebootWhitelist.DisableTimeAfterBoot", 15);
            notFoundInConfigMessage("RebootWhitelist.DisableTimeAfterBoot");
        }
        if (!config.contains("RebootWhitelist.Enabled")) {
            config.set("RebootWhitelist.Enabled", false);
            notFoundInConfigMessage("RebootWhitelist.Enabled");
        }

        // ----------------------------------------------------------------------------------------------------

        // NumberOfClaimsFlag

        if (!config.contains("NumberOfClaimsFlag.MinNumberOfClaimsToLog")) {
            config.set("NumberOfClaimsFlag.MinNumberOfClaimsToLog", 40);
            notFoundInConfigMessage("NumberOfClaimsFlag.MinNumberOfClaimsToLog");
        }
        if (!config.contains("NumberOfClaimsFlag.ClaimDataDirectory")) {
            config.set("NumberOfClaimsFlag.ClaimDataDirectory", new File("plugins/GriefPreventionData/ClaimData/").getAbsolutePath());
            notFoundInConfigMessage("NumberOfClaimsFlag.ClaimDataDirectory");
        }

        // ----------------------------------------------------------------------------------------------------

        // CommandOnJoin

        if (!config.contains("CommandOnJoin")) {
            List<String> playersAndCommands = new ArrayList<>();
            playersAndCommands.add("Player:playerNameGoesHere Command:commandGoesHere");
            config.set("CommandOnJoin", playersAndCommands);
            notFoundInConfigMessage("CommandOnJoin");
        }

        // ----------------------------------------------------------------------------------------------------

        // NamedMobClaimDamage

        if (!config.contains("NamedMobClaimDamage.Names")) {
            List<String> names = new ArrayList<>();
            names.add("Muted");
            names.add("Silent");
            names.add("Silence me");
            names.add("Protected");
            names.add("Protect me");
            config.set("NamedMobClaimDamage.Names", names);
            notFoundInConfigMessage("NamedMobClaimDamage.Names");
        }

        // ----------------------------------------------------------------------------------------------------

        // AdditionalLogs

        if (!config.contains("AdditionalLogs")) {

            List<String> logNamesAndPaths = new ArrayList<>();

            String sellItemsLogName = "SellItems";
            String sellItemsLogPath = new Methods().getServerJarPath() + "logs/SellItems";
            logNamesAndPaths.add("Name:" + sellItemsLogName + " " + "Path:" + sellItemsLogPath);

            String shopLogName = "Shop";
            String shopLogPath = new Methods().getServerJarPath() + "logs/Shop";
            logNamesAndPaths.add("Name:" + shopLogName + " " + "Path:" + shopLogPath);

            String moneyLogName = "Money";
            String moneyLogPath = new Methods().getServerJarPath() + "plugins/CMI/moneyLog";
            logNamesAndPaths.add("Name:" + moneyLogName + " " + "Path:" + moneyLogPath);

            config.set("AdditionalLogs", logNamesAndPaths);
            notFoundInConfigMessage("AdditionalLogs");

        }

        // ----------------------------------------------------------------------------------------------------

        Main.getInstance().saveConfig();

    }

    static void notFoundInConfigMessage(String string){

        Main.getInstance().getLogger().info(string + " not found in the config, creating it now.");

    }

}
