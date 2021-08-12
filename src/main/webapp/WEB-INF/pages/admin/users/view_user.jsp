<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<html>
<head>
    <jsp:include page="../shared/head.html" />
    <!-- jQuery Select2 -->
    <link href="/static/lib/jquery-select2/css/select2.min.css" rel="stylesheet">
    <link href="/static/lib/jquery-select2-bootstrap/css/select2-bootstrap.min.css" rel="stylesheet">
    <script src="/static/lib/jquery-select2/js/select2.min.js"></script>
    <!-- custom -->
    <script src="/static/js/util/async_task_queue.js"></script>
    <script src="/static/js/util/fetch.js"></script>
    <link href="/static/css/profile_image.css" rel="stylesheet">
    <script src="/static/lib/imageTools/imageTools.js"></script>
    <script src="/static/js/util/validation.js"></script>
    <script src="/static/js/util/upload.js"></script>
    <script src="/static/js/admin/users/view_user.js"></script>
</head>
<body>
<main class="d-flex flex-row h-100" data-group-id="${targetEntity.groupId}">
    <jsp:include page="../shared/sidebar.jsp" />

    <div class="admin-main-area w-100 h-auto">
        <div class="container">
            <h1 class="display-3">
                <c:choose>
                    <c:when test="${newEntityPage}">
                        <img src="/static/images/profile/default_profile.jpg" class="rounded-circle">
                        New user
                    </c:when>
                    <c:otherwise>
                        <input hidden id="filePicker" name="profileImage" type="file">
                        <div id="uploadPicture" class="image-container">
                            <img id="profileImage" src="/static/images/profile/${targetEntity.profileImageName}"
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
            <c:when test="${newEntityPage}">
                <form id="userForm" action="/admin/users/new" method="post" enctype="multipart/form-data">
            </c:when>
            <c:otherwise>
                <form id="userForm" action="/admin/users/edit" method="post" enctype="multipart/form-data">
            </c:otherwise>
        </c:choose>
                <c:if test="${entityUpdateSuccess}">
                    <div class="alert alert-success" role="alert">
                        User successfully updated
                    </div>
                </c:if>

                <c:if test="${validationError}">
                    <div class="alert alert-danger" role="alert">
                        A server-side validation error occurred
                    </div>
                </c:if>

                <c:if test="${notUniqueLogin}">
                    <div class="alert alert-danger" role="alert">
                        Not unique login
                    </div>
                </c:if>

                <c:if test="${notUniqueEmail}">
                    <div class="alert alert-danger" role="alert">
                        Not unique email
                    </div>
                </c:if>

                <input hidden id="targetId" type="text" name="id" value="${targetEntity.entityId}">
                <div class="form-group me-5 ms-5 mb-2">
                    <label for="loginInput">Login</label>
                    <input type="text" name="login" class="form-control" pattern="[0-9a-zA-Z]{8,20}"
                           id="loginInput" value="${targetEntity.login}"
                           <c:if test="${not newEntityPage}">readonly</c:if>
                    >
                </div>
                <div class="form-group me-5 ms-5 mb-2">
                    <label for="emailInput">Email</label>
                    <input type="email" name="email" class="form-control" maxlength="50" id="emailInput"
                           value="${targetEntity.email}"
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
                           <c:if test="${newEntityPage}">required</c:if>
                           placeholder=<c:if test="${not newEntityPage}">"Leave it empty if you do not want to change password"</c:if>
                    >
                    <div class="invalid-feedback">
                        Password must contain 8 - 32 alphanumeric characters (letters and digits are reqiuired both)
                    </div>
                </div>
                <div class="form-group me-5 ms-5 mb-2">
                    <label for="confirmPasswordInput">Confirm password</label>
                    <input type="password" name="confirmPassword" class="form-control" id="confirmPasswordInput"
                            <c:if test="${newEntityPage}">required</c:if>
                            placeholder=<c:if test="${not newEntityPage}">"Leave it empty if you do not want to change password"</c:if>
                    >
                    <div class="invalid-feedback">
                        Passwords do not match
                    </div>
                </div>
                <div class="form-group me-5 ms-5 mb-2">
                    <label for="roleSelect">Role</label>
                    <select class="form-control" name="role" id="roleSelect">
                        <c:choose>
                            <c:when test="${targetEntity.role.ordinal() eq 3}">
                                <option value="1" disabled>Student</option>
                                <option value="2" disabled>Teacher</option>
                                <option value="3" selected readonly>Admin</option>
                            </c:when>
                            <c:when test="${targetEntity.role.ordinal() eq 1}">
                                <option value="1" selected>Student</option>
                                <option value="2">Teacher</option>
                                <option value="3" disabled>Admin</option>
                            </c:when>
                            <c:otherwise>
                                <option value="1">Student</option>
                                <option value="2" selected>Teacher</option>
                                <option value="3" disabled>Admin</option>
                            </c:otherwise>
                        </c:choose>
                    </select>
                </div>
                <div class="form-group me-5 ms-5 mb-2" id="groupSelectBlock">
                    <label for="groupSelect">Group</label>
                    <select class="form-control" name="groupId" id="groupSelect"></select>
                </div>
                <div class="form-group me-5 ms-5 mb-2">
                    <label for="firstNameInput">First name</label>
                    <input type="text" name="firstName" class="form-control" pattern="[a-zA-Zа-яА-Я]{1,50}"
                           id="firstNameInput" value="${targetEntity.firstName}" required>
                    <div class="invalid-feedback">
                        First name must have 1 - 50 alphabetic characters
                    </div>
                </div>
                <div class="form-group me-5 ms-5 mb-2">
                    <label for="patornymicInput">Patronymic</label>
                    <input type="text" name="patronymic" class="form-control" pattern="[a-zA-Zа-яА-Я]{1,50}"
                           id="patornymicInput" value="${targetEntity.patronymic}" required>
                    <div class="invalid-feedback">
                        Patronymic must have 1 - 50 alphabetic characters
                    </div>
                </div>
                <div class="form-group me-5 ms-5 mb-2">
                    <label for="lastNameInput">Last name</label>
                    <input type="text" name="lastName" class="form-control" pattern="[a-zA-Zа-яА-Я]{1,50}"
                           id="lastNameInput" value="${targetEntity.lastName}" required>
                    <div class="invalid-feedback">
                        Last name must have 1 - 50 alphabetic characters
                    </div>
                </div>
                <input hidden type="text" name="profileImageName" value="${targetEntity.profileImageName}">
                <div class="form-group me-5 ms-5 mb-2">
                    <c:choose>
                        <c:when test="${targetEntity.status.statusId eq 2}">
                            <input class="form-check-input" name="confirmed" type="checkbox" id="confirmedCheckbox" checked>
                        </c:when>
                        <c:otherwise>
                            <input class="form-check-input" name="confirmed" type="checkbox" id="confirmedCheckbox">
                        </c:otherwise>
                    </c:choose>
                    <label class="form-check-label" for="confirmedCheckbox">Confirmed</label>
                </div>
                <button type="submit" class="btn btn-secondary me-5 ms-5 mb-2" id="saveButton"
                        <c:if test="${newEntityPage}">disabled</c:if>
                >Save</button>
            </form>
    </div>
</main>
</body>
</html>
