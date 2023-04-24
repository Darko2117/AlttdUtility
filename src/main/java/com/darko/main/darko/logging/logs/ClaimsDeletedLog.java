package com.darko.main.darko.logging.logs;

import java.util.LinkedHashMap;

public class ClaimsDeletedLog extends Log {

    public ClaimsDeletedLog() {

        super(new LinkedHashMap<>() {
            {
                put("Time", "");
                put("User", "");
                put("LowestY", "");
                put("Area", "");
            }
        });

    }

}
