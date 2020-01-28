package com.darko.main.utilities.other;

import org.bukkit.configuration.file.FileConfiguration;

import com.darko.main.Main;

public class ConfigSetup {

    public static void onConfigSetup() {

        FileConfiguration config = Main.getInstance().getConfig();
        if (!config.contains("Messages.NoPermission")) {
            config.set("Messages.NoPermission", "&cYou do not have permission to do this.");
        }

        if (!config.contains("Messages.ChairEnabled")) {
            config.set("Messages.ChairEnabled", "&aChair mode enabled, right click on any stairs to sit on them.");
        }

        if (!config.contains("Messages.ChairDisabled")) {
            config.set("Messages.ChairDisabled", "&cChair mode disabled.");
        }

        if (!config.contains("Messages.InvalidChairBlock")) {
            config.set("Messages.InvalidChairBlock", "&cInvalid block found above/below the stairs.");
        }

        if (!config.contains("Messages.ChairNoClaimPerm")) {
            config.set("Messages.ChairNoClaimPerm", "&cYou don't have %player%'s permission to use that.");
        }

        if (!config.contains("Messages.ChairNoRegionPerm")) {
            config.set("Messages.ChairNoRegionPerm", "&cYou can't sit in this region.");
        }

        if (!config.contains("Messages.HatNoItem")) {
            config.set("Messages.HatNoItem", "&eHold the item that you wish to put on your head.");
        }

        if (!config.contains("Messages.HatEquipped")) {
            config.set("Messages.HatEquipped", "&aItem equipped.");
        }

        if (!config.contains("Messages.HatSwapped")) {
            config.set("Messages.HatSwapped", "&aItems swapped.");
        }

        if (!config.contains("Messages.HatCurseOfBinding")) {
            config.set("Messages.HatCurseOfBinding", "&cCan't take off an item that has the &d&oCurse of Binding&r&c.");
        }

        if (!config.contains("Messages.OfflinePlayerPayment")) {
            config.set("Messages.OfflinePlayerPayment", "&cYou can't send money to offline players.");
        }

        if (!config.contains("Messages.NoCooldowns")) {
            config.set("Messages.NoCooldowns", "&eYou have no cooldowns right now.");
        }

        if (!config.contains("Messages.OnlineGroup")) {
            config.set("Messages.OnlineGroup", "&6Online players for the group &b%group%&6:");
        }

        if (!config.contains("Messages.NetherEntry")) {
            config.set("Messages.NetherEntry", "&cWhen exiting the nether you will be teleported to spawn.");
        }

        if (!config.contains("Messages.NetherExit")) {
            config.set("Messages.NetherExit", "&cYou have been teleported to spawn.");
        }

        if (!config.contains("Messages.AutoFixEnabled")) {
            config.set("Messages.AutoFixEnabled", "&aAuto repair enabled.");
        }

        if (!config.contains("Messages.AutoFixDisabled")) {
            config.set("Messages.AutoFixDisabled", "&cAuto repair disabled.");
        }
        Main.getInstance().saveConfig();
    }

}
