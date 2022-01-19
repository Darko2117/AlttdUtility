package com.darko.main.darko.logging.logs;

import com.darko.main.darko.logging.Logging;

public class ItemsDestroyedLog extends Log {

    public ItemsDestroyedLog() {

        super();
        super.setName("ItemsDestroyedLog");
        super.setEnabled(true);
        super.setDaysOfLogsToKeep(Logging.defaultDaysOfLogsToKeep);
        super.addArgument("Time", "");
        super.addArgument("Item", "");
        super.addArgument("Location", "");
        super.addArgument("Cause", "");

    }

}
