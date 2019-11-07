package com.darko.utilities.plugin;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class onJoin implements Listener {
	@EventHandler
    public void onPlayerJoin(PlayerJoinEvent event){
    Player player = event.getPlayer();
    if(!GlobalVariables.chairEnabled.containsKey(player.getUniqueId().toString())){
    GlobalVariables.chairEnabled.put(player.getUniqueId().toString(), false);
    }}}
