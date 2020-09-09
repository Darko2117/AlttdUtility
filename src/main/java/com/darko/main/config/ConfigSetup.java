package com.darko.main.config;

import java.io.File;
import java.util.*;

import com.darko.main.utilities.logging.Logging;
import org.bukkit.configuration.file.FileConfiguration;

import com.darko.main.Main;

public class ConfigSetup {

    private enum Messages {

        NoPermission("Messages.NoPermission", "&cYou do not have permission to do this."),
        ChairEnabled("Messages.ChairEnabled", "&aChair mode enabled, right click on any stairs to sit on them."),
        ChairDisabled("Messages.ChairDisabled", "&cChair mode disabled."),
        InvalidChairBlock("Messages.InvalidChairBlock", "&cInvalid block found above/below the stairs."),
        ChairNoClaimPerm("Messages.ChairNoClaimPerm", "&cYou don't have %player%'s permission to use that."),
        ChairNoRegionPerm("Messages.ChairNoRegionPerm", "&cYou can't sit in this region."),
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
        IncorrectUsageSearchLogsCommand("Messages.IncorrectUsageSearchLogsCommand", "&cUsage of this command is /searchlogs <logName> <numberOfDays> <Argument1Name: Argument1> <Argument2Name: Argument2> <Argument3Name: Argument3>... Just follow what tab complete is giving you.");

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
        toggles.add("Chair");
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
        toggles.add("SearchLogsCommand");

        for (String string : toggles) {
            if (!config.contains("FeatureToggles." + string)) config.set("FeatureToggles." + string, true);
        }

        // ----------------------------------------------------------------------------------------------------

        // Messages
        for (Messages message : Messages.values()) {
            if (!config.contains(message.path)) {
                config.set(message.path, message.message);
                Main.getInstance().getLogger().info(message.path + " not found in the config, creating it now.");
            }
        }

        // ----------------------------------------------------------------------------------------------------

        // Cooldown command
        if (!config.contains("CooldownCommandPermissions")) {

            List<String> permissions = new ArrayList<>();
            permissions.add("Permission:rtp.no Name:RTP");
            permissions.add("Permission:keyshop.buy Name:SuperCrate");

            config.set("CooldownCommandPermissions", permissions);

            Main.getInstance().getLogger().info("CooldownCommandPermissions not found in the config, creating it now.");

        }

        // ----------------------------------------------------------------------------------------------------

        // List groups
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

            Main.getInstance().getLogger().info("ListGroups not found in the config, creating it now.");
        }

        // ----------------------------------------------------------------------------------------------------

        // List groups
        if (!config.contains("PrefixAvailableGroups")) {

            List<String> groups = new ArrayList<>();

            groups.add("archduke");

            config.set("PrefixAvailableGroups", groups);

            Main.getInstance().getLogger().info("PrefixAvailableGroups not found in the config, creating it now.");

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

            Main.getInstance().getLogger().info("SpawnLimiter not found in the config, creating it now.");

        }

        // ----------------------------------------------------------------------------------------------------

        // Logging

        for (Map.Entry<String, String> entry : Logging.logNamesAndConfigPaths.entrySet()) {

            if (!config.contains(entry.getValue())) {

                config.set(entry.getValue() + ".Enabled", true);
                config.set(entry.getValue() + ".NumberOfLogsToKeep", 365);

                Main.getInstance().getLogger().info(entry.getValue() + " not found in the config, creating it now.");

            }

        }

        // ----------------------------------------------------------------------------------------------------

        // SearchLogs

        if (!config.contains("SearchLogs")) {

            config.set("SearchLogs.OutputPath", new File(Main.getInstance().getDataFolder() + "/search-output/").getAbsolutePath());

            Main.getInstance().getLogger().info("SearchLogs path not found in the config, creating it now.");

        }

        // ----------------------------------------------------------------------------------------------------

        // DatabaseInitiate
        if (!config.contains("Database")) {

            config.set("Database.driver", "mariadb");
            config.set("Database.ip", "localhost");
            config.set("Database.port", "3306");
            config.set("Database.name", "alttdutility");
            config.set("Database.username", "root");
            config.set("Database.password", "");

            Main.getInstance().getLogger().info("Database info not found in the config, creating it now.");

        }

        // ----------------------------------------------------------------------------------------------------

        Main.getInstance().saveConfig();

    }

}
