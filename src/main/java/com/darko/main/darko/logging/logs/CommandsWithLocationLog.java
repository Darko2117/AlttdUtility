package com.darko.main.darko.logging.logs;

import java.util.LinkedHashMap;

public class CommandsWithLocationLog extends Log {

    public CommandsWithLocationLog() {

        super(new LinkedHashMap<>() {
            {
                put("Time", "");
                put("User", "");
                put("Command", "");
                put("Location", "");
            }
        });

    }

}
