package com.darko.main.utilities.config;

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
        HatEquipped("Messages.HatEquipped", "&aItem equipped."), HatSwapped("Messages.HatSwapped", "&aItems swapped."),
        HatCurseOfBinding("Messages.HatCurseOfBinding",
                "&cCan't take off an item that has the &d&oCurse of Binding&r&c."),
        OfflinePlayerPayment("Messages.OfflinePlayerPayment", "&cYou can't send money to offline players."),
        NoCooldowns("Messages.NoCooldowns", "&eYou have no cooldowns right now."),
        ListGroup("Messages.ListGroup", "&eOnline players for the group &b%group%&6:"),
        NetherEntry("Messages.NetherEntry", "&cWhen exiting the nether you will be teleported to spawn."),
        NetherExit("Messages.NetherExit", "&cYou have been teleported to spawn."),
        AutoFixEnabled("Messages.AutoFixEnabled", "&aAuto repair enabled."),
        AutoFixDisabled("Messages.AutoFixDisabled", "&cAuto repair disabled."),
        PlayerNotFound("Messages.PlayerNotFound", "&cPlayer not found."),
        GroupNotFound("Messages.GroupNotFound", "&cNo online players in that group."),
        DeathMessage("Messages.DeathMessage", "&cUse &6/dback&c to go back to your previous death location."),
        InvalidPrefixLengthMessage("Messages.InvalidPrefixLengthMessage", "&cA prefix cannot be longer than 10 characters."),
        PrefixSetConfirmedMessage("Messages.PrefixSetConfirmedMessage", "&ePrefix &6%prefix%&e set for &6%player%&e."),
        PrefixRemovedConfirmedMessage("Messages.PrefixRemovedConfirmedMessage", "&ePrefix removed for &6%player%&e."),
        servermsgInvalidUsage("Messages.servermsgInvalidUsage", "&cUsage of this command is /servermsg <player/permission> <message>.\nYou can use color codes and ^n for a new line.");

        private final String path;
        private final String message;

        Messages(String path, String message) {
            this.path = path;
            this.message = message;
        }
    }

    public static void onConfigSetup() {

        FileConfiguration config = Main.getInstance().getConfig();

        // Messages
        for (Messages m : Messages.values()) {
            if (!config.contains(m.path)) {
                config.set(m.path, m.message);
                Main.getInstance().getLogger().info(m.path + " not found in the config, creating it now.");
            }
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
            Donors.add("baron");
            Donors.add("count");
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
            Integer spawnRadius = 112;
            Integer spawnTimeLimit = 60;
            Integer spawnLimit = 10;
            config.set("SpawnLimiter.IRON_GOLEM.RadiusLimit", spawnRadius);
            config.set("SpawnLimiter.IRON_GOLEM.TimeLimit", spawnTimeLimit);
            config.set("SpawnLimiter.IRON_GOLEM.SpawnLimit", spawnLimit);
            Main.getInstance().getLogger().info("SpawnLimiter not found in the config, creating it now.");
        }

        // ----------------------------------------------------------------------------------------------------

        // Logging
        Logging.UpdateLogDates();

        Iterator it = Logging.LogNamesAndConfigPaths.entrySet().iterator();

        while (it.hasNext()) {

            Map.Entry element = (Map.Entry) it.next();

            if (!config.contains(element.getValue().toString())) {
                config.set(element.getValue() + ".Enabled", true);
                config.set(element.getValue() + ".NumberOfLogsToKeep", 365);
                Main.getInstance().getLogger().info(element.getValue() + " not found in the config, creating it now.");
            }

        }

        // ----------------------------------------------------------------------------------------------------

        // DatabaseInitiate
        if (!config.contains("Database")){
            config.set("Database.driver", "mariadb");
            config.set("Database.ip", "localhost");
            config.set("Database.port", "3306");
            config.set("Database.name", "utility");
            config.set("Database.username", "root");
            config.set("Database.password", "root");
        }

        // ----------------------------------------------------------------------------------------------------

        Main.getInstance().saveConfig();
    }

}
