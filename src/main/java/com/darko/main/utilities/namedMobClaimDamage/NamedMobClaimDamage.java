package com.darko.main.utilities.namedMobClaimDamage;

import com.darko.main.AlttdUtility;
import me.ryanhamshire.GriefPrevention.Claim;
import me.ryanhamshire.GriefPrevention.GriefPrevention;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PotionSplashEvent;

import java.util.List;

public class NamedMobClaimDamage implements Listener {

    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {

        if (!AlttdUtility.getInstance().getConfig().getBoolean(("FeatureToggles.NamedMobClaimDamageProtection"))) return;

        Entity entity = event.getEntity();

        if (entity.getCustomName() == null) return;
        if (!checkIfNameIsProtected(entity.getCustomName())) return;

        //Player

        if (event.getDamager() instanceof Player) {

            Player damagerPlayer = (Player) event.getDamager();

            if (claimCheck(damagerPlayer, entity.getLocation()) != null) {

                damagerPlayer.sendMessage(ChatColor.translateAlternateColorCodes('&', AlttdUtility.getInstance().getConfig()
                        .getString("Messages.GriefPreventionThatBelongsToMessage").replace("%player%", claimCheck(damagerPlayer, entity.getLocation()))));

                event.setCancelled(true);

            }

        }

        //Arrow

        if (event.getDamager() instanceof Arrow) {

            Arrow damagerArrow = (Arrow) event.getDamager();

            if (!(damagerArrow.getShooter() instanceof Player)) return;

            Player damagerPlayer = (Player) damagerArrow.getShooter();

            if (claimCheck(damagerPlayer, entity.getLocation()) != null) {

                damagerPlayer.sendMessage(ChatColor.translateAlternateColorCodes('&', AlttdUtility.getInstance().getConfig()
                        .getString("Messages.GriefPreventionThatBelongsToMessage").replace("%player%", claimCheck(damagerPlayer, entity.getLocation()))));

                event.setCancelled(true);

            }

        }

        //CraftAreaEffectCloud

        if (event.getDamager() instanceof AreaEffectCloud) {

            AreaEffectCloud damagerAreaEffectCloud = (AreaEffectCloud) event.getDamager();

            if (!(damagerAreaEffectCloud.getSource() instanceof Player)) return;

            Player damagerPlayer = (Player) damagerAreaEffectCloud.getSource();

            if (claimCheck(damagerPlayer, entity.getLocation()) != null) {

                damagerPlayer.sendMessage(ChatColor.translateAlternateColorCodes('&', AlttdUtility.getInstance().getConfig()
                        .getString("Messages.GriefPreventionThatBelongsToMessage").replace("%player%", claimCheck(damagerPlayer, entity.getLocation()))));

                event.setCancelled(true);

            }

        }

        //Fireworks

        if (event.getDamager() instanceof Firework) {

            Firework damagerFirework = (Firework) event.getDamager();

            if (!(damagerFirework.getShooter() instanceof Player)) return;

            Player damagerPlayer = (Player) damagerFirework.getShooter();

            if (claimCheck(damagerPlayer, entity.getLocation()) != null) {

                damagerPlayer.sendMessage(ChatColor.translateAlternateColorCodes('&', AlttdUtility.getInstance().getConfig()
                        .getString("Messages.GriefPreventionThatBelongsToMessage").replace("%player%", claimCheck(damagerPlayer, entity.getLocation()))));

                event.setCancelled(true);

            }

        }

    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
    public void onPotionSplash(PotionSplashEvent event) {

        //PotionSplash

        if (!AlttdUtility.getInstance().getConfig().getBoolean(("FeatureToggles.NamedMobClaimDamageProtection"))) return;

        ThrownPotion potion = event.getEntity();

        if (!(potion.getShooter() instanceof Player)) return;

        Player potionThrower = (Player) potion.getShooter();

        for (Entity entity : event.getAffectedEntities()) {

            if (entity.getCustomName() == null) continue;
            if (!checkIfNameIsProtected(entity.getCustomName())) continue;

            if (claimCheck(potionThrower, entity.getLocation()) != null) {

                potionThrower.sendMessage(ChatColor.translateAlternateColorCodes('&', AlttdUtility.getInstance().getConfig()
                        .getString("Messages.GriefPreventionThatBelongsToMessage").replace("%player%", claimCheck(potionThrower, entity.getLocation()))));

                event.setCancelled(true);

            }

        }

    }

    String claimCheck(Player player, Location location) {

        Claim claim = GriefPrevention.instance.dataStore.getClaimAt(location, true, null);
        if (claim == null) return null;

        if (claim.allowBuild(player, Material.STONE) != null) return claim.getOwnerName();

        return null;

    }

    Boolean checkIfNameIsProtected(String name) {

        List<String> protectedNames = AlttdUtility.getInstance().getConfig().getStringList("NamedMobClaimDamage.Names");

        for (String s : protectedNames) {

            if (s.toLowerCase().equals(name.toLowerCase())) return true;

        }

        return false;

    }

}
