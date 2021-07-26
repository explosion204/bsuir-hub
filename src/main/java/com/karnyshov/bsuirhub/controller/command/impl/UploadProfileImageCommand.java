package com.karnyshov.bsuirhub.controller.command.impl;

import com.google.gson.Gson;
import com.karnyshov.bsuirhub.controller.command.Command;
import com.karnyshov.bsuirhub.controller.command.CommandResult;
import com.karnyshov.bsuirhub.controller.listener.AuthenticatedSessionCollector;
import com.karnyshov.bsuirhub.exception.ServiceException;
import com.karnyshov.bsuirhub.model.entity.User;
import com.karnyshov.bsuirhub.model.entity.UserRole;
import com.karnyshov.bsuirhub.model.service.UserService;
import com.karnyshov.bsuirhub.model.validator.UserDataValidator;
import jakarta.inject.Inject;
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

import static com.karnyshov.bsuirhub.controller.command.AjaxAttributes.*;
import static com.karnyshov.bsuirhub.controller.command.ApplicationPath.PROFILE_PICTURES_ROOT;
import static com.karnyshov.bsuirhub.controller.command.CommandResult.RouteType.JSON;
import static com.karnyshov.bsuirhub.controller.command.SessionAttribute.USER;

public class UploadProfileImageCommand implements Command {
    private static final Logger logger = LogManager.getLogger();

    private static final int FILE_UNIQUE_SEQUENCE_LENGTH = 8;
    private static final String UNDERSCORE = "_";
    private static final String IMAGE_FILE_EXTENSION = ".jpg";

    @Inject
    private UserService userService;

    @Override
    public CommandResult execute(HttpServletRequest request) {
        Map<String, Object> response = new HashMap<>();

        try {
            long issuerId = Long.parseLong(request.getParameter(ISSUER_ID));
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
                    || issuer.get().getUserRole() == UserRole.ADMIN)) {

                String fileName = RandomStringUtils.random(FILE_UNIQUE_SEQUENCE_LENGTH, true, true) + UNDERSCORE
                        + targetId + IMAGE_FILE_EXTENSION;
                String filePath = uploadPath + File.separator + fileName;

                for (Part part : request.getParts()) {
                    part.write(filePath);
                }

                // validate file
                boolean validationResult = UserDataValidator.validateProfileImage(filePath);

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
                response.put(STATUS, false);
            }
        } catch (NumberFormatException | ServiceException | IOException | ServletException e) {
            logger.error("An error occurred executing 'upload profile image' command", e);
            response.put(STATUS, false);
        }

        return new CommandResult(new Gson().toJson(response), JSON);
    }
}
