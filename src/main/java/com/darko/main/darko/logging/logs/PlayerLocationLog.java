package com.darko.main.darko.logging.logs;

import java.util.LinkedHashMap;

public class PlayerLocationLog extends Log {

    public PlayerLocationLog() {

        super(new LinkedHashMap<>() {
            {
                put("Time", "");
                put("Player", "");
                put("Location", "");
            }
        });

    }

}
