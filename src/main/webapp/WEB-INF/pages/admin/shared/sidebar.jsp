<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="bht" uri="bsuirhub-tags" %>

<div class="admin-sidebar d-flex flex-column flex-shrink-0 p-3 bg-light">
    <a href="/" class="d-flex align-items-center mb-3 mb-md-0 me-md-auto link-dark text-decoration-none">
        <svg class="bi me-2" width="40" height="32"><use xlink:href="#bootstrap"></use></svg>
        <span class="fs-4">BSUIR <span class="emphasized-logo-text">HUB</span></span>
    </a>
    <hr>
    <ul id="navbar" class="nav nav-pills flex-column mb-auto h-100">
        <li class="nav-item">
            <a href="/admin/users" class="nav-link link-dark" aria-current="page"><bht:localeTag key="admin.users" /></a>
        </li>
        <li class="nav-item">
            <a href="/admin/faculties" class="nav-link link-dark"><bht:localeTag key="admin.faculties" /></a>
        </li>
        <li class="nav-item">
            <a href="/admin/departments" class="nav-link link-dark"><bht:localeTag key="admin.departments" /></a>
        </li>
        <li class="nav-item">
            <a href="/admin/groups" class="nav-link link-dark"><bht:localeTag key="admin.groups" /></a>
        </li>
        <li class="nav-item">
            <a href="/admin/subjects" class="nav-link link-dark"><bht:localeTag key="admin.subjects" /></a>
        </li>
        <li class="mt-auto border-top border-2 pt-3">
            <div class="dropdown">
                <a class="btn btn-secondary dropdown-toggle w-100" href="#" role="button" id="dropdownMenuLink" data-bs-toggle="dropdown" aria-expanded="false">
                    <bht:localeTag key="footer.lang" /> - ${cookie['localeCode'].value}
                </a>

                <ul class="dropdown-menu" aria-labelledby="dropdownMenuLink">
                    <li><button id="ru_locale" class="dropdown-item" onclick="setLocale('ru');"><bht:localeTag key="lang.russian" /></button></li>
                    <li><button id="en_locale" class="dropdown-item" onclick="setLocale('en');"><bht:localeTag key="lang.english" /></button></li>
                </ul>
            </div>
        </li>
    </ul>
    <hr>
    <div class="dropdown">
        <a href="#" class="d-flex align-items-center link-dark text-decoration-none dropdown-toggle text-wrap"
           id="dropdownUser" data-bs-toggle="dropdown" aria-expanded="false">
            <img src="/static/images/profile/${sessionScope.user.profileImageName}" width="32" height="32"
                 class="rounded-circle me-2">
            <strong><jsp:text>${sessionScope.user.lastName} ${sessionScope.user.firstName}</jsp:text></strong>
        </a>
        <ul class="dropdown-menu text-small shadow" aria-labelledby="dropdownUser">
            <li><a class="dropdown-item" href="/settings"><bht:localeTag key="admin.settings" /></a></li>
            <li><hr class="dropdown-divider"></li>
            <li><a class="dropdown-item" href="/logout"><bht:localeTag key="admin.logout" /></a></li>
        </ul>
    </div>
</div>

<script>
    function confirmDelete() {
        return confirm("<bht:localeTag key="admin.delete_attention" />");
    }
</script>