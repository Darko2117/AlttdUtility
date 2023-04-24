package com.darko.main.darko.logging.logs;

import java.util.LinkedHashMap;

public class ClaimsModifiedLog extends Log {

    public ClaimsModifiedLog() {

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
