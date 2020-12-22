package com.darko.main.temporary_holiday_presents;

import com.darko.main.Main;
import com.darko.main.database.Database;
import com.darko.main.other.Methods;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class SetPresentCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        new BukkitRunnable() {
            @Override
            public void run() {

                String UUID = args[0];
                String serializedItem = Methods.serializeItemStack(((Player) sender).getInventory().getItem(0));

                String query = "INSERT INTO presents(UUID, Item, Claimed) VALUES('" + UUID + "', '" + serializedItem + "', false);";

                try {

                    Database.connection.prepareStatement(query).executeUpdate();
                    sender.sendMessage("Done");

                } catch (Throwable throwable) {
                    throwable.printStackTrace();
                }

            }
        }.runTaskAsynchronously(Main.getInstance());

        return true;

    }

}
