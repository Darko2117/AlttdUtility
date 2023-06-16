package com.darko.main.darko.logging.logs;

import java.util.LinkedHashMap;

public class MCMMORepairUseLog extends Log {

    public MCMMORepairUseLog() {

        super(new LinkedHashMap<>() {
            {
                put("Time", "");
                put("Player", "");
                put("Item", "");
            }
        });

    }

}
