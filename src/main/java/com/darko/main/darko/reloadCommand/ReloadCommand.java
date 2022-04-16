package com.darko.main.darko.reloadCommand;

import com.darko.main.common.reload.Reload;
import com.darko.main.common.reload.ReloadType;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class ReloadCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        Reload.reload(ReloadType.RELOAD);

        sender.sendMessage(ChatColor.GREEN + "Plugin reloaded!");
        return true;

    }

}
