package com.darko.main.darko.logging.logs;

import java.util.LinkedHashMap;

public class UIClicksLog extends Log {

    public UIClicksLog() {

        super(new LinkedHashMap<>() {
            {
                put("Time", "");
                put("User", "");
                put("InventoryName", "");
                put("ClickedItem", "");
                put("Location", "");
            }
        });

    }

}
