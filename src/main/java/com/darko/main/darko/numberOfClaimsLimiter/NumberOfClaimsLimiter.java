package com.darko.main.darko.numberOfClaimsLimiter;

import com.darko.main.AlttdUtility;
import com.darko.main.common.Methods;
import me.ryanhamshire.GriefPrevention.GriefPrevention;
import me.ryanhamshire.GriefPrevention.events.ClaimCreatedEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.permissions.PermissionAttachmentInfo;

public class NumberOfClaimsLimiter implements Listener {

    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
    public void onClaimCreatedEvent(ClaimCreatedEvent event) {

        if (!AlttdUtility.getInstance().getConfig().getBoolean("FeatureToggles.NumberOfClaimsLimiter")) return;

        if (!(event.getCreator() instanceof Player player)) return;

        if (player.hasPermission("utility.claim-limit.bypass")) return;

        int numberOfClaimsAfterEvent = GriefPrevention.instance.dataStore.getPlayerData(player.getUniqueId()).getClaims().size() + 1;
        int claimLimitFromPermission = -1;

        for (PermissionAttachmentInfo permissionAttachmentInfo : player.getEffectivePermissions()) {

            if (!permissionAttachmentInfo.getPermission().startsWith("utility.claim-limit.")) continue;

            try {
                int claimLimitFromPermissionTemp = Integer.parseInt(permissionAttachmentInfo.getPermission().substring(20));
                if (claimLimitFromPermissionTemp > claimLimitFromPermission)
                    claimLimitFromPermission = claimLimitFromPermissionTemp;
            } catch (NumberFormatException ignored) {
            }

        }

        if (claimLimitFromPermission == -1) {
            AlttdUtility.getInstance().getLogger().info(player.getName() + " doesn't have a utility.claim-limit. permission set.");
            return;
        }

        if (numberOfClaimsAfterEvent == claimLimitFromPermission) {
            new Methods().sendConfigMessage(player, "Messages.NumberOfClaimsLimiterAtLimit");
        } else if (numberOfClaimsAfterEvent > claimLimitFromPermission) {
            new Methods().sendConfigMessage(player, "Messages.NumberOfClaimsLimiterOverLimit");
            event.setCancelled(true);
        }

    }

}
