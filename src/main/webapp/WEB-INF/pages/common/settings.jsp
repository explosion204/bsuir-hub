<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Settings</title> <!-- TODO titles -->
    <jsp:include page="shared/head.html" />
    <link href="/static/css/common/settings.css" rel="stylesheet">
    <script src="/static/lib/imageTools/imageTools.js"></script>
    <script src="/static/js/util/validation.js"></script>
    <script src="/static/js/util/upload.js"></script>
    <script src="/static/js/common/settings.js"></script>
</head>
<body data-user-id="${sessionScope.user.entityId}">
    <jsp:include page="shared/header.jsp" />

    <div class="wrapper bg-white w-100 p-4">
        <h4 class="pb-4 border-bottom">Account settings</h4>
        <div class="d-flex py-3 border-bottom">
            <img id="profileImage" src="/static/images/profile/${sessionScope.user.profileImageName}"
                 class="img rounded-circle" alt="">
            <div class="ms-4" id="img-section">
                <b>Profile Image</b>
                <p>Accepted file type .jpg, .jpeg, .png less than 512KB</p>
                <input hidden id="filePicker" name="profileImage" type="file">
                <button id="uploadProfileImage" class="btn btn-secondary border"><b>Upload</b></button>
            </div>
        </div>

        <c:if test="${emailChangeSuccess}">
            <div class="alert alert-success mt-2" role="alert">
                Confirmation email sent
            </div>
        </c:if>


        <c:if test="${invalidEmail}">
            <div class="alert alert-danger" role="alert">
                Invalid email
            </div>
        </c:if>

        <c:if test="${notUniqueEmail}">
            <div class="alert alert-danger" role="alert">
                Not unique email
            </div>
        </c:if>

        <form id="emailForm" method="post" action="/settings/change_email">
            <div class="form-group mb-2 mt-2">
                <label for="emailInput">Email
                    <c:choose>
                        <c:when test="${sessionScope.user.status.statusId eq 1}">
                    <span class="text-danger">(not confirmed)</span>
                        </c:when>
                        <c:otherwise>
                    <span class="text-success">(confirmed)</span>
                        </c:otherwise>
                    </c:choose>
                </label>
                <input type="email" name="email" class="bg-light form-control" maxlength="50" id="emailInput"
                        value="${sessionScope.user.email}" required>
                <div class="invalid-feedback">
                    Invalid email
                </div>
            </div>
            <div class="py-2 pb-3 border-bottom">
                <button type="submit" class="btn btn-secondary w-100" id="changeEmailButton" disabled>Change email</button>
            </div>
        </form>

        <c:if test="${passwordChangeSuccess}">
            <div class="alert alert-success mt-2" role="alert">
                Password successfully changed
            </div>
        </c:if>

        <c:if test="${invalidCurrentPassword}">
            <div class="alert alert-danger mt-2" role="alert">
                Invalid current password
            </div>
        </c:if>

        <c:if test="${invalidPassword}">
            <div class="alert alert-danger" role="alert">
                Password must contain 8 - 32 alphanumeric characters (letters and digits are required both)
            </div>
        </c:if>

        <c:if test="${passwordsDoNotMatch}">
            <div class="alert alert-danger" role="alert">
                Passwords do not match
            </div>
        </c:if>

        <form id="passwordForm" method="post" action="/settings/change_password">
            <div class="form-group mb-2 mt-2">
                <label for="currentPasswordInput">Current password</label>
                <input type="password" name="currentPassword" class="bg-light form-control" maxlength="50"
                       id="currentPasswordInput" required>
                <div class="invalid-feedback">
                    Current password is required
                </div>
            </div>
            <div class="form-group mb-2 mt-2">
                <label for="passwordInput">New password</label>
                <input type="password" name="password" class="bg-light form-control" maxlength="50" id="passwordInput"
                       pattern="(?=.*\w)(?=.*\d)[0-9a-zA-Z]{8,32}" required>
                <div class="invalid-feedback">
                    Password must contain 8 - 32 alphanumeric characters (letters and digits are required both)
                </div>
            </div>
            <div class="form-group mb-2 mt-2">
                <label for="confirmPasswordInput">Confirm password</label>
                <input type="password" name="confirmPassword" class="bg-light form-control" maxlength="50" id="confirmPasswordInput"
                       pattern="(?=.*\w)(?=.*\d)[0-9a-zA-Z]{8,32}" required>
                <div class="invalid-feedback">
                    Passwords do not match
                </div>
            </div>
            <div class="py-2 pb-3">
                <button type="submit" class="btn btn-secondary w-100" id="changePasswordButton" disabled>Change password</button>
            </div>
        </form>
    </div>

    <jsp:include page="shared/footer.jsp"/>
</body>
</html>
