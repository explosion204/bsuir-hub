package com.karnyshov.bsuirhub.controller.command;

import com.karnyshov.bsuirhub.controller.command.impl.*;
import jakarta.enterprise.inject.spi.CDI;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.karnyshov.bsuirhub.controller.command.RequestMethod.*;

public enum CommandType {
    GO_TO_INDEX_PAGE_COMMAND("/", GET) {
        { command = CDI.current().select(GoToIndexPageCommand.class).get(); }
    },
    GO_TO_LOGIN_PAGE_COMMAND("/login", GET) {
        { command = CDI.current().select(GoToLoginPageCommand.class).get(); }
    },


    SET_LOCALE("/set_locale", POST) {
        { command = CDI.current().select(SetLocaleCommand.class).get(); }
    },
    LOGIN_COMMAND("/login", POST) {
        { command = CDI.current().select(LoginCommand.class).get(); }
    },
    LOGOUT_COMMAND("/logout", GET) {
        { command = CDI.current().select(LogoutCommand.class).get(); }
    };

    private Pattern urlPattern;
    private RequestMethod requestMethod;
    Command command;

    CommandType(String urlPattern, RequestMethod requestMethod) {
        this.urlPattern = Pattern.compile(urlPattern);
        this.requestMethod = requestMethod;
    }

    public static Command parseCommand(String url, RequestMethod requestMethod, List<String> parsedParams) {
        for (CommandType commandType : CommandType.values()) {
            if (Pattern.matches(commandType.urlPattern.pattern(), url) && commandType.requestMethod == requestMethod) {
                Matcher matcher = commandType.urlPattern.matcher(url);

                if (parsedParams != null && matcher.find()) {
                    for (int i = 1; i <= matcher.groupCount(); i++) {
                        parsedParams.add(matcher.group(i));
                    }
                }

                return commandType.command;
            }
        }

        return CommandType.GO_TO_INDEX_PAGE_COMMAND.command; // FIXME: 7/19/2021 404 error
    }
}
