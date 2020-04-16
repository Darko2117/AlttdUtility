package com.darko.main.utilities.config;

import java.util.*;

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
        DeathMessage("Messages.DeathMessage", "&cUse &6/dback&c to go back to your previous death location.");

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
        List<String> paths = new ArrayList<>();

        paths.add("Logging.ClaimCreatedLog.Enabled");
        paths.add("Logging.ClaimDeletedLog.Enabled");
        paths.add("Logging.ClaimExpiredLog.Enabled");
        paths.add("Logging.ClaimModifiedLog.Enabled");
        paths.add("Logging.EggLog.Enabled");
        paths.add("Logging.DroppedItemsLog.Enabled");
        paths.add("Logging.CratePrizeLog.Enabled");
        paths.add("Logging.SpawnLimitReachedLog.Enabled");
        paths.add("Logging.ItemPlacedInItemFrameLog.Enabled");
        paths.add("Logging.MCMMORepairUseLog.Enabled");

        for (String path : paths) {
            if (!config.contains(path)) {
                config.set(path, true);
                Main.getInstance().getLogger().info(path + " not found in the config, creating it now.");
            }
        }

        HashMap<String, Integer> amountOfFilesToKeep = new HashMap<>();

        amountOfFilesToKeep.put("Logging.ClaimCreatedLog.NumberOfLogsToKeep", 30);
        amountOfFilesToKeep.put("Logging.ClaimDeletedLog.NumberOfLogsToKeep", 30);
        amountOfFilesToKeep.put("Logging.ClaimExpiredLog.NumberOfLogsToKeep", 30);
        amountOfFilesToKeep.put("Logging.ClaimModifiedLog.NumberOfLogsToKeep", 30);
        amountOfFilesToKeep.put("Logging.EggLog.NumberOfLogsToKeep", 30);
        amountOfFilesToKeep.put("Logging.DroppedItemsLog.NumberOfLogsToKeep", 30);
        amountOfFilesToKeep.put("Logging.CratePrizeLog.NumberOfLogsToKeep", 30);
        amountOfFilesToKeep.put("Logging.SpawnLimitReachedLog.NumberOfLogsToKeep", 30);
        amountOfFilesToKeep.put("Logging.ItemPlacedInItemFrameLog.NumberOfLogsToKeep", 30);
        amountOfFilesToKeep.put("Logging.MCMMORepairUseLog.NumberOfLogsToKeep", 30);

        Iterator amountOfFilesToKeepIterator = amountOfFilesToKeep.entrySet().iterator();

        while (amountOfFilesToKeepIterator.hasNext()) {

            Map.Entry element = (Map.Entry) amountOfFilesToKeepIterator.next();

            if (!config.contains(element.getKey().toString())) {
                config.set(element.getKey().toString(), element.getValue());
                Main.getInstance().getLogger().info(element.getKey() + " not found in the config, creating it now.");
            }

        }

        // ----------------------------------------------------------------------------------------------------


        Main.getInstance().saveConfig();
    }

}
