package com.darko.main.darko.logging.logs;

import com.darko.main.darko.logging.Logging;

public class NicknamesLog extends Log{

    public NicknamesLog(){

        super();
        super.setName("NicknamesLog");
        super.setEnabled(true);
        super.setDaysOfLogsToKeep(Logging.defaultDaysOfLogsToKeep);
        super.addArgument("Time", "");
        super.addArgument("User", "");
        super.addArgument("Nickname", "");
        super.addArgument("WhoResponded", "");
        super.addArgument("Action", "");

    }

}
