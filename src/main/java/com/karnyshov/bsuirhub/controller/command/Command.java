package com.karnyshov.bsuirhub.controller.command;

import jakarta.servlet.http.HttpServletRequest;

public interface Command {
    CommandResult execute(HttpServletRequest request);
}
