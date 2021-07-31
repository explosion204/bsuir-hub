package com.karnyshov.bsuirhub.controller.command.impl.ajax;

import com.google.gson.Gson;
import com.karnyshov.bsuirhub.controller.command.Command;
import com.karnyshov.bsuirhub.controller.command.CommandResult;
import com.karnyshov.bsuirhub.controller.listener.AuthenticatedSessionCollector;
import com.karnyshov.bsuirhub.controller.validator.UserValidator;
import com.karnyshov.bsuirhub.exception.ServiceException;
import com.karnyshov.bsuirhub.model.entity.User;
import com.karnyshov.bsuirhub.model.entity.UserRole;
import com.karnyshov.bsuirhub.model.service.UserService;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.Part;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static com.karnyshov.bsuirhub.controller.command.AjaxRequestParameter.*;
import static com.karnyshov.bsuirhub.controller.command.ApplicationPath.PROFILE_PICTURES_ROOT;
import static com.karnyshov.bsuirhub.controller.command.CommandResult.RouteType.JSON;
import static com.karnyshov.bsuirhub.controller.command.SessionAttribute.USER;

@Named
public class UploadProfileImageCommand implements Command {
    private static final Logger logger = LogManager.getLogger();

    private static final int FILE_UNIQUE_SEQUENCE_LENGTH = 8;
    private static final String UNDERSCORE = "_";
    private static final String IMAGE_FILE_EXTENSION = ".jpg";

    @Inject
    private UserService userService;

    @Inject
    private UserValidator validator;

    @Override
    public CommandResult execute(HttpServletRequest request) {
        Map<String, Object> response = new HashMap<>();
        boolean status = true;

        try {
            User user = (User) request.getSession().getAttribute(USER);
            long issuerId = user.getEntityId();
            long targetId = Long.parseLong(request.getParameter(TARGET_ID));

            String uploadPath = request.getServletContext().getRealPath(PROFILE_PICTURES_ROOT);
            File uploadDirectory = new File(uploadPath);

            if (!uploadDirectory.exists()) {
                uploadDirectory.mkdir();
            }

            Optional<User> issuer = userService.findById(issuerId);
            Optional<User> target = userService.findById(targetId);

            // administrator can change everyone's profile image
            // other users can change only their own profile images
            if (issuer.isPresent() && target.isPresent() && (issuerId == targetId
                    || issuer.get().getRole() == UserRole.ADMIN)) {

                String fileName = RandomStringUtils.random(FILE_UNIQUE_SEQUENCE_LENGTH, true, true) + UNDERSCORE
                        + targetId + IMAGE_FILE_EXTENSION;
                String filePath = uploadPath + File.separator + fileName;

                for (Part part : request.getParts()) {
                    part.write(filePath);
                }

                // validate file
                boolean validationResult = validator.validateProfileImage(filePath);

                if (validationResult) {
                    // delete existing user profile image
                    File currentProfileImage = new File(uploadPath + File.separator
                            + target.get().getProfilePicturePath());

                    if (currentProfileImage.exists()) {
                        currentProfileImage.delete();
                    }

                    // update profile image path
                    User updatedTarget = User.builder()
                            .of(target.get())
                            .setProfilePicturePath(fileName)
                            .build();

                    userService.update(updatedTarget);

                    // success
                    // update target user session if exists
                    // this command can be executed by administrator, so we have to update session as
                    // if it belongs to another user
                    AuthenticatedSessionCollector.findSession(targetId).ifPresent(
                            httpSession -> httpSession.setAttribute(USER, updatedTarget)
                    );
                } else {
                    // delete file
                    new File(filePath).delete();
                }

                response.put(STATUS, validationResult);
            } else {
                logger.error("Invalid issuer (id = " + issuerId + ") or target (id = " + targetId + ")");
                status = false;
            }
        } catch (NumberFormatException | ServiceException | IOException | ServletException | NullPointerException e) {
            logger.error("An error occurred executing 'upload profile image' command", e);
            status = false;
        }

        response.put(STATUS, status);
        return new CommandResult(new Gson().toJson(response), JSON);
    }
}
