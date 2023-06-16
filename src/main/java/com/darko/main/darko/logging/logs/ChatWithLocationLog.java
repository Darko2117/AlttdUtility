package com.darko.main.darko.logging.logs;

import java.util.LinkedHashMap;

public class ChatWithLocationLog extends Log {

    public ChatWithLocationLog() {

        super(new LinkedHashMap<>() {
            {
                put("Time", "");
                put("Player", "");
                put("Message", "");
                put("OriginalMessage", "");
                put("Location", "");
            }
        });

    }

}
