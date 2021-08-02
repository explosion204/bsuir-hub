<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<fmt:setLocale value="${sessionScope.locale}" />
<fmt:setBundle basename="locale" />

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
                        <a href="#" class="nav-link active" aria-current="page"><fmt:message key="header.home" /></a>
                    </li>
                    <li class="nav-item me-1">
                        <a href="#" class="nav-link" aria-current="page"><fmt:message key="header.rating" /></a>
                    </li>
                    <li class="nav-item dropdown me-1">
                        <a href="#" class="nav-link dropdown-toggle" id="universityDropdownMenu" role="button"
                           data-bs-toggle="dropdown" aria-expanded="false">
                            <fmt:message key="header.university" />
                        </a>
                        <ul class="dropdown-menu" aria-labelledby="universityDropdownMenu">
                            <li><a href="#" class="dropdown-item"><fmt:message key="header.teachers" /></a></li>
                            <li><a href="#" class="dropdown-item"><fmt:message key="header.subjects" /></a></li>
                            <li><a href="#" class="dropdown-item"><fmt:message key="header.faculties" /></a></li>
                            <li><a href="#" class="dropdown-item"><fmt:message key="header.departments" /></a></li>
                        </ul>
                    </li>
                    <li class="nav-item me-auto">
                        <a href="#" class="nav-link" aria-current="page"><fmt:message key="header.about" /></a>
                    </li>

                    <c:choose>
                        <c:when test="${sessionScope.user.role.ordinal() ne 0}">
                            <li class="nav-item emphasized-nav-item dropdown me-3">
                                <a href="#" class="nav-link dropdown-toggle dropdown-plus d-flex align-items-center"
                                   id="accountDropdownMenu" role="button" data-bs-toggle="dropdown" aria-expanded="false">
                                    <p class="mt-auto mb-auto ms-1 me-3"><b>
                                        <jsp:text>${sessionScope.user.lastName} ${sessionScope.user.firstName}</jsp:text>
                                    </b></p>
                                    <p class="profile-picture-holder ms-auto mt-auto mb-auto">
                                        <img id="profileImage" src="/static/images/profile/${sessionScope.user.profilePicturePath}">
                                    </p>
                                    <i class="fas fa-chevron-down ms-2 me-2"></i>
                                </a>

                                <ul class="dropdown-menu" aria-labelledby="accountDropdownMenu">
                                    <li><a href="#" class="dropdown-item"><fmt:message key="header.profile" /></a></li>
                                    <li><a href="/settings" class="dropdown-item"><fmt:message key="header.settings" /></a></li>
                                    <li><hr class="dropdown-divider"></li>
                                    <li><a href="/logout" class="dropdown-item"><fmt:message key="header.logout" /></a></li>
                                </ul>
                            </li>
                        </c:when>
                        <c:otherwise>
                            <li class="nav-item">
                                <a href="/login" class="btn btn-secondary"><fmt:message key="header.login" /></a>
                            </li>
                        </c:otherwise>
                    </c:choose>

                </ul>
            </div>
        </div>
    </nav>
</header>
