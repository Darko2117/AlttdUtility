package com.darko.main.darko.logging.logs;

import com.darko.main.darko.logging.Logging;

public class IllegalItemsLog extends Log {

    public IllegalItemsLog() {

        super();
        super.setName("IllegalItemsLog");
        super.setEnabled(true);
        super.setDaysOfLogsToKeep(Logging.defaultDaysOfLogsToKeep);
        super.addArgument("Time", "");
        super.addArgument("Player", "");
        super.addArgument("Item", "");
        super.addArgument("ReplacedWithItem", "");
        super.addArgument("Location", "");
        super.addArgument("Event", "");

    }

}
