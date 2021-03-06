package com.darko.main.common.config;

import com.darko.main.AlttdUtility;
import com.darko.main.common.Methods;
import com.darko.main.darko.logging.Logging;
import org.bukkit.configuration.file.FileConfiguration;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ConfigSetup {

    private enum Messages {

        NoPermission("Messages.NoPermission", "&cYou do not have permission to do this."),
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
        IncorrectUsageSearchSpecialLogsCommand("Messages.IncorrectUsageSearchSpecialLogsCommand", "&cWrong usage of this command, honestly just check the guide on how to use it, I can't explain it in one message..."),
        IncorrectUsageSearchAdditionalLogsCommand("Messages.IncorrectUsageSearchAdditionalLogsCommand", "&cUsage of this command is /searchlogs additional <logName> <numberOfDays> <search-string>."),
        SitCommandNotOnGroundMessage("Messages.SitCommandNotOnGroundMessage", "&cYou must be standing on the ground to do this command."),
        SeatOccupiedMessage("Messages.SeatOccupiedMessage", "&cThat seat is occupied."),
        SeatInvalidBlock("Messages.SeatInvalidBlock", "&cYou can't sit on that block."),
        GriefPreventionNoBuildPermMessage("Messages.GriefPreventionNoBuildPermMessage", "&cYou don't have %player%'s permission to build here."),
        GriefPreventionThatBelongsToMessage("Messages.GriefPreventionThatBelongsToMessage", "&cThat belongs to %player%."),
        InvalidUsageCommandOnJoinMessage("Messages.InvalidUsageCommandOnJoinMessage", "&cUsage of this command is /commandonjoin <player> <command>."),
        CommandOnJoinSetMessage("Messages.CommandOnJoinSetMessage", "&aSet command: %command% for player: %player%."),
        CustomChatMessageUsage("Messages.CustomChatMessageUsage", "&cUsage of this command is\n/ccm <add/edit> <messageName> <message>\nor /ccm remove <messageName>\nor /ccm <messageName>."),
        CustomChatMessageSavedMessage("Messages.CustomChatMessageSavedMessage", "&aMessage saved. MessageName: %messageName% Message: %message%"),
        CustomChatMessageAlreadyExists("Messages.CustomChatMessageAlreadyExists", "&cA custom message with that name already exists, please chose another one."),
        CustomChatMessageDoesntExist("Messages.CustomChatMessageDoesntExist", "&cA custom message with that name doesn't exist."),
        CustomChatMessageRemovedMessage("Messages.CustomChatMessageRemovedMessage", "&aMessage removed."),
        CustomChatMessageEdited("Messages.CustomChatMessageEdited", "&aMessage edited."),
        FreezeMailSuccessfullySend("Messages.FreezeMailSuccessfullySend", "&aSuccessfully sent freezemail to %player%!"),
        FreezeMailPlayerDoesntExist("Messages.FreezeMailPlayerDoesntExist", "&c%target% Is not a valid player!"),
        FreezeMailSuccessfullyCompleted("Messages.FreezeMailSuccessfullyCompleted", "&aThank you! You are now able to move and talk again!"),
        FreezeMailNotAcceptedYet("Messages.FreezeMailNotAcceptedYet", "You won't be able to move, speak, or send commands until you've acknowledged you've read the following messages and will do as they say. To acknowledge these messages, type &6I read the message &r"),
        FreezeMailTitle("Messages.FreezeMailTitle", "&6Read your messages"),
        FreezeMailSubTitle("Messages.FreezeMailSubTitle", "&aYou won't be able to move until you do"),
        FreezeMailListRead("Messages.FreezeMailListRead", "&fShowing all unread freeze mails for: &d%player%&f. To see all mails do &6/freezemail list %player% all&f:"),
        FreezeMailListAll("Messages.FreezeMailListAll", "&fShowing all freeze mails for &d%player%&f:"),
        FreezeMailListAllUnread("Messages.FreezeMailListAllUnread", "&fShowing all unread freeze mails:"),
        NickChanged("Messages.NickChanged", "&eYour nickname was changed to %nickname%&e."),
        NickNotChanged("Messages.NickNotChanged", "&eYour nickname request was denied."),
        NickReset("Messages.NickReset", "&eNickname changed back to normal."),
        NickChangedOthers("Messages.NickChangedOthers", "&6%targetplayer%&e's nickname was changed to %nickname%&e."),
        NickTargetNickChange("Messages.NickTargetNickChange", "&eYour nickname was changed to %nickname% &eby %sendernick%&e"),
        NickResetOthers("Messages.NickResetOthers", "&6%player%&6's &enickname was reset back to normal."),
        NickInvalidCharacters("Messages.NickInvalidCharacters", "&eYou can only use letters and numbers in nicknames."),
        NickLengthInvalid("Messages.NickLengthInvalid", "&eNicknames need to be between 3 to 16 characters long."),
        NickPlayerNotOnline("Messages.NickPlayerNotOnline", "&cThat player is not online."),
        NickBlockedColorCodes("Messages.NickBlockedColorCodes", "&eYou have blocked color codes in that nickname."),
        NickUserNotFound("Messages.NickUserNotFound", "&cFailed to set nickname from player, try again from a server this player has been on before."),
        NickAccepted("Messages.NickAccepted", "&aYou accepted %targetPlayer%&a's nickname. They are now called %newNick%&a."),
        NickDenied("Messages.NickDenied", "&aYou denied %targetPlayer%&a's nickname. They are still called %oldNick%&a."),
        NickAlreadyHandled("Messages.NickAlreadyHandled", "&c%targetPlayer%&c's nickname was already accepted or denied."),
        NickNoLuckperms("Messages.NickNoLuckPerms", "&cDue to an issue with LuckPerms /nick try won't work at the moment."),
        NickTooSoon("Messages.NickTooSoon", "&cPlease wait %time%&c until requesting a new nickname"),
        NickRequestReplaced("Messages.NickRequestReplaced", "&aReplaced your previous request %oldRequestedNick%&a with %newRequestedNick%&a."),
        NickNewRequest("Messages.NickNewRequest", "&aNew nickname request by %player%&a!"),
        NickTryout("Messages.NickTryout", "&f%prefix&f %nick%&7: &fHi, this is what my new nickname could look like!"),
        CantFindPlayer("Messages.CantFindPlayer", "&cCould not find %playerName%&c try again on a server they've played on before."),
        NickRequested("Messages.NickRequested", "&aYour requested to be nicknamed %nick%&a has been received. Staff will accept or deny this request asap!"),
        NickReviewWaiting("Messages.NickReviewWaiting", "&aThere are %amount% nicknames waiting for review!"),
        NickTaken("Messages.NickTaken", "&cSomeone else already has this nickname, or has this name as their username."),
        NickRequestsOnLogin("Messages.NickRequestsOnLogin", "&aCurrent nick requests: %amount%"),
        GodModeEnabled("Messages.GodModeEnabled", "&aGodMode enabled."),
        GodModeDisabled("Messages.GodModeDisabled", "&cGodMode disabled."),
        PetGodModeEnabled("Messages.PetGodModeEnabled", "&aPetGodMode enabled."),
        PetGodModeDisabled("Messages.PetGodModeDisabled", "&cPetGodMode disabled."),
        InvalidUsageClaimPatrolCommand("Messages.InvalidUsageClaimPatrolCommand", "&cThe usage for this command is /claimpatrol <owner/trust> <user> <number(optional)>."),
        PlayerHasNotJoinedBefore("Messages.PlayerHasNotJoinedBefore", "&cThat player has not joined before."),
        NoClaimsToPatrol("Messages.NoClaimsToPatrol", "&cNo claims to patrol!"),
        TrappedCommandOnCooldown("Messages.TrappedCommandOnCooldown", "&cThat command is on a cooldown for %time%!"),
        InvalidUsageTPPunchCommand("Messages.InvalidUsageTPPunchCommand", "&cUsage of this command is /tppunch <x> <y> <z> <dimension(optional)>."),
        ValidUsageTPPunchCommand("Messages.ValidUsageTPPunchCommand", "&aThe next thing you punch will be teleported to %location%."),
        TPPunchCancelled("Messages.TPPunchCancelled", "&cTeleport punch cancelled."),
        EntityTeleportedTPPunchCommand("Messages.EntityTeleportedTPPunchCommand", "&aEntity teleported to %location%.");

        private final String path;
        private final String message;

        Messages(String path, String message) {
            this.path = path;
            this.message = message;
        }

    }

    public static void configSetup() {

        FileConfiguration config = AlttdUtility.getInstance().getConfig();

        //Toggles

        List<String> toggles = new ArrayList<>();
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
        toggles.add("CustomChatMessageCommand");
        toggles.add("PreventNoPvPFishing");
        toggles.add("AllowNamedPublicChests");
        toggles.add("AllowNamedPublicVillagers");
        toggles.add("ProtectTNTArrowDamage");
        toggles.add("FreezeMail");
        toggles.add("Nicknames");
        toggles.add("GodModeCommand");
        toggles.add("PetGodModeCommand");
        toggles.add("BlockRavagerDestroyingBlocksInClaim");
        toggles.add("DisablePvpOnLeave");
        toggles.add("ClaimPatrolCommand");
        toggles.add("TimedTips");
        toggles.add("ToggleScruffCommand");
        toggles.add("JoinNotifications");
        toggles.add("BlockEggThrowingInClaimsWithoutTrust");
        toggles.add("BlockSignEditIfShop");
        toggles.add("BlockChorusFruitArrowBreakingInClaim");
        toggles.add("BlockChorusFruitArrowBreakingOnPlot");
        toggles.add("TrappedCommand");
        toggles.add("TPPunchCommand");
        toggles.add("BlockWitherBlockAndEntityDamageOutsideClaim");

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
                config.set(entry.getValue() + ".NumberOfLogsToKeep", 60);

                notFoundInConfigMessage(entry.getValue());

            }

        }

        // ----------------------------------------------------------------------------------------------------

        // SearchLogs

        if (!config.contains("SearchLogs.OutputPath")) {
            config.set("SearchLogs.OutputPath", new File(AlttdUtility.getInstance().getDataFolder() + "/search-output/").getAbsolutePath());
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

        // NamedMobClaimDamage

        if (!config.contains("NamedMobClaimDamage.Names")) {
            List<String> names = new ArrayList<>();
            names.add("Muted");
            names.add("Silent");
            names.add("Silence me");
            names.add("Silenced");
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

        // Nicknames

        if (!config.contains("Nicknames.Lore")) {
            List<String> lore = new ArrayList<>();

            lore.add("&bNew nick: %newNick%");
            lore.add("&bOld nick: %oldNick%");
            lore.add("&bLast changed: %lastChanged%");
            lore.add("&aLeft click to Accept &d| &cRight click to Deny");

            config.set("Nicknames.Lore", lore);

            notFoundInConfigMessage("Nicknames.Lore");
        }

        if (!config.contains("Nicknames.WaitTime")) {
            config.set("Nicknames.WaitTime", 86400000);
            notFoundInConfigMessage("Nicknames.WaitTime");
        }

        if (!config.contains("Nicknames.BlockedColorCodes")) {
            config.set("Nicknames.BlockedColorCodes", new String[]{"&k", "&l", "&n", "&m", "&o"});
            notFoundInConfigMessage("Nicknames.BlockedColorCodes");
        }

        if (!config.contains("Nicknames.AllowedColorCodes")) {
            config.set("Nicknames.AllowedColorCodes", new String[]{"&0", "&1", "&2", "&3", "&4", "&5", "&6", "&7", "&8", "&9", "&a", "&b", "&c", "&d", "&e", "&f", "&r"});
            notFoundInConfigMessage("Nicknames.AllowedColorCodes");
        }

        // ----------------------------------------------------------------------------------------------------

        //TimedTips

        if (!config.contains("TimedTips.Delay")) {
            config.set("TimedTips.Delay", 15);
            notFoundInConfigMessage("TimedTips.Delay");
        }
        if (!config.contains("TimedTips.Messages")) {

            List<String> messages = new ArrayList<>();
            String message = "";

            message = message.concat("&f====================\\n");
            message = message.concat("&fThis is what a tip is supposed to look like!\\n");
            message = message.concat("&f====================");

            messages.add(message);

            config.set("TimedTips.Messages", messages);
            notFoundInConfigMessage("TimedTips.Messages");
        }

        // ----------------------------------------------------------------------------------------------------

        //Trapped command

        if (!config.contains("Trapped.CommandToExecute")) {
            config.set("Trapped.CommandToExecute", "cmi spawn %player%");
            notFoundInConfigMessage("Trapped.CommandToExecute");
        }
        if (!config.contains("Trapped.Cooldown")) {
            config.set("Trapped.Cooldown", 600);
            notFoundInConfigMessage("Trapped.Cooldown");
        }
        if (!config.contains("Trapped.SecondsToTeleport")) {
            config.set("Trapped.SecondsToTeleport", 15);
            notFoundInConfigMessage("Trapped.SecondsToTeleport");
        }

        // ----------------------------------------------------------------------------------------------------

        AlttdUtility.getInstance().saveConfig();

    }

    static void notFoundInConfigMessage(String string) {

        AlttdUtility.getInstance().getLogger().info(string + " not found in the config, creating it now.");

    }

}
