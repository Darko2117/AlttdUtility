package com.darko.main.darko.joinLimiter;

import com.darko.main.AlttdUtility;
import com.darko.main.common.Methods;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

public class JoinLimiter implements Listener {

    static List<JoinLimiterObject> joinLimiterObjectList = new ArrayList<>();

    @EventHandler(ignoreCancelled = false, priority = EventPriority.HIGHEST)
    public void onPlayerJoin(PlayerLoginEvent event) {

        if (event.getPlayer().hasPermission("utility.joinlimiterbypass"))
            return;

        if (!event.getResult().equals(PlayerLoginEvent.Result.ALLOWED))
            return;

        JoinLimiterObject joinLimiterObject = getJoinLimiterObjectFromUUID(event.getPlayer().getUniqueId());

        if (joinLimiterObject != null) {
            joinLimiterObject.update();
            if (joinLimiterObject.getNumberOfRecentJoins() == 0) {
                joinLimiterObjectList.remove(joinLimiterObject);
                joinLimiterObject = null;
            }
        }

        if (joinLimiterObject == null) {
            joinLimiterObjectList.add(new JoinLimiterObject(event.getPlayer().getUniqueId(), System.currentTimeMillis()));
            return;
        }

        String timeString = new Methods().getTimeStringFromIntSeconds(Integer.parseInt(String.valueOf(joinLimiterObject.getRemainingWaitTime() / 1000)));

        if (joinLimiterObject.getNumberOfRecentJoins() >= AlttdUtility.getInstance().getConfig().getInt("JoinLimiter.JoinLimit")) {
            event.setResult(PlayerLoginEvent.Result.KICK_OTHER);
            event.setKickMessage(ChatColor.translateAlternateColorCodes('&', AlttdUtility.getInstance().getConfig().getString("Messages.JoinLimiterCantJoin").replace("%time%", timeString)));
            return;
        }

        if (joinLimiterObject.getNumberOfRecentJoins() == AlttdUtility.getInstance().getConfig().getInt("JoinLimiter.JoinLimit") - 1) {

            new BukkitRunnable() {
                @Override
                public void run() {
                    event.getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&', AlttdUtility.getInstance().getConfig().getString("Messages.JoinLimiterJoinWarning").replace("%time%", timeString)));
                }
            }.runTaskLater(AlttdUtility.getInstance(), 40);

        }

        joinLimiterObject.joinTimes.add(System.currentTimeMillis());

    }

    JoinLimiterObject getJoinLimiterObjectFromUUID(UUID uuid) {

        for (JoinLimiterObject joinLimiterObject : joinLimiterObjectList) {
            if (joinLimiterObject.getUuid().equals(uuid)) {
                return joinLimiterObject;
            }
        }

        return null;

    }

    private class JoinLimiterObject {

        UUID uuid;
        List<Long> joinTimes = new ArrayList<>();

        JoinLimiterObject(UUID uuid, Long joinTime) {

            this.uuid = uuid;
            joinTimes.add(joinTime);

        }

        void update() {

            List<Long> toRemove = new ArrayList<>();
            for (Long time : joinTimes) {
                if ((time + (AlttdUtility.getInstance().getConfig().getLong("JoinLimiter.TimeLimit") * 1000)) < System.currentTimeMillis()) {
                    toRemove.add(time);
                }
            }
            joinTimes.removeAll(toRemove);

        }

        public UUID getUuid() {
            return uuid;
        }

        public void setUuid(UUID uuid) {
            this.uuid = uuid;
        }

        public List<Long> getJoinTimes() {
            return joinTimes;
        }

        public void setJoinTimes(List<Long> joinTimes) {
            this.joinTimes = joinTimes;
        }

        public Integer getNumberOfRecentJoins() {
            return joinTimes.size();
        }

        public Long getRemainingWaitTime() {

            Long firstTime = joinTimes.get(0);
            for (Long time : joinTimes) {
                if (time < firstTime)
                    firstTime = time;
            }
            return (firstTime + (AlttdUtility.getInstance().getConfig().getLong("JoinLimiter.TimeLimit") * 1000)) - System.currentTimeMillis();

        }

    }

}
