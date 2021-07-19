package com.karnyshov.bsuirhub.controller.command;

import jakarta.servlet.http.HttpServletRequest;

import java.util.List;

public interface Command {
    CommandResult execute(HttpServletRequest request, List<String> commandParams);
}
