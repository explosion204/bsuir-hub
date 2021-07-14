package com.karnyshov.bsuirhub.controller.command;

import com.karnyshov.bsuirhub.controller.command.impl.GoToIndexPageCommand;
import com.karnyshov.bsuirhub.controller.command.impl.GoToLoginPageCommand;
import com.karnyshov.bsuirhub.controller.command.impl.LoginCommand;
import com.karnyshov.bsuirhub.exception.CommandException;
import jakarta.enterprise.inject.spi.CDI;

import static com.karnyshov.bsuirhub.controller.command.RequestMethod.*;

public enum CommandType {
    GO_TO_INDEX_PAGE_COMMAND("", GET) {
        { command = CDI.current().select(GoToIndexPageCommand.class).get(); }
    },
    GO_TO_LOGIN_PAGE_COMMAND("login", GET) {
        { command = CDI.current().select(GoToLoginPageCommand.class).get(); }
    },
    LOGIN_COMMAND("login", POST) {
        { command = CDI.current().select(LoginCommand.class).get(); }
    };

    private String commandName;
    private RequestMethod requestMethod;
    Command command;

    CommandType(String commandName, RequestMethod requestMethod) {
        this.commandName = commandName;
        this.requestMethod = requestMethod;
    }

    public static Command getCommand(String commandName, RequestMethod requestMethod) throws CommandException {
        for (CommandType commandType : CommandType.values()) {
            if (commandType.commandName.equals(commandName) && commandType.requestMethod == requestMethod) {
                return commandType.command;
            }
        }

        throw new CommandException("Command '" + commandName + "/" + requestMethod.name() + "' not found");
    }
}
