package com.darko.main.fairy;

import com.darko.main.AlttdUtility;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class GlowBerryEffect implements Listener {

    @EventHandler(ignoreCancelled = true, priority = EventPriority.LOWEST)
    public void whenEating(PlayerItemConsumeEvent event) {
        Player player = event.getPlayer();
        ItemStack item = event.getItem();

        if (item.getType() != Material.GLOW_BERRIES)
            return;

        final int duration = AlttdUtility.getInstance().getConfig().getInt("GlowBerryEffectDuration");

        player.addPotionEffect(new PotionEffect(
                PotionEffectType.GLOWING,
                duration,
                1,
                false,
                false
        ));
    }
}
