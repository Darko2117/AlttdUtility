package com.darko.main.darko.logging.logs;

import java.util.LinkedHashMap;

public class ClaimsDeletedLog extends Log {

    public ClaimsDeletedLog() {

        super(new LinkedHashMap<>() {
            {
                put("Time", "");
                put("Player", "");
                put("LowestY", "");
                put("Area", "");
            }
        });

    }

}
