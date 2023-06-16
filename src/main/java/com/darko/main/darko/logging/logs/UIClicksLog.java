package com.darko.main.darko.logging.logs;

import java.util.LinkedHashMap;

public class UIClicksLog extends Log {

    public UIClicksLog() {

        super(new LinkedHashMap<>() {
            {
                put("Time", "");
                put("Player", "");
                put("InventoryName", "");
                put("Item", "");
                put("Location", "");
            }
        });

    }

}
