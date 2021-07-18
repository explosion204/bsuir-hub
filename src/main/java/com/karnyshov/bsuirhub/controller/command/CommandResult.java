package com.karnyshov.bsuirhub.controller.command;

public class CommandResult {
    public enum RouteType {
        FORWARD,
        REDIRECT,
        JSON
    }

    private String detail;
    private RouteType routeType;

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
