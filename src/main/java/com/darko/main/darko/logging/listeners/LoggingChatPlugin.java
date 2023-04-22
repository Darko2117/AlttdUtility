package com.darko.main.darko.logging.listeners;

import com.alttd.chat.events.NickEvent;
import com.darko.main.darko.logging.Logging;
import com.darko.main.darko.logging.logs.NicknamesLog;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

import java.util.Date;

public class LoggingChatPlugin implements Listener {

    @EventHandler(priority = EventPriority.MONITOR)
    public static void onNickEvent(NickEvent event) {

        if (!Logging.getCachedLogFromName("NicknamesLog").isEnabled())
            return;

        String time = new Date(System.currentTimeMillis()).toString();

        String user = event.getTargetName();

        String nickname = event.getNickName();

        String whoResponded = event.getSenderName();

        String action = event.getNickEventType().toString();

        NicknamesLog log = new NicknamesLog();
        log.addArgumentValue(time);
        log.addArgumentValue(user);
        log.addArgumentValue(nickname);
        log.addArgumentValue(whoResponded);
        log.addArgumentValue(action);

        Logging.addToLogWriteQueue(log);

    }

}
