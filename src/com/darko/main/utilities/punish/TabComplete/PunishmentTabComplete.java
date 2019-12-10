package com.darko.main.utilities.punish.TabComplete;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

public class PunishmentTabComplete implements TabCompleter{

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
    	if(args.length == 1){
    	List<String> completions = new ArrayList<>();
    	for(Player player : Bukkit.getOnlinePlayers()){
    		completions.add(player.getName());
    	}        
    	return completions;
    }
    return null;	
    }
}
