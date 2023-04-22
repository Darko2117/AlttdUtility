package com.darko.main.darko.logging.logs;

import com.darko.main.darko.logging.Logging;

public class ClaimsModifiedLog extends Log {

    public ClaimsModifiedLog() {

        super();
        super.setName("ClaimsModifiedLog");
        super.setEnabled(true);
        super.setDaysOfLogsToKeep(Logging.defaultDaysOfLogsToKeep);
        super.addArgument("Time", "");
        super.addArgument("User", "");
        super.addArgument("LowestY", "");
        super.addArgument("Area", "");

    }

}
