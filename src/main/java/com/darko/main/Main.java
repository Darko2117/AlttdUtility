package com.darko.main;

import com.darko.main.register.Register;
import com.darko.main.utilities.rebootWhitelist.RebootWhitelist;
import com.darko.main.utilities.reload.ReloadCommand;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

public class Main extends JavaPlugin {

    public static Main instance;

    public static Main getInstance() {
        return instance;
    }

    @Override
    public void onEnable() {

        instance = this;
        Main.getInstance().getLogger().info("--------------------------------------------------");

        ReloadCommand.reload();

        Register.registerEvents();

        Register.registerCommands();

        RebootWhitelist.disableAfterBoot();

        Main.getInstance().getLogger().info("Utility plugin started...");
        Main.getInstance().getLogger().info("--------------------------------------------------");

        new BukkitRunnable() {
            @Override
            public void run() {
                com.darko.main.temporary_holiday_presents.Database.createPresentsTable();
            }
        }.runTaskLaterAsynchronously(Main.getInstance(), 20);

    }

}
