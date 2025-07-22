package com.darko.main.darko.logging.logs;

import java.util.LinkedHashMap;

public class LeadUsageLog extends Log {

    public LeadUsageLog() {

        super(new LinkedHashMap<>() {
            {
                put("Time", "");
                put("Player", "");
                put("Entity", "");
                put("Action", "");
                put("Location", "");
            }
        });

    }

}
