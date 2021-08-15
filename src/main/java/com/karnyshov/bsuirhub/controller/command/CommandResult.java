package com.karnyshov.bsuirhub.controller.command;

/**
 * {@code CommandResult} class is a result of {@link Command} execution.
 * @author Dmitry Karnyshov
 */
public class CommandResult {
    /**
     * {@code RouteType} enum represents the way how controller handles the result.
     */
    public enum RouteType {
        /**
         * Forward to JSP pages.
         */
        FORWARD,
        /**
         * Redirect to URLs.
         */
        REDIRECT,
        /**
         * JSON response for ajax requests.
         */
        JSON
    }

    private String detail;
    private RouteType routeType;

    /**
     * Instantiates a new {@code CommandResult}.
     *
     * @param detail it can be URL or local path to JSP page.
     * @param routeType {@code RouteType} enum constant.
     */
    public CommandResult(String detail, RouteType routeType) {
        this.detail = detail;
        this.routeType = routeType;
    }

    public String getDetail() {
        return detail;
    }
    public RouteType getRouteType() {
        return routeType;
    }
}
