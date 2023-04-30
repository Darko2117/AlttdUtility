package com.darko.main.darko.logging.logs;

import com.darko.main.common.Methods;
import lombok.Getter;
import lombok.Setter;
import java.io.File;
import java.util.LinkedHashMap;
import java.util.Map;

@Getter
@Setter
public abstract class Log {

    private String name = this.getClass().getSimpleName();
    private boolean enabled = true;
    private int daysOfLogsToKeep = 60;
    private LinkedHashMap<String, String> arguments;

    public Log(LinkedHashMap<String, String> arguments) {
        this.arguments = arguments;
    }

    public void addArgumentValue(String argumentValue) {
        if (argumentValue.isEmpty())
            argumentValue = "-";
        for (Map.Entry<String, String> entry : arguments.entrySet()) {
            if (!entry.getValue().isEmpty())
                continue;
            entry.setValue(argumentValue);
            break;
        }
    }

    public String getPath() {
        return File.separator + "logs" + File.separator + Methods.getDateStringYYYYMMDD() + "-" + name + ".txt";
    }

    public String getLogArgumentsString() {

        StringBuilder string = new StringBuilder("|");
        for (Map.Entry<String, String> entry : arguments.entrySet()) {
            string.append(entry.getKey()).append(":").append(entry.getValue()).append("|");
        }

        return string.toString();

    }

}
