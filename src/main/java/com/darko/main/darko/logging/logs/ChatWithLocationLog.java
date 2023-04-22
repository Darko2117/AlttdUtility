package com.darko.main.darko.logging.logs;

import com.darko.main.darko.logging.Logging;

public class ChatWithLocationLog extends Log {

    public ChatWithLocationLog() {

        super();
        super.setName("ChatWithLocationLog");
        super.setEnabled(true);
        super.setDaysOfLogsToKeep(Logging.defaultDaysOfLogsToKeep);
        super.addArgument("Time", "");
        super.addArgument("User", "");
        super.addArgument("Message", "");
        super.addArgument("OriginalMessage", "");
        super.addArgument("Location", "");

    }

}
