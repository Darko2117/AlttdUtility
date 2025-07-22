package com.darko.main.darko.logging.logs;

import java.util.LinkedHashMap;

public class MountingLog extends Log {

    public MountingLog() {

        super(new LinkedHashMap<>() {
            {
                put("Time", "");
                put("Player", "");
                put("Vehicle", "");
                put("Action", "");
                put("Location", "");
            }
        });

    }

}
