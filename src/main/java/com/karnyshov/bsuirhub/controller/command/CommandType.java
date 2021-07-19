package com.karnyshov.bsuirhub.controller.command;

import com.karnyshov.bsuirhub.controller.command.impl.*;
import jakarta.enterprise.inject.spi.CDI;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.karnyshov.bsuirhub.controller.command.RequestMethod.*;

public enum CommandType {
    /* AJAX AREA */
    SET_LOCALE(SetLocaleCommand.class, POST,"/set_locale"),

    /* COMMON AREA */
    GO_TO_INDEX_PAGE(GoToIndexPageCommand.class, GET, "/"),
    GO_TO_LOGIN_PAGE(GoToLoginPageCommand.class, GET, "/login"),
    GO_TO_NOT_FOUND_PAGE(GoToNotFoundPageCommand.class, GET, "/error/404"),
    GO_TO_INTERNAL_SERVER_ERROR_PAGE(GoToInternalErrorPage.class, GET, "/error/500"),
    LOGIN(LoginCommand.class, POST, "/login"),
    LOGOUT(LogoutCommand.class, GET, "/logout"),

    /* ADMIN AREA */
    GO_TO_USERS_INDEX_ADMIN_PAGE(GoToAdminUsersPageCommand.class, GET, "/admin/users", "/admin");
    // GO_TO_NEW_USER_ADMIN_PAGE(null, GET, "/admin/users/new"),  // TODO: 7/20/2021
    // GO_TO_EDIT_USER_ADMIN_PAGE_COMMAND(null, GET, "/admin/users/(\\d{1,19})"), // TODO: 7/20/2021
    // SAVE_USER(null, POST,"/admin/users/save"), // TODO: 7/20/2021
    // DELETE_USER(null, GET, "/admin/users/delete/(\\d{1,19})"); // TODO: 7/20/2021

    private Set<Pattern> urlPatterns = new LinkedHashSet<>();
    private RequestMethod requestMethod;
    private Command command;

    CommandType(Class<? extends Command> commandClass, RequestMethod requestMethod, String ... urlPatterns) {
        command = CDI.current().select(commandClass).get();
        Arrays.stream(urlPatterns).forEach(
                pattern -> this.urlPatterns.add(Pattern.compile(pattern))
        );
        this.requestMethod = requestMethod;
    }

    public static Optional<Command> parseCommand(String url, RequestMethod requestMethod, List<String> parsedParams) {
        for (CommandType commandType : CommandType.values()) {
            Optional<Pattern> pattern = commandType.urlPatterns
                    .stream()
                    .filter(p -> Pattern.matches(p.pattern(), url) && commandType.requestMethod == requestMethod)
                    .findFirst();

            if (pattern.isPresent()) {
                Matcher matcher = pattern.get().matcher(url);

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
