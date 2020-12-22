package com.darko.main.temporary_holiday_presents;

import com.darko.main.Main;
import com.darko.main.database.Database;
import com.darko.main.other.Methods;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class PresentCommand implements CommandExecutor {

    static List<Player> recentlyRanTheCommand = new ArrayList<>();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if(recentlyRanTheCommand.contains((Player) sender)) return true;

        recentlyRanTheCommand.add((Player) sender);
        new BukkitRunnable() {
            @Override
            public void run() {
                recentlyRanTheCommand.remove((Player) sender);
            }
        }.runTaskLater(Main.getInstance(), 120);

        new BukkitRunnable() {
            @Override
            public void run() {
                try {

                    String UUID = ((Player) sender).getUniqueId().toString();

                    String query = "SELECT * FROM presents WHERE UUID = '" + UUID + "' AND Claimed = false;";

                    ResultSet rs = Database.connection.prepareStatement(query).executeQuery();

                    if (!rs.next()) {
                        sender.sendMessage(ChatColor.RED + "You've already claimed your present!");
                    }

                    ItemStack item = Methods.deserializeItemStack(rs.getString("Item"));

                    Inventory inventory = Bukkit.createInventory(null, 9, ChatColor.translateAlternateColorCodes('&', "&2H&4a&2p&4p&2y &4H&2o&4l&2i&4d&2a&4y&2s&4!"));

                    inventory.setItem(4, item);

                    new BukkitRunnable() {
                        @Override
                        public void run() {

                            ((Player) sender).openInventory(inventory);

                        }
                    }.runTask(Main.getInstance());

                    new BukkitRunnable() {
                        @Override
                        public void run() {

                            ((Player) sender).closeInventory();
                            ((Player) sender).getInventory().addItem(item);

                            new BukkitRunnable() {
                                @Override
                                public void run() {
                                    try {

                                        String query = "UPDATE presents SET Claimed = true WHERE UUID = '" + UUID + "';";
                                        Database.connection.prepareStatement(query).executeUpdate();

                                    } catch (Throwable throwable) {
                                        throwable.printStackTrace();
                                    }
                                }
                            }.runTaskAsynchronously(Main.getInstance());

                        }
                    }.runTaskLater(Main.getInstance(), 100);

                } catch (Throwable throwable) {
                    throwable.printStackTrace();
                }
            }
        }.runTaskAsynchronously(Main.getInstance());

        return true;

    }

}
