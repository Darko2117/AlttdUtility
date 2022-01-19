package com.darko.main.darko.logging.logs;

import com.darko.main.darko.logging.Logging;

public class ItemsDespawnedLog extends Log {

    public ItemsDespawnedLog() {

        super();
        super.setName("ItemsDespawnedLog");
        super.setEnabled(true);
        super.setDaysOfLogsToKeep(Logging.defaultDaysOfLogsToKeep);
        super.addArgument("Time", "");
        super.addArgument("Item", "");
        super.addArgument("Location", "");

    }

}
