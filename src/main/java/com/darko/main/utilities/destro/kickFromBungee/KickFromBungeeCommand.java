package com.darko.main.utilities.destro.kickFromBungee;

import com.darko.main.Main;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.List;

public class KickFromBungeeCommand implements CommandExecutor, TabCompleter {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender.hasPermission("utility.kickfrombungee")) {
            if (!(args.length == 0)) {
                Player target  = Bukkit.getPlayer(args[0]);
                if(target != null) {
                    String reason = StringUtils.join(args, " ", 1, args.length);
                    ByteArrayDataOutput out = ByteStreams.newDataOutput();
                    out.writeUTF("KickPlayer");
                    out.writeUTF(target.getName());
                    out.writeUTF(ChatColor.translateAlternateColorCodes('&', reason));
                    target.sendPluginMessage(Main.getInstance(), "BungeeCord", out.toByteArray());
                } else {

                }
            }
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] strings) {
        return null;
    }
}
