package com.darko.main.darko.logging.logs;

import com.darko.main.darko.logging.Logging;

public class NumberOfClaimsNotificationLog extends Log {

    public NumberOfClaimsNotificationLog() {

        super();
        super.setName("NumberOfClaimsNotificationLog");
        super.setEnabled(true);
        super.setDaysOfLogsToKeep(Logging.defaultDaysOfLogsToKeep);
        super.addArgument("Time", "");
        super.addArgument("User", "");
        super.addArgument("NumberOfClaims", "");

    }

}
