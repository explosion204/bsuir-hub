<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="bht" uri="bsuirhub-tags" %>

<c:set var="email_placeholder"><bht:localeTag key="user.email_placeholder" /></c:set>
<c:set var="password_placeholder"><bht:localeTag key="user.password_placeholder" /></c:set>

<html>
<head>
    <title><bht:localeTag key="admin.users" /></title>
    <jsp:include page="../shared/head.html" />
    <!-- jQuery Select2 -->
    <link href="/static/lib/jquery-select2/css/select2.min.css" rel="stylesheet">
    <link href="/static/lib/jquery-select2-bootstrap/css/select2-bootstrap.min.css" rel="stylesheet">
    <script src="/static/lib/jquery-select2/js/select2.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/select2@4.0.13/dist/js/i18n/${cookie['localeCode'].value}.js"></script>
    <!-- custom -->
    <script src="/static/js/util/async_task_queue.js"></script>
    <script src="/static/js/util/fetch.js"></script>
    <link href="/static/css/profile_image.css" rel="stylesheet">
    <script src="/static/lib/imageTools/imageTools.js"></script>
    <script src="/static/js/util/validation.js"></script>
    <script src="/static/js/util/upload.js"></script>
    <script src="/static/js/admin/users/view_user.js"></script>
</head>
<body data-not-unique-login="${notUniqueLogin}" data-not-unique-email="${notUniqueEmail}">

<!-- Locale -->
<div id="user_confirm_clear" hidden><bht:localeTag key="user.confirm_clear" /></div>
<div id="user_clear_success" hidden><bht:localeTag key="user.clear_success" /></div>

<main class="d-flex flex-row h-100"
      data-locale-code="${cookie['localeCode'].value}"
      data-group-id="${targetEntity.groupId}">
    <jsp:include page="../shared/sidebar.jsp" />

    <div class="admin-main-area w-100 h-auto">
        <div class="container">
            <h1 class="display-3">
                <c:choose>
                    <c:when test="${newEntityPage}">
                        <img src="/static/images/profile/default_profile.jpg" class="rounded-circle">
                        <th><bht:localeTag key="user.new_user" /></th>
                    </c:when>
                    <c:otherwise>
                        <input hidden id="filePicker" name="profileImage" type="file">
                        <div id="uploadPicture" class="image-container">
                            <img id="profileImage" src="/static/images/profile/${targetEntity.profileImageName}"
                                 class="image rounded-circle">
                            <div class="overlay rounded-circle">
                                <div class="image-text"><bht:localeTag key="user.change_profile_image" /></div>
                            </div>
                        </div>
                        <bht:localeTag key="user.edit_user" />
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
                        <bht:localeTag key="user.user_updated" />
                    </div>
                </c:if>

                <c:if test="${validationError}">
                    <div class="alert alert-danger" role="alert">
                        <bht:localeTag key="model.validation_error" />
                    </div>
                </c:if>

                <c:if test="${notUniqueLogin}">
                    <div class="alert alert-danger" role="alert">
                        <bht:localeTag key="user.not_unique_login" />
                    </div>
                </c:if>

                <c:if test="${notUniqueEmail}">
                    <div class="alert alert-danger" role="alert">
                        <bht:localeTag key="user.not_unique_email" />
                    </div>
                </c:if>

                <input hidden id="targetId" type="text" name="id" value="${targetEntity.entityId}">
                <div class="form-group me-5 ms-5 mb-2">
                    <label for="loginInput"><bht:localeTag key="user.login" /></label>
                    <input type="text" name="login" class="form-control" pattern="[0-9a-zA-Z]{8,20}"
                           id="loginInput" value="${targetEntity.login}"
                           <c:if test="${not newEntityPage}">readonly</c:if>
                    >
                    <div class="invalid-feedback">
                        <bht:localeTag key="user.login_validation" />
                    </div>
                </div>
                <div class="form-group me-5 ms-5 mb-2">
                    <label for="emailInput"><bht:localeTag key="user.email" /></label>
                    <input type="email" name="email" class="form-control" maxlength="50" id="emailInput"
                           value="${targetEntity.email}"
                           placeholder="${email_placeholder}"
                    >
                    <div class="invalid-feedback">
                        <bht:localeTag key="user.email_validation" />
                    </div>
                </div>
                <div class="form-group me-5 ms-5 mb-2">
                    <label for="passwordInput"><bht:localeTag key="user.password" /></label>
                    <input type="password" name="password" class="form-control"
                           pattern="(?=.*[a-zA-Z])(?=.*\d)[a-zA-Z0-9]{8,32}" id="passwordInput"
                           <c:if test="${newEntityPage}">required</c:if>
                           placeholder=<c:if test="${not newEntityPage}">"${password_placeholder}"</c:if>
                    >
                    <div class="invalid-feedback">
                        <bht:localeTag key="user.password_validation" />
                    </div>
                </div>
                <div class="form-group me-5 ms-5 mb-2">
                    <label for="confirmPasswordInput"><bht:localeTag key="user.confirm_password" /></label>
                    <input type="password" name="confirmPassword" class="form-control" id="confirmPasswordInput"
                            <c:if test="${newEntityPage}">required</c:if>
                            placeholder=<c:if test="${not newEntityPage}">"${password_placeholder}"</c:if>
                    >
                    <div class="invalid-feedback">
                        <bht:localeTag key="user.passwords_do_not_match" />
                    </div>
                </div>
                <div class="form-group me-5 ms-5 mb-2">
                    <label for="roleSelect">Role</label>
                    <select class="form-control" name="role" id="roleSelect">
                        <c:choose>
                            <c:when test="${targetEntity.role.ordinal() eq 3}">
                                <option value="1" disabled><bht:localeTag key="role.student" /></option>
                                <option value="2" disabled><bht:localeTag key="role.teacher" /></option>
                                <option value="3" selected readonly><bht:localeTag key="role.admin" /></option>
                            </c:when>
                            <c:when test="${targetEntity.role.ordinal() eq 1}">
                                <option value="1" selected><bht:localeTag key="role.student" /></option>
                                <option value="2"><bht:localeTag key="role.teacher" /></option>
                                <option value="3" disabled><bht:localeTag key="role.admin" /></option>
                            </c:when>
                            <c:otherwise>
                                <option value="1"><bht:localeTag key="role.student" /></option>
                                <option value="2" selected><bht:localeTag key="role.teacher" /></option>
                                <option value="3" disabled><bht:localeTag key="role.admin" /></option>
                            </c:otherwise>
                        </c:choose>
                    </select>
                </div>
                <div class="form-group me-5 ms-5 mb-2" id="groupSelectBlock">
                    <label for="groupSelect"><bht:localeTag key="user.group" /></label>
                    <select class="form-control" name="groupId" id="groupSelect"></select>
                </div>
                <div class="form-group me-5 ms-5 mb-2">
                    <label for="firstNameInput"><bht:localeTag key="user.first_name" /></label>
                    <input type="text" name="firstName" class="form-control" pattern="[a-zA-Zа-яА-Я]{1,50}"
                           id="firstNameInput" value="${targetEntity.firstName}" required>
                    <div class="invalid-feedback">
                        <bht:localeTag key="user.first_name_validation" />
                    </div>
                </div>
                <div class="form-group me-5 ms-5 mb-2">
                    <label for="patronymicInput"><bht:localeTag key="user.patronymic" /></label>
                    <input type="text" name="patronymic" class="form-control" pattern="[a-zA-Zа-яА-Я]{1,50}"
                           id="patronymicInput" value="${targetEntity.patronymic}" required>
                    <div class="invalid-feedback">
                        <bht:localeTag key="user.patronymic_validation" />
                    </div>
                </div>
                <div class="form-group me-5 ms-5 mb-2">
                    <label for="lastNameInput"><bht:localeTag key="user.last_name" /></label>
                    <input type="text" name="lastName" class="form-control" pattern="[a-zA-Zа-яА-Я]{1,50}"
                           id="lastNameInput" value="${targetEntity.lastName}" required>
                    <div class="invalid-feedback">
                        <bht:localeTag key="user.last_name_validation" />
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
                    <label class="form-check-label" for="confirmedCheckbox"><bht:localeTag key="user.confirmed" /></label>
                </div>
                <button type="submit" class="btn btn-secondary me-5 ms-5 mb-2" id="saveButton"
                        <c:if test="${newEntityPage}">disabled</c:if>
                ><bht:localeTag key="admin.save" /></button>
            </form>

            <c:if test="${targetEntity.role.ordinal() eq 1}">
                <button id="clearGradesButton" class="btn btn-secondary me-5 ms-5 mb-2">
                    <bht:localeTag key="user.clear_all_grades" />
                </button>
            </c:if>
    </div>
</main>
</body>
</html>
