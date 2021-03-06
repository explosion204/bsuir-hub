package com.karnyshov.bsuirhub.controller.command;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import com.karnyshov.bsuirhub.controller.command.impl.*;
import com.karnyshov.bsuirhub.controller.command.impl.admin.ajax.ClearStudentGradesCommand;
import com.karnyshov.bsuirhub.controller.command.impl.admin.ajax.CreateAssignmentCommand;
import com.karnyshov.bsuirhub.controller.command.impl.admin.ajax.DeleteAssignmentCommand;
import com.karnyshov.bsuirhub.controller.command.impl.admin.ajax.UpdateAssignmentCommand;
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


/**
 * {@code CommandProvider} class contains all mappings between URLs and {@link Command} instances.
 * It retrieves command dependencies from Jakarta CDI container and adds them to hash table
 * with composite keys (URL, {@link RequestMethod}).
 * @author Dmitry Karnyshov
 */
public class CommandProvider {
    private static final CommandProvider instance = new CommandProvider();
    private Table<String, RequestMethod, Command> urlMap = HashBasedTable.create();

    private CommandProvider() {
        /* AJAX AREA */
        urlMap.put(AJAX_GET_USERS_URL, GET, resolveCommand(GetUsersCommand.class));
        urlMap.put(AJAX_GET_FACULTIES_URL, GET, resolveCommand(GetFacultiesCommand.class));
        urlMap.put(AJAX_GET_DEPARTMENTS_URL, GET, resolveCommand(GetDepartmentsCommand.class));
        urlMap.put(AJAX_GET_GROUPS_URL, GET, resolveCommand(GetGroupsCommand.class));
        urlMap.put(AJAX_GET_SUBJECTS_URL, GET, resolveCommand(GetSubjectsCommand.class));
        urlMap.put(AJAX_GET_ASSIGNMENTS_URL, GET, resolveCommand(GetAssignmentsCommand.class));
        urlMap.put(AJAX_GET_GRADES_URL, GET, resolveCommand(GetGradesCommand.class));
        urlMap.put(AJAX_GET_COMMENTS_URL, GET, resolveCommand(GetCommentsCommand.class));
        urlMap.put(AJAX_GET_AVERAGE_GRADE_URL, GET, resolveCommand(GetAverageGradeCommand.class));
        urlMap.put(AJAX_UPLOAD_PROFILE_IMAGE_URL, POST, resolveCommand(UploadProfileImageCommand.class));
        urlMap.put(AJAX_CREATE_GRADE_URL, POST, resolveCommand(CreateGradeCommand.class));
        urlMap.put(AJAX_UPDATE_GRADE_URL, POST, resolveCommand(UpdateGradeCommand.class));
        urlMap.put(AJAX_DELETE_GRADE_URL, POST, resolveCommand(DeleteGradeCommand.class));
        urlMap.put(AJAX_CREATE_COMMENT_URL, POST, resolveCommand(CreateCommentCommand.class));
        urlMap.put(AJAX_DELETE_COMMENT_URL, POST, resolveCommand(DeleteCommentCommand.class));
        urlMap.put(ADMIN_AJAX_CREATE_ASSIGNMENT_URL, POST, resolveCommand(CreateAssignmentCommand.class));
        urlMap.put(ADMIN_AJAX_UPDATE_ASSIGNMENT_URL, POST, resolveCommand(UpdateAssignmentCommand.class));
        urlMap.put(ADMIN_AJAX_DELETE_ASSIGNMENT_URL, POST, resolveCommand(DeleteAssignmentCommand.class));
        urlMap.put(ADMIN_AJAX_CLEAR_STUDENT_GRADES_URL, POST, resolveCommand(ClearStudentGradesCommand.class));

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
        urlMap.put(CONTACT_US_URL, GET, resolveCommand(GoToContactUsPageCommand.class));
        urlMap.put(SEND_MESSAGE_URL, POST, resolveCommand(SendMessageCommand.class));
        urlMap.put(CHANGE_PASSWORD_URL, POST, resolveCommand(ChangePasswordCommand.class));
        urlMap.put(CHANGE_EMAIL_URL, POST, resolveCommand(ChangeEmailCommand.class));
        urlMap.put(CONFIRM_EMAIL_URL, GET, resolveCommand(ConfirmEmailCommand.class));
        urlMap.put(TEACHER_DASHBOARD_URL, GET, resolveCommand(GoToTeacherDashboardPageCommand.class));
        urlMap.put(STUDENT_DASHBOARD_URL, GET, resolveCommand(GoToStudentDashboardPageCommand.class));
        urlMap.put(GRADES_OVERVIEW_URL, GET, resolveCommand(GoToGradesOverviewPageCommand.class));

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

    /**
     * Get instance of {@code CommandProvider} class.
     *
     * @return {@code CommandProvider} instance.
     */
    public static CommandProvider getInstance() {
        return instance;
    }

    /**
     * Get command by URL and {@link RequestMethod}.
     *
     * @param url stringified url from controller.
     * @param requestMethod request method.
     * @return the command
     */
    public Optional<Command> getCommand(String url, RequestMethod requestMethod) {
        Command command = urlMap.get(url, requestMethod);
        return Optional.ofNullable(command);
    }

    private Command resolveCommand(Class<? extends Command> commandClass) {
        return CDI.current().select(commandClass).get();
    }
}
