package com.darko.main.darko.logging.logs;

import java.util.LinkedHashMap;

public class ClaimsCreatedLog extends Log {

    public ClaimsCreatedLog() {

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
