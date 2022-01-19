package com.darko.main.darko.logging.logs;

import com.darko.main.darko.logging.Logging;

public class MCMMORepairUseLog extends Log {

    public MCMMORepairUseLog() {

        super();
        super.setName("MCMMORepairUseLog");
        super.setEnabled(true);
        super.setDaysOfLogsToKeep(Logging.defaultDaysOfLogsToKeep);
        super.addArgument("Time", "");
        super.addArgument("User", "");
        super.addArgument("Item", "");

    }

}
