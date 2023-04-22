package com.darko.main.darko.balanceStats.databaseLogs;

import com.Zrips.CMI.events.CMIUserBalanceChangeEvent;
import com.darko.main.AlttdUtility;
import com.darko.main.common.database.Database;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.scheduler.BukkitRunnable;

public class DatabaseMoneyLog implements Listener {

    @EventHandler
    public void onCMIUserBalanceChange(CMIUserBalanceChangeEvent cmiUserBalanceChangeEvent) {

        if (Database.connection == null)
            return;

        String userUUID = cmiUserBalanceChangeEvent.getUser().getPlayer().getUniqueId().toString();
        String userUsername = cmiUserBalanceChangeEvent.getUser().getPlayer().getName();

        String sourceUUID = null;
        String sourceUsername = null;
        if (cmiUserBalanceChangeEvent.getSource() != null) {
            sourceUUID = cmiUserBalanceChangeEvent.getSource().getPlayer().getUniqueId().toString();
            sourceUsername = cmiUserBalanceChangeEvent.getSource().getPlayer().getName();
        }

        double fromBalance = cmiUserBalanceChangeEvent.getFrom();
        double toBalance = cmiUserBalanceChangeEvent.getTo();

        String actionType = cmiUserBalanceChangeEvent.getActionType();

        String statement = "INSERT INTO money_log(user_UUID, user_username, source_UUID, source_username, from_balance, to_balance, action_type) VALUES(" + "'" + userUUID + "', " + "'" + userUsername + "', " + "'" + sourceUUID + "', " + "'" + sourceUsername + "', " + fromBalance + ", " + toBalance + ", " + "'" + actionType + "');";

        new BukkitRunnable() {
            @Override
            public void run() {
                try {
                    Database.connection.prepareStatement(statement).executeUpdate();
                } catch (Throwable throwable) {
                    throwable.printStackTrace();
                }
            }
        }.runTaskAsynchronously(AlttdUtility.getInstance());

    }

}
