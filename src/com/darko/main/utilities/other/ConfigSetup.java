package com.darko.main.utilities.other;

import org.bukkit.configuration.file.FileConfiguration;

import com.darko.main.Main;

public class ConfigSetup {

    private enum Message {
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
        OnlineGroup("Messages.OnlineGroup", "&6Online players for the group &b%group%&6:"),
        NetherEntry("Messages.NetherEntry", "&cWhen exiting the nether you will be teleported to spawn."),
        NetherExit("Messages.NetherExit", "&cYou have been teleported to spawn."),
        AutoFixEnabled("Messages.AutoFixEnabled", "&aAuto repair enabled."),
        AutoFixDisabled("Messages.AutoFixDisabled", "&cAuto repair disabled."),
        PlayerNotFound("Messages.PlayerNotFound", "&cPlayer not found."),
        GroupNotFound("Messages.GroupNotFound", "&cNo online players in that group.");

        private final String path;
        private final String message;

        Message(String path, String message) {
            this.path = path;
            this.message = message;
        }
    }

    public static void onConfigSetup() {
        FileConfiguration config = Main.getInstance().getConfig();
        for (Message m : Message.values()) {
            if (!config.contains(m.path)) {
                config.set(m.path, m.message);
                System.out.println(m.path + " not found in the config, creating it now.");
            }
        }
        Main.getInstance().saveConfig();
    }

}
