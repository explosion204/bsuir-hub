<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<div class="admin-sidebar d-flex flex-column flex-shrink-0 p-3 bg-light">
    <a href="/" class="d-flex align-items-center mb-3 mb-md-0 me-md-auto link-dark text-decoration-none">
        <svg class="bi me-2" width="40" height="32"><use xlink:href="#bootstrap"></use></svg>
        <span class="fs-4">BSUIR <span class="emphasized-logo-text">HUB</span></span>
    </a>
    <hr>
    <ul id="navbar" class="nav nav-pills flex-column mb-auto">
        <li class="nav-item">
            <a href="/admin/users" class="nav-link link-dark" aria-current="page">Users</a>
        </li>
        <li class="nav-item">
            <a href="/admin/faculties" class="nav-link link-dark">Faculties</a>
        </li>
        <li class="nav-item">
            <a href="/admin/departments" class="nav-link link-dark">Departments</a>
        </li>
        <li class="nav-item">
            <a href="/admin/groups" class="nav-link link-dark">Groups</a>
        </li>
    </ul>
    <hr>
    <div class="dropdown">
        <a href="#" class="d-flex align-items-center link-dark text-decoration-none dropdown-toggle text-wrap"
           id="dropdownUser" data-bs-toggle="dropdown" aria-expanded="false">
            <img src="/static/images/profile/${sessionScope.user.profilePicturePath}" width="32" height="32"
                 class="rounded-circle me-2">
            <strong><jsp:text>${sessionScope.user.lastName} ${sessionScope.user.firstName}</jsp:text></strong>
        </a>
        <ul class="dropdown-menu text-small shadow" aria-labelledby="dropdownUser">
            <li><a class="dropdown-item" href="/settings">Settings</a></li>
            <li><a class="dropdown-item" href="#">Profile</a></li>
            <li><hr class="dropdown-divider"></li>
            <li><a class="dropdown-item" href="/logout">Log out</a></li>
        </ul>
    </div>
</div>