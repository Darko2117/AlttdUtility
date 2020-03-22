package com.darko.main.utilities.logging;

import com.darko.main.utilities.logging.claimlogging.ClaimCreatedLogFile;
import com.darko.main.utilities.logging.claimlogging.ClaimDeletedLogFile;
import com.darko.main.utilities.logging.claimlogging.ClaimExpiredLogFile;
import com.darko.main.utilities.logging.claimlogging.ClaimModifiedLogFile;
import com.darko.main.utilities.logging.egglogging.EggLogFile;
import com.darko.main.utilities.logging.prizelogging.CratePrizeLogFile;
import com.darko.main.utilities.logging.spawnlogging.SpawnLimitReachedLog;
import com.darko.main.utilities.logging.spawnlogging.SpawnLimitReachedLogFile;

public class Logging {

    public static void Initiate() {

        LogDirectory.CreateLogDirectory();
        ClaimCreatedLogFile.Initiate();
        ClaimDeletedLogFile.Initiate();
        ClaimExpiredLogFile.Initiate();
        ClaimModifiedLogFile.Initiate();
        EggLogFile.Initiate();
        CratePrizeLogFile.Initiate();
        SpawnLimitReachedLogFile.Initiate();

    }

}
