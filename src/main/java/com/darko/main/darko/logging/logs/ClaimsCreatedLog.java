package com.darko.main.darko.logging.logs;

import com.darko.main.darko.logging.Logging;

public class ClaimsCreatedLog extends Log {

    public ClaimsCreatedLog() {

        super();
        super.setName("ClaimsCreatedLog");
        super.setEnabled(true);
        super.setDaysOfLogsToKeep(Logging.defaultDaysOfLogsToKeep);
        super.addArgument("Time", "");
        super.addArgument("User", "");
        super.addArgument("LowestY", "");
        super.addArgument("Area", "");

    }

}