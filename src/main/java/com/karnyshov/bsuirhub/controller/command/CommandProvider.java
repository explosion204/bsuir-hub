package com.karnyshov.bsuirhub.controller.command;

import com.karnyshov.bsuirhub.controller.command.impl.*;
import jakarta.enterprise.inject.spi.CDI;

import java.util.*;
import java.util.regex.Pattern;

import static com.karnyshov.bsuirhub.controller.command.RequestMethod.*;

public enum CommandProvider {
    /* AJAX AREA */
    // FIXME: 7/25/2021 api is exposed to everyone
    SET_LOCALE(SetLocaleCommand.class, POST,"/api/set_locale"),
    GET_USERS(GetUsersCommand.class, GET, "/api/get_users"),
    UPLOAD_PROFILE_IMAGE(UploadProfileImageCommand.class, POST, "/api/upload_profile_image"),

    /* COMMON AREA */
    GO_TO_INDEX_PAGE(GoToIndexPageCommand.class, GET, "/"),
    GO_TO_LOGIN_PAGE(GoToLoginPageCommand.class, GET, "/login", "/login/"),
    GO_TO_SETTINGS_PAGE(GoToSettingsPageCommand.class, GET, "/settings", "/settings/"),
    GO_TO_NOT_FOUND_PAGE(GoToNotFoundPageCommand.class, GET, "/error/404"),
    GO_TO_INTERNAL_SERVER_ERROR_PAGE(GoToInternalErrorPage.class, GET, "/error/500"),
    LOGIN(LoginCommand.class, POST, "/login"),
    LOGOUT(LogoutCommand.class, GET, "/logout", "/logout/"),
    CHANGE_PASSWORD(ChangePasswordCommand.class, POST, "/settings/change_password"),

    /* ADMIN AREA */
    GO_TO_USERS_INDEX_PAGE(GoToUsersPageCommand.class, GET, "/admin/users", "/admin/users/", "/admin", "/admin/"),
    GO_TO_NEW_USER_PAGE(GoToNewUserPageCommand.class, GET, "/admin/users/new", "/admin/users/new/"),
    GO_TO_EDIT_USER_PAGE(GoToEditUserPageCommand.class, GET, "/admin/users/edit"),
    CREATE_USER(CreateUserCommand.class, POST, "/admin/users/new"),
    UPDATE_USER(UpdateUserCommand.class, POST, "/admin/users/edit"),
    DELETE_USER(DeleteUserCommand.class, POST, "/admin/users/delete");

    private List<Pattern> urlPatterns = new ArrayList<>();
    private RequestMethod requestMethod;
    private Command command;

    CommandProvider(Class<? extends Command> commandClass, RequestMethod requestMethod, String ... urlPatterns) {
        command = CDI.current().select(commandClass).get();
        Arrays.stream(urlPatterns).forEach(
                pattern -> this.urlPatterns.add(Pattern.compile(pattern))
        );
        this.requestMethod = requestMethod;
    }

    public static Optional<Command> getCommand(String url, RequestMethod requestMethod) {
        for (CommandProvider commandType : CommandProvider.values()) {
            Optional<Pattern> pattern = commandType.urlPatterns
                    .stream()
                    .filter(p -> Pattern.matches(p.pattern(), url) && commandType.requestMethod == requestMethod)
                    .findFirst();

            if (pattern.isPresent()) {
                return Optional.of(commandType.command);
            }
        }

        return Optional.empty();
    }
}
