package com.karnyshov.bsuirhub.controller.command;

import com.karnyshov.bsuirhub.controller.command.impl.*;
import jakarta.enterprise.inject.spi.CDI;

import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.karnyshov.bsuirhub.controller.command.RequestMethod.*;

public enum CommandType {
    /* AJAX AREA */
    SET_LOCALE("/set_locale", POST) {
        { command = CDI.current().select(SetLocaleCommand.class).get(); }
    },

    /* COMMON AREA */
    GO_TO_INDEX_PAGE("/", GET) {
        { command = CDI.current().select(GoToIndexPageCommand.class).get(); }
    },
    GO_TO_LOGIN_PAGE("/login", GET) {
        { command = CDI.current().select(GoToLoginPageCommand.class).get(); }
    },
    GO_TO_NOT_FOUND_PAGE("/error/404", GET) {
        { command = CDI.current().select(GoToNotFoundPageCommand.class).get(); }
    },
    GO_TO_INTERNAL_SERVER_ERROR_PAGE("/error/500", GET) {
        { command = CDI.current().select(GoToInternalErrorPage.class).get(); }
    },
    LOGIN("/login", POST) {
        { command = CDI.current().select(LoginCommand.class).get(); }
    },
    LOGOUT("/logout", GET) {
        { command = CDI.current().select(LogoutCommand.class).get(); }
    },

    /* ADMIN AREA */
    GO_TO_USERS_INDEX_ADMIN_PAGE("/admin/users", GET) {
        // TODO: 7/19/2021
    },
    GO_TO_NEW_USER_ADMIN_PAGE("/admin/users/new", GET) {
        // TODO: 7/19/2021
    },
    GO_TO_EDIT_USER_ADMIN_PAGE_COMMAND("/admin/users/(\\d{1,19})", GET) {
        // TODO: 7/19/2021
    },
    SAVE_USER("/admin/users/save", POST) {
        // TODO: 7/19/2021
    },
    DELETE_USER("/admin/users/delete/(\\d{1,19})", GET) {
        // TODO: 7/19/2021
    };

    private Pattern urlPattern;
    private RequestMethod requestMethod;
    Command command;

    CommandType(String urlPattern, RequestMethod requestMethod) {
        this.urlPattern = Pattern.compile(urlPattern);
        this.requestMethod = requestMethod;
    }

    public static Optional<Command> parseCommand(String url, RequestMethod requestMethod, List<String> parsedParams) {
        for (CommandType commandType : CommandType.values()) {
            if (Pattern.matches(commandType.urlPattern.pattern(), url) && commandType.requestMethod == requestMethod) {
                Matcher matcher = commandType.urlPattern.matcher(url);

                if (matcher.find()) {
                    for (int i = 1; i <= matcher.groupCount(); i++) {
                        parsedParams.add(matcher.group(i));
                    }
                }

                return Optional.of(commandType.command);
            }
        }

        return Optional.empty();
    }
}
