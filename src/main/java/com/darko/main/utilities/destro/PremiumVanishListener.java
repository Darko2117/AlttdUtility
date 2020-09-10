package com.darko.main.utilities.destro;

import de.myzelyam.api.vanish.PlayerVanishStateChangeEvent;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Tameable;
import org.bukkit.entity.Wolf;
import org.bukkit.event.Listener;

public class PremiumVanishListener implements Listener {

    public void onPlayerVanishChange(PlayerVanishStateChangeEvent event) {
        if(event.isCancelled()) return;
        if(event.isVanishing()) {
            for(World world : Bukkit.getWorlds()) {
                world.getEntitiesByClasses(Wolf.class).stream().forEach(entity -> {
                    if (((Tameable) entity).isTamed()){
                        if (((Tameable) entity).getOwner().getUniqueId().equals(event.getUUID())){
                            ((Wolf)entity).setSitting(true);
                        }
                    }
                });
            }
        }
    }
}
