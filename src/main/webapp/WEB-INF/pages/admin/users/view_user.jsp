<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<html>
<head>
    <jsp:include page="../shared/head.html" />
    <link href="/static/css/profile_image.css" rel="stylesheet">
    <script src="/static/lib/imageTools/imageTools.js"></script>
    <script src="/static/js/util/validation.js"></script>
    <script src="/static/js/util/upload.js"></script>
    <script src="/static/js/admin/users/view_user.js"></script>
</head>
<body>
<main class="d-flex flex-row h-100">
    <jsp:include page="../shared/sidebar.jsp" />

    <!-- for AJAX purposes -->
    <input hidden id="targetId" value="${id}">

    <div class="admin-main-area w-100 h-auto">
        <div class="container">
            <h1 class="display-3">
                <c:choose>
                    <c:when test="${new_entity_page}">
                        <img src="/static/images/default_profile.jpg" class="rounded-circle">
                        New user
                    </c:when>
                    <c:otherwise>
                        <input hidden id="filePicker" name="profileImage" type="file">
                        <div id="uploadPicture" class="image-container">
                            <img id="profileImage" src="/static/images/profile/${target_user.profilePicturePath}"
                                 class="image rounded-circle">
                            <div class="overlay rounded-circle">
                                <div class="image-text">Change profile picture</div>
                            </div>
                        </div>
                        Edit user
                    </c:otherwise>
                </c:choose>
            </h1>
        </div>
        <hr>
        <c:choose>
            <c:when test="${new_entity_page}">
                <form id="userForm" action="/admin/users/new" method="post" enctype="multipart/form-data">
            </c:when>
            <c:otherwise>
                <form id="userForm" action="/admin/users/edit" method="post" enctype="multipart/form-data">
            </c:otherwise>
        </c:choose>
                <c:if test="${success}">
                    <div class="alert alert-success" role="alert">
                        User successfully updated
                    </div>
                </c:if>

                <c:if test="${invalid_login}">
                    <div class="alert alert-danger" role="alert">
                        Invalid login
                    </div>
                </c:if>
                <c:if test="${not_unique_login}">
                    <div class="alert alert-danger" role="alert">
                        Not unique login
                    </div>
                </c:if>
                <c:if test="${invalid_email}">
                    <div class="alert alert-danger" role="alert">
                        Invalid email
                    </div>
                </c:if>
                <c:if test="${not_unique_email}">
                    <div class="alert alert-danger" role="alert">
                        Not unique email
                    </div>
                </c:if>
                <c:if test="${invalid_password}">
                    <div class="alert alert-danger" role="alert">
                        Password must contain 8 - 32 alphanumeric characters (letters and digits are required both)
                    </div>
                </c:if>
                <c:if test="${passwords_do_not_match}">
                    <div class="alert alert-danger" role="alert">
                        Passwords do not match
                    </div>
                </c:if>
                <c:if test="${invalid_first_name}">
                    <div class="alert alert-danger" role="alert">
                        First name must have 1 - 50 alphabetic characters
                    </div>
                </c:if>
                <c:if test="${invalid_patronymic}">
                    <div class="alert alert-danger" role="alert">
                        Patronymic must have 1 - 50 alphabetic characters
                    </div>
                </c:if>
                <c:if test="${invalid_last_name}">
                    <div class="alert alert-danger" role="alert">
                        Last name must have 1 - 50 alphabetic characters
                    </div>
                </c:if>
                <c:if test="${invalid_role}">
                    <div class="alert alert-danger" role="alert">
                        Cannot grant administrator role via this application
                    </div>
                </c:if>

                <input hidden type="text" name="id" value="${target_user.entityId}">
                <div class="form-group me-5 ms-5 mb-2">
                    <label for="loginInput">Login</label>
                    <input type="text" name="login" class="form-control" pattern="[0-9a-zA-Z]{8,20}"
                           id="loginInput" value="${target_user.login}"
                           <c:if test="${not new_entity_page}">readonly</c:if>
                    >
                </div>
                <div class="form-group me-5 ms-5 mb-2">
                    <label for="emailInput">Email</label>
                    <input type="email" name="email" class="form-control" maxlength="50" id="emailInput"
                           value="${target_user.email}"
                           placeholder="Leave it empty if you do not want set email (WARNING: user will be considered as not confirmed)"
                    >
                    <div class="invalid-feedback">
                        Invalid email
                    </div>
                </div>
                <div class="form-group me-5 ms-5 mb-2">
                    <label for="passwordInput">Password</label>
                    <input type="password" name="password" class="form-control"
                           pattern="(?=.*\w)(?=.*\d)[0-9a-zA-Z]{8,32}" id="passwordInput"
                           <c:if test="${new_entity_page}">required</c:if>
                           placeholder=<c:if test="${not new_entity_page}">"Leave it empty if you do not want to change password"</c:if>
                    >
                    <div class="invalid-feedback">
                        Password must contain 8 - 32 alphanumeric characters (letters and digits are reqiuired both)
                    </div>
                </div>
                <div class="form-group me-5 ms-5 mb-2">
                    <label for="confirmPasswordInput">Confirm password</label>
                    <input type="password" name="confirmPassword" class="form-control" id="confirmPasswordInput"
                            <c:if test="${new_entity_page}">required</c:if>
                            placeholder=<c:if test="${not new_entity_page}">"Leave it empty if you do not want to change password"</c:if>

                    >
                    <div class="invalid-feedback">
                        Passwords do not match
                    </div>
                </div>
                <div class="form-group me-5 ms-5 mb-2">
                    <label for="roleSelect">Role</label>
                    <select class="form-control" name="role" id="roleSelect">
                        <c:choose>
                            <c:when test="${target_user.userRole.roleId eq 3}">
                                <option value="1" disabled>Student</option>
                                <option value="2" disabled>Teacher</option>
                                <option value="3" selected readonly>Admin</option>
                            </c:when>
                            <c:when test="${target_user.userRole.roleId eq 1}">
                                <option value="1" selected>Student</option>
                                <option value="2">Teacher</option>
                                <option value="3" disabled>Admin</option>
                            </c:when>
                            <c:otherwise>
                                <option value="1">Student</option>
                                <option value="2" selected>Teacher</option>
                                <option value="3" disabled></option>
                            </c:otherwise>
                        </c:choose>
                    </select>
                </div>
                <div class="form-group me-5 ms-5 mb-2">
                    <label for="firstNameInput">First name</label>
                    <input type="text" name="firstName" class="form-control" pattern="[a-zA-Zа-яА-Я]{1,50}"
                           id="firstNameInput" value="${target_user.firstName}" required>
                    <div class="invalid-feedback">
                        First name must have 1 - 50 alphabetic characters
                    </div>
                </div>
                <div class="form-group me-5 ms-5 mb-2">
                    <label for="patornymicInput">Patronymic</label>
                    <input type="text" name="patronymic" class="form-control" pattern="[0-9a-zA-Zа-яА-Я]{1,50}"
                           id="patornymicInput" value="${target_user.patronymic}" required>
                    <div class="invalid-feedback">
                        Patronymic must have 1 - 50 alphabetic characters
                    </div>
                </div>
                <div class="form-group me-5 ms-5 mb-2">
                    <label for="lastNameInput">Last name</label>
                    <input type="text" name="lastName" class="form-control" pattern="[0-9a-zA-Zа-яА-Я]{1,50}"
                           id="lastNameInput" value="${target_user.lastName}" required>
                    <div class="invalid-feedback">
                        Last name must have 1 - 50 alphabetic characters
                    </div>
                </div>
                <input hidden type="text" name="profilePicturePath" value="${target_user.profilePicturePath}">
                <div class="form-group me-5 ms-5 mb-2">
                    <c:choose>
                        <c:when test="${target_user.userStatus.statusId eq 2}">
                            <input class="form-check-input" name="confirmed" type="checkbox" id="confirmedCheckbox" checked>
                        </c:when>
                        <c:otherwise>
                            <input class="form-check-input" name="confirmed" type="checkbox" id="confirmedCheckbox">
                        </c:otherwise>
                    </c:choose>
                    <label class="form-check-label" for="confirmedCheckbox">Confirmed</label>
                </div>
                <button type="submit" class="btn btn-secondary me-5 ms-5 mb-2" id="saveButton"
                        <c:if test="${new_entity_page}">disabled</c:if>
                >Save</button>
            </form>
    </div>
</main>
</body>
</html>
