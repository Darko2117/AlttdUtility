package com.darko.main.utilities.other;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import com.darko.main.Main;
import com.darko.main.cosmetics.chair.Chair;
import com.darko.main.cosmetics.chair.onBlockBreak;
import com.darko.main.cosmetics.chair.onChunkLoad;
import com.darko.main.cosmetics.chair.onPlayerDismount;
import com.darko.main.cosmetics.chair.onPlayerQuit;
import com.darko.main.cosmetics.chair.onStairsRightClick;
import com.darko.main.cosmetics.hat.Hat;
import com.darko.main.utilities.CMI.onPayCommand.onPayCommand;
import com.darko.main.utilities.cooldown.Cooldown;
import com.darko.main.utilities.cooldown.cooldownTabComplete;
import com.darko.main.utilities.durability.AutoFix;
import com.darko.main.utilities.durability.onDurabilityUse;
import com.darko.main.utilities.flags.onAnvilClick;
import com.darko.main.utilities.flags.onBoneMeal;
import com.darko.main.utilities.flags.onEnchantmentTableClick;
import com.darko.main.utilities.flags.onEntityRename;
import com.darko.main.utilities.online.GroupsTabComplete;
import com.darko.main.utilities.online.OnlineCommand;
import com.darko.main.utilities.portal.onPortalUse;
import com.darko.main.utilities.servermsg.Servermsg;
import com.darko.main.utilities.tamedexpire.onEntityInteractWithLead;
import com.darko.main.utilities.teleport.onPlayerTeleport;

public class Register extends JavaPlugin {

    public static void RegisterEvents() {
        Bukkit.getPluginManager().registerEvents(new onStairsRightClick(), Main.getInstance());
        Bukkit.getPluginManager().registerEvents(new onPlayerDismount(), Main.getInstance());
        Bukkit.getPluginManager().registerEvents(new onPlayerQuit(), Main.getInstance());
        Bukkit.getPluginManager().registerEvents(new onBlockBreak(), Main.getInstance());
        Bukkit.getPluginManager().registerEvents(new onChunkLoad(), Main.getInstance());
        // Bukkit.getPluginManager().registerEvents(new onPlayerTeleportCommand(),
        // Main.getInstance());
        Bukkit.getPluginManager().registerEvents(new onAnvilClick(), Main.getInstance());
        Bukkit.getPluginManager().registerEvents(new onEnchantmentTableClick(), Main.getInstance());
        Bukkit.getPluginManager().registerEvents(new onPlayerTeleport(), Main.getInstance());
        Bukkit.getPluginManager().registerEvents(new onEntityRename(), Main.getInstance());
        Bukkit.getPluginManager().registerEvents(new onEntityInteractWithLead(), Main.getInstance());
        Bukkit.getPluginManager().registerEvents(new onBoneMeal(), Main.getInstance());
        Bukkit.getPluginManager().registerEvents(new onPayCommand(), Main.getInstance());
        Bukkit.getPluginManager().registerEvents(new onPortalUse(), Main.getInstance());
        Bukkit.getPluginManager().registerEvents(new onDurabilityUse(), Main.getInstance());
    }

    public static void RegisterCommands() {
        Main.getInstance().getCommand("autofix").setExecutor(new AutoFix());
        Main.getInstance().getCommand("online").setExecutor(new OnlineCommand());
        Main.getInstance().getCommand("online").setTabCompleter(new GroupsTabComplete());
        Main.getInstance().getCommand("hat").setExecutor(new Hat());
        Main.getInstance().getCommand("servermsg").setExecutor(new Servermsg());
        Main.getInstance().getCommand("chair").setExecutor(new Chair());
        if (GlobalVariables.LuckPermsFound) {
            Main.getInstance().getCommand("cooldown").setExecutor(new Cooldown());
            Main.getInstance().getCommand("cooldown").setTabCompleter(new cooldownTabComplete());
        }

        if (GlobalVariables.WorldGuardFound) {
            Flags.FlagsEnable();
        }
    }

}
