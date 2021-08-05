package com.karnyshov.bsuirhub.controller.command;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import com.karnyshov.bsuirhub.controller.command.impl.*;
import com.karnyshov.bsuirhub.controller.command.impl.admin.department.*;
import com.karnyshov.bsuirhub.controller.command.impl.admin.faculty.*;
import com.karnyshov.bsuirhub.controller.command.impl.admin.group.*;
import com.karnyshov.bsuirhub.controller.command.impl.admin.subject.*;
import com.karnyshov.bsuirhub.controller.command.impl.admin.user.*;
import com.karnyshov.bsuirhub.controller.command.impl.ajax.*;
import jakarta.enterprise.inject.spi.CDI;

import java.util.Optional;

import static com.karnyshov.bsuirhub.controller.command.ApplicationPath.*;
import static com.karnyshov.bsuirhub.controller.command.RequestMethod.GET;
import static com.karnyshov.bsuirhub.controller.command.RequestMethod.POST;

public class CommandProvider {
    public static final CommandProvider instance = new CommandProvider();
    private Table<String, RequestMethod, Command> urlMap = HashBasedTable.create();


    private CommandProvider() {
        /* AJAX AREA */
        // FIXME: 7/25/2021 api is exposed to everyone
        urlMap.put(AJAX_SET_LOCALE_URL, POST, resolveCommand(SetLocaleCommand.class));
        urlMap.put(AJAX_GET_USERS_URL, GET, resolveCommand(GetUsersCommand.class));
        urlMap.put(AJAX_GET_FACULTIES_URL, GET, resolveCommand(GetFacultiesCommand.class));
        urlMap.put(AJAX_GET_DEPARTMENTS_URL, GET, resolveCommand(GetDepartmentsCommand.class));
        urlMap.put(AJAX_GET_GROUPS_URL, GET, resolveCommand(GetGroupsCommand.class));
        urlMap.put(AJAX_GET_SUBJECTS_URL, GET, resolveCommand(GetSubjectsCommand.class));
        urlMap.put(AJAX_GET_ASSIGNMENTS_URL, GET, resolveCommand(GetStudyAssignmentsCommand.class));
        urlMap.put(AJAX_UPLOAD_PROFILE_IMAGE_URL, POST, resolveCommand(UploadProfileImageCommand.class));
        urlMap.put(ADMIN_AJAX_CREATE_ASSIGNMENT_URL, POST, resolveCommand(CreateStudyAssignmentCommand.class));
        urlMap.put(ADMIN_AJAX_UPDATE_ASSIGNMENT_URL, POST, resolveCommand(UpdateStudyAssignmentCommand.class));
        urlMap.put(ADMIN_AJAX_DELETE_ASSIGNMENT_URL, POST, resolveCommand(DeleteStudyAssignmentCommand.class));

        /* COMMON AREA */
        urlMap.put(INDEX_URL, GET, resolveCommand(GoToIndexPageCommand.class));
        urlMap.put(LOGIN_URL, GET, resolveCommand(GoToLoginPageCommand.class));
        urlMap.put(LOGIN_URL, POST, resolveCommand(LoginCommand.class));
        urlMap.put(LOGOUT_URL, GET, resolveCommand(LogoutCommand.class));
        urlMap.put(FORGOT_PASSWORD_URL, GET, resolveCommand(GoToForgotPasswordPageCommand.class));
        urlMap.put(SEND_RESET_PASSWORD_LINK_URL, POST, resolveCommand(SendResetPasswordLinkCommand.class));
        urlMap.put(RESET_PASSWORD_URL, GET, resolveCommand(GoToResetPasswordPageCommand.class));
        urlMap.put(RESET_PASSWORD_URL, POST, resolveCommand(ResetPasswordCommand.class));
        urlMap.put(SETTINGS_URL, GET, resolveCommand(GoToSettingsPageCommand.class));
        urlMap.put(CHANGE_PASSWORD_URL, POST, resolveCommand(ChangePasswordCommand.class));
        urlMap.put(CHANGE_EMAIL_URL, POST, resolveCommand(ChangeEmailCommand.class));
        urlMap.put(CONFIRM_EMAIL_URL, GET, resolveCommand(ConfirmEmailCommand.class));
        urlMap.put(TEACHER_URL, GET, resolveCommand(GoToTeacherPageCommand.class));
        urlMap.put(NOT_FOUND_ERROR_URL, GET, resolveCommand(GoToNotFoundPageCommand.class));
        urlMap.put(INTERNAL_SERVER_ERROR_URL, GET, resolveCommand(GoToInternalErrorPage.class));

        /* ADMIN AREA */
        urlMap.put(ADMIN_USERS_URL, GET, resolveCommand(GoToUsersPageCommand.class));
        urlMap.put(ADMIN_NEW_USER_URL, GET, resolveCommand(GoToNewUserPageCommand.class));
        urlMap.put(ADMIN_NEW_USER_URL, POST, resolveCommand(CreateUserCommand.class));
        urlMap.put(ADMIN_EDIT_USER_URL, GET, resolveCommand(GoToEditUserPageCommand.class));
        urlMap.put(ADMIN_EDIT_USER_URL, POST, resolveCommand(UpdateUserCommand.class));
        urlMap.put(ADMIN_DELETE_USER_URL, POST, resolveCommand(DeleteUserCommand.class));

        urlMap.put(ADMIN_FACULTIES_URL, GET, resolveCommand(GoToFacultiesPageCommand.class));
        urlMap.put(ADMIN_NEW_FACULTY_URL, GET, resolveCommand(GoToNewFacultyPageCommand.class));
        urlMap.put(ADMIN_NEW_FACULTY_URL, POST, resolveCommand(CreateFacultyCommand.class));
        urlMap.put(ADMIN_EDIT_FACULTY_URL, GET, resolveCommand(GoToEditFacultyPageCommand.class));
        urlMap.put(ADMIN_EDIT_FACULTY_URL, POST, resolveCommand(UpdateFacultyCommand.class));
        urlMap.put(ADMIN_DELETE_FACULTY_URL, POST, resolveCommand(DeleteFacultyCommand.class));

        urlMap.put(ADMIN_DEPARTMENTS_URL, GET, resolveCommand(GoToDepartmentsPageCommand.class));
        urlMap.put(ADMIN_NEW_DEPARTMENT_URL, GET, resolveCommand(GoToNewDepartmentPageCommand.class));
        urlMap.put(ADMIN_NEW_DEPARTMENT_URL, POST, resolveCommand(CreateDepartmentCommand.class));
        urlMap.put(ADMIN_EDIT_DEPARTMENT_URL, GET, resolveCommand(GoToEditDepartmentPageCommand.class));
        urlMap.put(ADMIN_EDIT_DEPARTMENT_URL, POST, resolveCommand(UpdateDepartmentCommand.class));
        urlMap.put(ADMIN_DELETE_DEPARTMENT_URL, POST, resolveCommand(DeleteDepartmentCommand.class));

        urlMap.put(ADMIN_GROUPS_URL, GET, resolveCommand(GoToGroupsPageCommand.class));
        urlMap.put(ADMIN_NEW_GROUP_URL, GET, resolveCommand(GoToNewGroupPageCommand.class));
        urlMap.put(ADMIN_NEW_GROUP_URL, POST, resolveCommand(CreateGroupCommand.class));
        urlMap.put(ADMIN_EDIT_GROUP_URL, GET, resolveCommand(GoToEditGroupPageCommand.class));
        urlMap.put(ADMIN_EDIT_GROUP_URL, POST, resolveCommand(UpdateGroupCommand.class));
        urlMap.put(ADMIN_DELETE_GROUP_URL, POST, resolveCommand(DeleteGroupCommand.class));

        urlMap.put(ADMIN_SUBJECTS_URL, GET, resolveCommand(GoToSubjectsPageCommand.class));
        urlMap.put(ADMIN_NEW_SUBJECT_URL, GET, resolveCommand(GoToNewSubjectPageCommand.class));
        urlMap.put(ADMIN_NEW_SUBJECT_URL, POST, resolveCommand(CreateSubjectCommand.class));
        urlMap.put(ADMIN_EDIT_SUBJECT_URL, GET, resolveCommand(GoToEditSubjectPageCommand.class));
        urlMap.put(ADMIN_EDIT_SUBJECT_URL, POST, resolveCommand(UpdateSubjectCommand.class));
        urlMap.put(ADMIN_DELETE_SUBJECT_URL, POST, resolveCommand(DeleteSubjectCommand.class));
    }

    public static CommandProvider getInstance() {
        return instance;
    }

    public Optional<Command> getCommand(String url, RequestMethod requestMethod) {
        Command command = urlMap.get(url, requestMethod);
        return Optional.ofNullable(command);
    }

    private Command resolveCommand(Class<? extends Command> commandClass) {
        return CDI.current().select(commandClass).get();
    }
}
