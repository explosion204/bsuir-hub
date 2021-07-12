package com.karnyshov.bsuirhub.controller.command;

import com.karnyshov.bsuirhub.controller.command.impl.GoToLoginPageCommand;
import com.karnyshov.bsuirhub.controller.command.impl.LoginCommand;
import jakarta.enterprise.inject.spi.CDI;

public enum CommandType {
    GO_TO_LOGIN_PAGE_COMMAND("login_page") {
        { command = CDI.current().select(GoToLoginPageCommand.class).get(); }
    },
    LOGIN_COMMAND("login") {
        { command = CDI.current().select(LoginCommand.class).get(); }
    };

    private String commandName;
    Command command;

    CommandType(String commandName) {
        this.commandName = commandName;
    }

    public static Command getCommand(String commandName) {
        return valueOf(commandName).command;
    }
}
