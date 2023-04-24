package com.darko.main.darko.logging.logs;

import java.util.LinkedHashMap;

public class ChatWithLocationLog extends Log {

    public ChatWithLocationLog() {

        super(new LinkedHashMap<>() {
            {
                put("Time", "");
                put("User", "");
                put("Message", "");
                put("OriginalMessage", "");
                put("Location", "");
            }
        });

    }

}
