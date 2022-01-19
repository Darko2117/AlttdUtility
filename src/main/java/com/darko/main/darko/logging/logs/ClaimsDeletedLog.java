package com.darko.main.darko.logging.logs;

import com.darko.main.darko.logging.Logging;

public class ClaimsDeletedLog extends Log {

    public ClaimsDeletedLog() {

        super();
        super.setName("ClaimsDeletedLog");
        super.setEnabled(true);
        super.setDaysOfLogsToKeep(Logging.defaultDaysOfLogsToKeep);
        super.addArgument("Time", "");
        super.addArgument("User", "");
        super.addArgument("LowestY", "");
        super.addArgument("Area", "");

    }

}