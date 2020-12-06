package com.darko.main.utilities.customChatMessage;

import com.darko.main.Main;
import com.darko.main.other.Methods;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class CustomChatMessage implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (!Main.getInstance().getConfig().getBoolean("FeatureToggles.CustomChatMessagesCommand")) return true;

        if (args.length == 0) {
            new Methods().sendConfigMessage(sender, "Messages.CustomChatMessagesUsage");
            return true;
        }



        return true;

    }

}
