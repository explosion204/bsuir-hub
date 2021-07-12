package com.karnyshov.bsuirhub.controller.command;

public class CommandResult {
    public enum RouteType {
        FORWARD,
        REDIRECT
    }

    private String routePath;
    private RouteType routeType;

    public CommandResult(String routePath, RouteType routeType) {
        this.routePath = routePath;
        this.routeType = routeType;
    }

    public String getRoutePath() {
        return routePath;
    }

    public RouteType getRouteType() {
        return routeType;
    }
}
