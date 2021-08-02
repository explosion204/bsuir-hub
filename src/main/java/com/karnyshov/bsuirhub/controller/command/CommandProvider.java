package com.karnyshov.bsuirhub.controller.command;

import com.karnyshov.bsuirhub.controller.command.impl.*;
import com.karnyshov.bsuirhub.controller.command.impl.admin.*;
import com.karnyshov.bsuirhub.controller.command.impl.ajax.*;
import jakarta.enterprise.inject.spi.CDI;

import java.util.*;

import static com.karnyshov.bsuirhub.controller.command.RequestMethod.*;

public enum CommandProvider {
    /* AJAX AREA */
    // FIXME: 7/25/2021 api is exposed to everyone
    SET_LOCALE(SetLocaleCommand.class, POST,"/ajax/set_locale"),
    GET_USERS(GetUsersCommand.class, GET, "/ajax/get_users"),
    GET_FACULTIES(GetFacultiesCommand.class, GET, "/ajax/get_faculties"),
    GET_DEPARTMENTS(GetDepartmentsCommand.class, GET, "/ajax/get_departments"),
    GET_GROUPS(GetGroupsCommand.class, GET, "/ajax/get_groups"),
    UPLOAD_PROFILE_IMAGE(UploadProfileImageCommand.class, POST, "/ajax/upload_profile_image"),

    /* COMMON AREA */
    GO_TO_INDEX_PAGE(GoToIndexPageCommand.class, GET, "/"),
    GO_TO_LOGIN_PAGE(GoToLoginPageCommand.class, GET, "/login", "/login/"),
    GO_TO_SETTINGS_PAGE(GoToSettingsPageCommand.class, GET, "/settings", "/settings/"),
    GO_TO_NOT_FOUND_PAGE(GoToNotFoundPageCommand.class, GET, "/error/404"),
    GO_TO_INTERNAL_SERVER_ERROR_PAGE(GoToInternalErrorPage.class, GET, "/error/500"),
    LOGIN(LoginCommand.class, POST, "/login"),
    LOGOUT(LogoutCommand.class, GET, "/logout", "/logout/"),
    CHANGE_PASSWORD(ChangePasswordCommand.class, POST, "/settings/change_password"),
    CHANGE_EMAIL(ChangeEmailCommand.class, POST, "/settings/change_email"),
    CONFIRM_EMAIL(ConfirmEmailCommand.class, GET, "/confirm_email"),

    /* ADMIN AREA */
    GO_TO_USERS_PAGE(GoToUsersPageCommand.class, GET, "/admin/users", "/admin/users/", "/admin", "/admin/"),
    GO_TO_NEW_USER_PAGE(GoToNewUserPageCommand.class, GET, "/admin/users/new", "/admin/users/new/"),
    GO_TO_EDIT_USER_PAGE(GoToEditUserPageCommand.class, GET, "/admin/users/edit"),
    CREATE_USER(CreateUserCommand.class, POST, "/admin/users/new"),
    UPDATE_USER(UpdateUserCommand.class, POST, "/admin/users/edit"),
    DELETE_USER(DeleteUserCommand.class, POST, "/admin/users/delete"),

    GO_TO_FACULTIES_PAGE(GoToFacultiesPageCommand.class, GET, "/admin/faculties", "/admin/faculties/"),
    GO_TO_NEW_FACULTY_PAGE(GoToNewFacultyPageCommand.class, GET, "/admin/faculties/new", "/admin/faculties/new/"),
    GO_TO_EDIT_FACULTY_PAGE(GoToEditFacultyPageCommand.class, GET, "/admin/faculties/edit"),
    CREATE_FACULTY(CreateFacultyCommand.class, POST, "/admin/faculties/new"),
    UPDATE_FACULTY(UpdateFacultyCommand.class, POST, "/admin/faculties/edit"),
    DELETE_FACULTY(DeleteFacultyCommand.class, POST, "/admin/faculties/delete"),

    GO_TO_DEPARTMENTS_PAGE(GoToDepartmentsPageCommand.class, GET, "/admin/departments", "/admin/departments/"),
    GO_TO_NEW_DEPARTMENT_PAGE(GoToNewDepartmentPageCommand.class, GET, "/admin/departments/new", "/admin/departments/new/"),
    GO_TO_EDIT_DEPARTMENT_PAGE(GoToEditDepartmentPageCommand.class, GET, "/admin/departments/edit"),
    CREATE_DEPARTMENT(CreateDepartmentCommand.class, POST, "/admin/departments/new"),
    UPDATE_DEPARTMENT(UpdateDepartmentCommand.class, POST, "/admin/departments/edit"),
    DELETE_DEPARTMENT(DeleteDepartmentCommand.class, POST, "/admin/departments/delete"),

    GO_TO_GROUPS_PAGE(GoToGroupsPageCommand.class, GET, "/admin/groups", "/admin/groups/"),
    GO_TO_NEW_GROUP_PAGE(GoToNewGroupPageCommand.class, GET, "/admin/groups/new", "/admin/groups/new/"),
    CREATE_GROUP(CreateGroupCommand.class, POST, "/admin/groups/new");

    private List<String> urlPatterns;
    private RequestMethod requestMethod;
    private Command command;

    CommandProvider(Class<? extends Command> commandClass, RequestMethod requestMethod, String ... urlPatterns) {
        command = CDI.current().select(commandClass).get();
        this.urlPatterns = Arrays.asList(urlPatterns);
        this.requestMethod = requestMethod;
    }

    public static Optional<Command> getCommand(String url, RequestMethod requestMethod) {
        for (CommandProvider commandType : CommandProvider.values()) {
            Optional<String> urlMatch = commandType.urlPatterns
                    .stream()
                    .filter(p -> p.equals(url) && commandType.requestMethod == requestMethod)
                    .findAny();

            if (urlMatch.isPresent()) {
                return Optional.of(commandType.command);
            }
        }

        return Optional.empty();
    }
}
