package com.darko.main.destro.kickFromBungee;

import com.darko.main.AlttdUtility;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class KickFromBungeeCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (!AlttdUtility.getInstance().getConfig().getBoolean("FeatureToggles.KickFromBungeeCommand"))
            return true;

        if (sender.hasPermission("utility.kickfrombungee")) {
            if (!(args.length == 0)) {
                Player target = Bukkit.getPlayer(args[0]);
                if (target != null) {
                    String reason = StringUtils.join(args, " ", 1, args.length);
                    ByteArrayDataOutput out = ByteStreams.newDataOutput();
                    out.writeUTF("KickPlayer");
                    out.writeUTF(target.getName());
                    out.writeUTF(ChatColor.translateAlternateColorCodes('&', reason));
                    target.sendPluginMessage(AlttdUtility.getInstance(), "BungeeCord", out.toByteArray());
                    AlttdUtility.getInstance().getLogger().info(target.getName() + " kicked from bungee.");
                } else {

                }
            }
        }
        return true;
    }

}
