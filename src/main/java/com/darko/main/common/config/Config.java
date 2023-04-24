package com.darko.main.common.config;

import com.darko.main.AlttdUtility;
import com.darko.main.common.Methods;
import com.darko.main.darko.illegalItemCheck.IllegalItem;
import com.darko.main.darko.logging.Logging;
import com.darko.main.darko.logging.logs.Log;
import org.bukkit.configuration.file.FileConfiguration;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Config {

    private enum Messages {

        NoPermission("Messages.NoPermission", "&cYou do not have permission to do this."), SeatNoClaimPerm("Messages.SeatNoClaimPerm", "&cYou don't have %player%'s permission to use that."), SeatNoRegionPerm("Messages.SeatNoRegionPerm", "&cYou can't sit in this region."), HatNoItem("Messages.HatNoItem", "&eHold the item that you wish to put on your head."), HatEquipped("Messages.HatEquipped", "&aItem equipped."), HatSwapped("Messages.HatSwapped", "&aItems swapped."), HatCurseOfBinding("Messages.HatCurseOfBinding", "&cCan't take off an item that has the &d&oCurse of Binding&r&c."), OfflinePlayerPayment("Messages.OfflinePlayerPayment", "&cYou can't send money to offline players."), ListGroup("Messages.ListGroup", "&eOnline players for the group &b%group%&6:"), AutofixEnabled("Messages.AutofixEnabled", "&aAutofix enabled."), AutofixDisabled("Messages.AutofixDisabled", "&cAutofix disabled."), PlayerNotFound("Messages.PlayerNotFound", "&cPlayer not found."), GroupNotFound("Messages.GroupNotFound", "&cNo online players in that group."), DeathMessage("Messages.DeathMessage", "&cUse &6/dback&c to go back to your previous death location."), InvalidPrefixLengthMessage("Messages.InvalidPrefixLengthMessage", "&cA prefix cannot be longer than 10 characters."), PrefixSetConfirmedMessage("Messages.PrefixSetConfirmedMessage", "&ePrefix &6%prefix%&e set for &6%player%&e."), PrefixRemovedConfirmedMessage("Messages.PrefixRemovedConfirmedMessage", "&ePrefix removed for &6%player%&e."), ServermsgInvalidUsage("Messages.ServermsgInvalidUsage", "&cUsage of this command is /servermsg <player/permission> <message>. You can use color codes and \\n for a new line."), NoDatabaseConnectionMessage("Messages.NoDatabaseConnectionMessage", "&cThis feature is unavailable when there is no database connection."), PlayerOnlyCommandMessage("Messages.PlayerOnlyCommandMessage", "&cThis command can only be performed by a player."), BlockItemPickupEnabledMessage("Messages.BlockItemPickupEnabledMessage", "&aBlock item pickup enabled."), BlockItemPickupDisabledMessage("Messages.BlockItemPickupDisabledMessage", "&cBlock item pickup disabled."), CrashCommandInvalidUsage("Messages.CrashCommandInvalidUsage", "&cUsage of this command is /crash <player>."), CrashCommandOfflinePlayer("Messages.CrashCommandOfflinePlayer", "&cThe player you are trying to crash is offline."), RebootWhitelistKickMessage("Messages.RebootWhitelistKickMessage", "&fThe server is rebooting, you will be able to join shortly."), IncorrectUsageSearchLogsCommand("Messages.IncorrectUsageSearchLogsCommand", "&cUsage of this command is /searchlogs <normal/special/additional>."), IncorrectUsageSearchNormalLogsCommand("Messages.IncorrectUsageSearchNormalLogsCommand", "&cUsage of this command is /searchlogs normal <numberOfDays> <search-string>."), IncorrectUsageSearchSpecialLogsCommand("Messages.IncorrectUsageSearchSpecialLogsCommand", "&cWrong usage of this command, honestly just check the guide on how to use it, I can't explain it in one message..."), IncorrectUsageSearchAdditionalLogsCommand("Messages.IncorrectUsageSearchAdditionalLogsCommand", "&cUsage of this command is /searchlogs additional <logName> <numberOfDays> <search-string>."), SitCommandNotOnGroundMessage("Messages.SitCommandNotOnGroundMessage", "&cYou must be standing on the ground to do this command."), SeatOccupiedMessage("Messages.SeatOccupiedMessage", "&cThat seat is occupied."), SeatInvalidBlock("Messages.SeatInvalidBlock", "&cInvalid block."), SeatInvalidBlockAbove("Messages.SeatInvalidBlockAbove", "&cInvalid block above."), SeatInvalidBlockBelow("Messages.SeatInvalidBlockBelow", "&cInvalid block below."), GriefPreventionNoBuildPermMessage("Messages.GriefPreventionNoBuildPermMessage", "&cYou don't have %player%'s permission to build here."), GriefPreventionThatBelongsToMessage("Messages.GriefPreventionThatBelongsToMessage", "&cThat belongs to %player%."), InvalidUsageCommandOnJoinMessage("Messages.InvalidUsageCommandOnJoinMessage", "&cUsage of this command is /commandonjoin <player> <command>."), CommandOnJoinSetMessage("Messages.CommandOnJoinSetMessage", "&aSet command: %command% for player: %player%."), CustomCommandMacroUsage("Messages.CustomCommandMacroUsage", "&cUsage of this command is\n/ccm <add/edit> <macroName> <command>\nor /ccm remove <macroName>\nor /ccm <macroName>"), CustomCommandMacroSavedMessage("Messages.CustomCommandMacroSavedMessage", "&aMacro saved. MacroName: %macroName% Command: %command%"), CustomCommandMacroAlreadyExists("Messages.CustomCommandMacroAlreadyExists", "&cA macro with that name already exists."), CustomCommandMacroDoesntExist("Messages.CustomCommandMacroDoesntExist", "&cA macro with that name doesn't exist."), CustomCommandMacroRemovedMessage("Messages.CustomCommandMacroRemovedMessage", "&aMacro removed."), CustomCommandMacroEdited("Messages.CustomCommandMacroEdited", "&aMacro edited."), CustomCommandMacroBlacklistedCommand("Messages.CustomCommandMacroBlacklistedCommand", "&cYou can't make a macro with that command."), FreezeMailSuccessfullySend("Messages.FreezeMailSuccessfullySend", "&aSuccessfully sent freezemail to %player%!"), FreezeMailPlayerDoesntExist("Messages.FreezeMailPlayerDoesntExist", "&c%target% Is not a valid player!"), FreezeMailSuccessfullyCompleted("Messages.FreezeMailSuccessfullyCompleted", "&aThank you! You are now able to move and talk again!"), FreezeMailNotAcceptedYet("Messages.FreezeMailNotAcceptedYet", "You won't be able to move, speak, or send commands until you've acknowledged you've read the following messages and will do as they say. To acknowledge these messages, type &6I read the message &r"), FreezeMailTitle("Messages.FreezeMailTitle", "&6Read your messages"), FreezeMailSubTitle("Messages.FreezeMailSubTitle", "&aYou won't be able to move until you do"), FreezeMailListRead("Messages.FreezeMailListRead", "&fShowing all unread freeze mails for: &d%player%&f. To see all mails do &6/freezemail list %player% all&f:"), FreezeMailListAll("Messages.FreezeMailListAll", "&fShowing all freeze mails for &d%player%&f:"), FreezeMailListAllUnread("Messages.FreezeMailListAllUnread", "&fShowing all unread freeze mails:"), CantFindPlayer("Messages.CantFindPlayer", "&cCould not find %playerName%&c try again on a server they've played on before."), GodModeEnabled("Messages.GodModeEnabled", "&aGodMode enabled."), GodModeDisabled("Messages.GodModeDisabled", "&cGodMode disabled."), PetGodModeEnabled("Messages.PetGodModeEnabled", "&aPetGodMode enabled."), PetGodModeDisabled("Messages.PetGodModeDisabled", "&cPetGodMode disabled."), InvalidUsageClaimPatrolCommand("Messages.InvalidUsageClaimPatrolCommand", "&cThe usage for this command is /claimpatrol <owner/trust> <user> <number(optional)>."), PlayerHasNotJoinedBefore("Messages.PlayerHasNotJoinedBefore", "&cThat player has not joined before."), NoClaimsToPatrol("Messages.NoClaimsToPatrol", "&cNo claims to patrol!"), TrappedCommandOnCooldown("Messages.TrappedCommandOnCooldown", "&cThat command is on a cooldown for %time%!"), InvalidUsageTPPunchCommand("Messages.InvalidUsageTPPunchCommand", "&cUsage of this command is /tppunch <x> <y> <z> <dimension(optional)>."), ValidUsageTPPunchCommand("Messages.ValidUsageTPPunchCommand", "&aThe next thing you punch will be teleported to %location%."), TPPunchCancelled("Messages.TPPunchCancelled", "&cTeleport punch cancelled."), EntityTeleportedTPPunchCommand("Messages.EntityTeleportedTPPunchCommand", "&aEntity teleported to %location%."), JoinLimiterCantJoin("Messages.JoinLimiterCantJoin", "&fYou've been joining this server too much, please wait %time% before joining again."), JoinLimiterJoinWarning("Messages.JoinLimiterJoinWarning", "&cYou've been joining this server too much, you'll have to wait %time% to join again."), InvalidUsageCommandUsageCommand("Messages.InvalidUsageCommandUsageCommand", "&cUsage of this command is /commandusage <user> <numberOfDays>."), BlockedBlocksCantPlace("Messages.BlockedBlocksCantPlace", "&cYou are not worthy!"), CrazyCratesKeysLimiterAtLimitMinusOne("Messages.CrazyCratesKeysLimiterAtLimitMinusOne", "&7You can store &61 &7more key for that crate."), CrazyCratesKeysLimiterAtLimit("Messages.CrazyCratesKeysLimiterAtLimit", "&cYou are at the key limit for that crate, use a key or the next one you get will be deleted."), CrazyCratesKeysLimiterOverLimit("Messages.CrazyCratesKeysLimiterOverLimit", "&cYou are over the key limit for that crate, key deleted."), NumberOfClaimsLimiterAtLimit("Messages.NumberOfClaimsLimiterAtLimit", "&cYou are at the limit of the number of claims you can have."), NumberOfClaimsLimiterOverLimit("Messages.NumberOfClaimsLimiterOverLimit", "&cYou are over the limit of the number of claims you can have. You will have to remove a claim if you wish to make more claims."), SaveItemEmptyHand("Messages.SaveItemEmptyHand", "&cYou need to hold an item in your main hand to save it."), SaveItemSaved("Messages.SaveItemSaved", "&aItem saved with the ID: %ID%"), MagnetEnabled("Messages.MagnetEnabled", "&aMagnet enabled"), MagnetDisabled("Messages.MagnetDisabled", "&cMagnet disabled"), InvalidUsageFindItemCommand("Messages.InvalidUsageFindItemCommand", "&cUsage of this command is /finditem <item>."), FindItemCommandInvalidItem("Messages.FindItemCommandInvalidItem", "&cInvalid item."), FindItemCommandOnCooldown("Messages.FindItemCommandOnCooldown", "&cThat command is on a cooldown for %seconds% seconds."), StorePetOnPVPPetStored("Messages.StorePetOnPVPPetStored", "&cPet stored due to enabling PvP. If you die while it's out it'll drop it's inventory.");

        private final String path;
        private final String message;

        Messages(String path, String message) {
            this.path = path;
            this.message = message;
        }

    }

    public static void configSetup() {

        FileConfiguration config = AlttdUtility.getInstance().getConfig();

        // Toggles

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
        toggles.add("CustomCommandMacroCommand");
        toggles.add("PreventNoPvPFishing");
        toggles.add("AllowNamedPublicChests");
        toggles.add("AllowNamedPublicVillagers");
        toggles.add("ProtectTNTArrowDamage");
        toggles.add("FreezeMail");
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
        toggles.add("JoinLimiter");
        toggles.add("CommandUsageCommand");
        toggles.add("BlockedBlocks");
        toggles.add("IllegalItemCheck");
        toggles.add("CrazyCratesKeysLimiter");
        toggles.add("NumberOfClaimsLimiter");
        toggles.add("InvsaveOnPlayerQuit");
        toggles.add("SaveItemCommand");
        toggles.add("ViewSavedItemsCommand");
        toggles.add("MagnetCommand");
        toggles.add("PreventChannelingWhenPvPOff");
        toggles.add("FindItemCommand");
        toggles.add("DatabaseVillagerShopLog");
        // toggles.add("DatabaseMoneyLog");
        toggles.add("BalStatsCommand");
        toggles.add("StorePetOnPVP");

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

        // LavaSponge

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

        // -----------------------------------------------------------------------------------------------------

        // Cooldown command

        if (!config.contains("CooldownCommandPermissions")) {

            List<String> permissions = new ArrayList<>();
            permissions.add("Permission:rtp.no Name:RTP");

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

        for (Map.Entry<String, Log> entry : Logging.getCachedLogs().entrySet()) {
            String checkedPath = "Logging." + entry.getValue().getName() + ".Enabled";
            if (!config.contains(checkedPath)) {
                config.set(checkedPath, entry.getValue().isEnabled());
                notFoundInConfigMessage(checkedPath);
            }
            checkedPath = "Logging." + entry.getValue().getName() + ".DaysOfLogsToKeep";
            if (!config.contains(checkedPath)) {
                config.set(checkedPath, entry.getValue().getDaysOfLogsToKeep());
                notFoundInConfigMessage(checkedPath);
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
            config.set("Database.driver", "mysql");
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

            String moneyLogName = "Money";
            String moneyLogPath = new Methods().getServerJarPath() + "plugins/CMI/moneyLog";
            logNamesAndPaths.add("Name:" + moneyLogName + " " + "Path:" + moneyLogPath);

            config.set("AdditionalLogs", logNamesAndPaths);
            notFoundInConfigMessage("AdditionalLogs");

        }

        // ----------------------------------------------------------------------------------------------------

        // TimedTips

        if (!config.contains("TimedTips.Delay")) {
            config.set("TimedTips.Delay", 900);
            notFoundInConfigMessage("TimedTips.Delay");
        }
        if (!config.contains("TimedTips.Prefix")) {
            config.set("TimedTips.Prefix", "&f====================");
            notFoundInConfigMessage("TimedTips.Prefix");
        }
        if (!config.contains("TimedTips.Suffix")) {
            config.set("TimedTips.Suffix", "&f====================");
            notFoundInConfigMessage("TimedTips.Suffix");
        }
        if (!config.contains("TimedTips.Tips")) {

            List<String> tips = new ArrayList<>();

            tips.add("This is what a tip is supposed to look like!");

            config.set("TimedTips.Tips", tips);
            notFoundInConfigMessage("TimedTips.Tips");
        }

        // ----------------------------------------------------------------------------------------------------

        // Trapped command

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

        // JoinLimiter

        if (!config.contains("JoinLimiter.JoinLimit")) {
            config.set("JoinLimiter.JoinLimit", 5);
            notFoundInConfigMessage("JoinLimiter.JoinLimit");
        }
        if (!config.contains("JoinLimiter.TimeLimit")) {
            config.set("JoinLimiter.TimeLimit", 300);
            notFoundInConfigMessage("JoinLimiter.TimeLimit");
        }


        // ----------------------------------------------------------------------------------------------------

        // BlockBlockPlace

        if (!config.contains("BlockBlockPlace.BlockedBlocks")) {

            List<String> blockedBlocks = new ArrayList<>();
            blockedBlocks.add("bedrock");
            config.set("BlockBlockPlace.BlockedBlocks", blockedBlocks);

            notFoundInConfigMessage("BlockBlockPlace.BlockedBlocks");
        }

        // ----------------------------------------------------------------------------------------------------

        // IllegalItemCheck

        if (!config.contains("IllegalItemCheckWhitelistedWorlds")) {

            List<String> worlds = new ArrayList<>();
            worlds.add("staff");
            config.set("IllegalItemCheckWhitelistedWorlds", worlds);

            notFoundInConfigMessage("IllegalItemCheckWhitelistedWorlds");
        }

        if (!config.contains("IllegalItemCheck")) {

            IllegalItem illegalItem = new IllegalItem("IllegalItem", "", "", "illegal", "", -1);

            config.createSection("IllegalItemCheck." + illegalItem.getName());

            config.set("IllegalItemCheck." + illegalItem.getName() + ".Material", illegalItem.getItemMaterial());
            config.set("IllegalItemCheck." + illegalItem.getName() + ".Name", illegalItem.getItemName());
            config.set("IllegalItemCheck." + illegalItem.getName() + ".Lore", illegalItem.getItemLore());
            config.set("IllegalItemCheck." + illegalItem.getName() + ".Enchant", illegalItem.getItemEnchant());
            config.set("IllegalItemCheck." + illegalItem.getName() + ".ReplaceWithID", illegalItem.getReplaceWithID());

            notFoundInConfigMessage("IllegalItemCheck");
        }

        // ----------------------------------------------------------------------------------------------------

        // CrazyCratesKeysLimiter

        if (!config.contains("CrazyCratesKeysLimiter")) {

            config.set("CrazyCratesKeysLimiter.dailyvotecrate.KeyLimit", 7);
            config.set("CrazyCratesKeysLimiter.weeklyvotecrate.KeyLimit", 1);
            config.set("CrazyCratesKeysLimiter.questcrate.KeyLimit", 2);

            notFoundInConfigMessage("CrazyCratesKeysLimiter");
        }

        // ----------------------------------------------------------------------------------------------------

        // CustomCommandMacro

        if (!config.contains("CustomCommandMacro.BlacklistedCommands")) {

            List<String> blacklistedCommands = List.of("msg", "ac", "acg", "p", "tell", "r", "reply", "message");
            config.set("CustomCommandMacro.BlacklistedCommands", blacklistedCommands);

        }

        // ----------------------------------------------------------------------------------------------------

        // SavedItems

        if (!config.contains("SavedItems")) {

            config.createSection("SavedItems");

        }

        // ----------------------------------------------------------------------------------------------------

        // FindItem

        if (!config.contains("FindItem.Radius")) {
            config.set("FindItem.Radius", 10);
        }
        if (!config.contains("FindItem.Cooldown")) {
            config.set("FindItem.Cooldown", 15);
        }
        if (!config.contains("FindItem.ItemLimit")) {
            config.set("FindItem.ItemLimit", 10);
        }
        if (!config.contains("FindItem.AllowedMilisecondsPerTick")) {
            config.set("FindItem.AllowedMilisecondsPerTick", 10);
        }

        // ----------------------------------------------------------------------------------------------------

        AlttdUtility.getInstance().saveConfig();

    }

    static void notFoundInConfigMessage(String string) {

        AlttdUtility.getInstance().getLogger().info(string + " not found in the config, creating it now.");

    }

}
