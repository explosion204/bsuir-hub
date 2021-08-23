<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="bht" uri="bsuirhub-tags" %>

<c:set var="role" value="${sessionScope.user.role.ordinal()}" />

<header>
    <nav class="navbar navbar-expand-md navbar-light bg-light">
        <div class="container-fluid">
            <a href="/" class="navbar-brand">BSUIR <span class="emphasized-logo-text">HUB</span></a>
            <button class="navbar-toggler" routeType="button" data-bs-toggle="collapse" data-bs-target="#navbarContent"
                    aria-controls="navbarContent" aria-expanded="false" aria-label="Toggle navigation">
                <span class="navbar-toggler-icon"></span>
            </button>
            <div class="collapse navbar-collapse" id="navbarContent">
                <ul class="nav navbar-nav w-100 align-items-md-center">
                    <li class="nav-item me-1">
                        <a href="/" class="nav-link" aria-current="page"><bht:localeTag key="header.home" /></a>
                    </li>
                    <c:if test="${role eq 2 or role eq 3}">
                        <li class="nav-item me-1">
                            <a href="/teacher" class="nav-link" aria-current="page"><bht:localeTag key="header.dashboard" /></a>
                        </li>
                    </c:if>
                    <c:if test="${role eq 1}">
                        <li class="nav-item me-1">
                            <a href="/student" class="nav-link" aria-current="page"><bht:localeTag key="header.dashboard" /></a>
                        </li>
                    </c:if>
                    <c:if test="${role eq 3}">
                        <li class="nav-item me-1">
                            <a href="/admin/users" class="nav-link" aria-current="page"><bht:localeTag key="header.admin_panel" /></a>
                        </li>
                    </c:if>
                    <li class="nav-item me-auto">
                        <a href="/contact_us" class="nav-link" aria-current="page"><bht:localeTag key="header.contact_us" /></a>
                    </li>

                    <c:choose>
                        <c:when test="${sessionScope.user.role.ordinal() ne 0}">
                            <li class="nav-item emphasized-nav-item dropdown me-3">
                                <a href="#" class="nav-link dropdown-toggle dropdown-plus d-flex align-items-center"
                                   id="accountDropdownMenu" role="button" data-bs-toggle="dropdown" aria-expanded="false">
                                    <p class="mt-auto mb-auto ms-1 me-3"><b>
                                        ${sessionScope.user.lastName} ${sessionScope.user.firstName} ${sessionScope.user.patronymic}
                                    </b></p>
                                    <p class="profile-image-holder ms-auto mt-auto mb-auto">
                                        <img id="profileImage" class="profile-image" src="/static/images/profile/${sessionScope.user.profileImageName}">
                                    </p>
                                    <i class="fas fa-chevron-down ms-2 me-2"></i>
                                </a>

                                <ul class="dropdown-menu" aria-labelledby="accountDropdownMenu">
                                    <li><a href="/settings" class="dropdown-item"><bht:localeTag key="header.settings" /></a></li>
                                    <li><hr class="dropdown-divider"></li>
                                    <li><a href="/logout" class="dropdown-item"><bht:localeTag key="header.logout" /></a></li>
                                </ul>
                            </li>
                        </c:when>
                        <c:otherwise>
                            <li class="nav-item">
                                <a href="/login" class="btn btn-secondary"><bht:localeTag key="header.login" /></a>
                            </li>
                        </c:otherwise>
                    </c:choose>

                </ul>
            </div>
        </div>
    </nav>
</header>
