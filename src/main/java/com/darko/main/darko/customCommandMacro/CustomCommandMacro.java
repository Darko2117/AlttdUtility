package com.darko.main.darko.customCommandMacro;

import lombok.Data;

@Data
public class CustomCommandMacro {

    private String UUID;
    private String macroName;
    private String command;

    public CustomCommandMacro(String UUID, String macroName, String command) {
        this.UUID = UUID;
        this.macroName = macroName;
        this.command = command;
    }

}
