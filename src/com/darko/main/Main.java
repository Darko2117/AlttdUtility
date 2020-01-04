package com.darko.main;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import com.darko.main.cosmetics.chair.Chair;
import com.darko.main.cosmetics.chair.onBlockBreak;
import com.darko.main.cosmetics.chair.onChunkLoad;
import com.darko.main.cosmetics.chair.onPlayerDismount;
import com.darko.main.cosmetics.chair.onPlayerJoin;
import com.darko.main.cosmetics.chair.onPlayerQuit;
import com.darko.main.cosmetics.chair.onPlayerTeleportCommand;
import com.darko.main.cosmetics.chair.onStairsRightClick;
import com.darko.main.cosmetics.hat.Hat;
import com.darko.main.utilities.CMI.onPayCommand.onPayCommand;
import com.darko.main.utilities.cooldown.Cooldown;
import com.darko.main.utilities.cooldown.cooldownTabComplete;
import com.darko.main.utilities.flags.onAnvilClick;
import com.darko.main.utilities.flags.onBoneMeal;
import com.darko.main.utilities.flags.onEntityRename;
import com.darko.main.utilities.other.APIs;
import com.darko.main.utilities.other.ConsoleColors;
import com.darko.main.utilities.other.Flags;
import com.darko.main.utilities.other.GlobalVariables;
import com.darko.main.utilities.servermsg.Servermsg;
import com.darko.main.utilities.tamedexpire.onEntityInteractWithLead;
import com.darko.main.utilities.teleport.onPlayerTeleport;

public class Main extends JavaPlugin {

    @Override
    public void onEnable() {
        System.out.println("------------------------------");
        System.out.print("[AlttdUtility] ");
        System.out.println("Utility plugin started... Teri bring back " + ConsoleColors.GREEN_BOLD + "Sugar Cane"
                + ConsoleColors.RESET + " please thank you :)");

        Bukkit.getPluginManager().registerEvents(new onStairsRightClick(), this);
        Bukkit.getPluginManager().registerEvents(new onPlayerDismount(this), this);
        Bukkit.getPluginManager().registerEvents(new onPlayerQuit(), this);
        Bukkit.getPluginManager().registerEvents(new onBlockBreak(), this);
        Bukkit.getPluginManager().registerEvents(new onChunkLoad(this), this);
        Bukkit.getPluginManager().registerEvents(new onPlayerTeleportCommand(), this);
        Bukkit.getPluginManager().registerEvents(new onAnvilClick(), this);
        Bukkit.getPluginManager().registerEvents(new onPlayerTeleport(), this);
        Bukkit.getPluginManager().registerEvents(new onEntityRename(), this);
        Bukkit.getPluginManager().registerEvents(new onEntityInteractWithLead(), this);
        Bukkit.getPluginManager().registerEvents(new onBoneMeal(), this);
        Bukkit.getPluginManager().registerEvents(new onPlayerJoin(), this);
        Bukkit.getPluginManager().registerEvents(new onPayCommand(), this);

        APIs.APIConnect();

        getCommand("hat").setExecutor(new Hat());
        getCommand("servermsg").setExecutor(new Servermsg());
        getCommand("chair").setExecutor(new Chair());
        if (GlobalVariables.LuckPermsFound) {
            getCommand("cooldown").setExecutor(new Cooldown());
            getCommand("cooldown").setTabCompleter(new cooldownTabComplete());
        }

        if (GlobalVariables.WorldGuardFound) {
            Flags.FlagsEnable();
        }

    }
}
