<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="bht" uri="bsuirhub-tags" %>
<html>
<head>
    <title><bht:localeTag key="settings.account_settings" /></title>
    <jsp:include page="shared/head.html" />
    <link href="/static/css/common/settings.css" rel="stylesheet">
    <script src="/static/lib/imageTools/imageTools.js"></script>
    <script src="/static/js/util/validation.js"></script>
    <script src="/static/js/util/upload.js"></script>
    <script src="/static/js/common/account/settings.js"></script>
</head>
<body data-user-id="${sessionScope.user.entityId}">
    <jsp:include page="shared/header.jsp" />

    <div class="wrapper bg-white w-100 p-4">
        <h4 class="pb-4 border-bottom"><bht:localeTag key="settings.account_settings" /></h4>
        <div class="d-flex py-3 border-bottom">
            <img id="profileImage" src="/static/images/profile/${sessionScope.user.profileImageName}"
                 class="img rounded-circle" alt="">
            <div class="ms-4" id="img-section">
                <b><bht:localeTag key="settings.profile_image" /></b>
                <p><bht:localeTag key="settings.accepted" /></p>
                <input hidden id="filePicker" name="profileImage" type="file">
                <button id="uploadProfileImage" class="btn btn-secondary border"><b><bht:localeTag key="settings.upload" /></b></button>
            </div>
        </div>

        <c:if test="${emailChangeSuccess}">
            <div class="alert alert-success mt-2" role="alert">
                <bht:localeTag key="settings.confirmation_sent" />
            </div>
        </c:if>

        <c:if test="${validationError}">
            <div class="alert alert-danger" role="alert">
                <bht:localeTag key="model.validation_error" />
            </div>
        </c:if>


        <c:if test="${notUniqueEmail}">
            <div class="alert alert-danger" role="alert">
                <bht:localeTag key="settings.not_unique_email" />
            </div>
        </c:if>

        <form id="emailForm" method="post" action="/settings/change_email">
            <div class="form-group mb-2 mt-2">
                <label for="emailInput"><bht:localeTag key="settings.email" />
                    <c:choose>
                        <c:when test="${sessionScope.user.status.statusId eq 1}">
                    <span class="text-danger"><bht:localeTag key="settings.not_confirmed" /></span>
                        </c:when>
                        <c:otherwise>
                    <span class="text-success"><bht:localeTag key="settings.confirmed" /></span>
                        </c:otherwise>
                    </c:choose>
                </label>
                <input type="email" name="email" class="bg-light form-control" maxlength="50" id="emailInput"
                        value="${sessionScope.user.email}" required>
                <div class="invalid-feedback">
                    <bht:localeTag key="settings.invalid_email" />
                </div>
            </div>
            <div class="py-2 pb-3 border-bottom">
                <button type="submit" class="btn btn-secondary w-100" id="changeEmailButton" disabled><bht:localeTag key="settings.change_email" /></button>
                <c:if test="${sessionScope.user.status.statusId eq 1}">
                    <button type="submit" class="btn btn-secondary w-100 mt-2" id="resendConfirmation"><bht:localeTag key="settings.resend_confirmation" /></button>
                </c:if>
            </div>
        </form>

        <c:if test="${passwordChangeSuccess}">
            <div class="alert alert-success mt-2" role="alert">
                <bht:localeTag key="settings.password_changed" />
            </div>
        </c:if>

        <c:if test="${invalidCurrentPassword}">
            <div class="alert alert-danger mt-2" role="alert">
                <bht:localeTag key="settings.invalid_current_password" />
            </div>
        </c:if>

        <form id="passwordForm" method="post" action="/settings/change_password">
            <div class="form-group mb-2 mt-2">
                <label for="currentPasswordInput"><bht:localeTag key="settings.current_password" /></label>
                <input type="password" name="currentPassword" class="bg-light form-control" maxlength="50"
                       id="currentPasswordInput" required>
                <div class="invalid-feedback">
                    <bht:localeTag key="settings.field_required" />
                </div>
            </div>
            <div class="form-group mb-2 mt-2">
                <label for="passwordInput"><bht:localeTag key="settings.new_password" /></label>
                <input type="password" name="password" class="bg-light form-control" maxlength="50" id="passwordInput"
                       pattern="(?=.*[a-zA-Z])(?=.*\d)[a-zA-Z0-9]{8,32}" required>
                <div class="invalid-feedback">
                    <bht:localeTag key="settings.password_validation" />
                </div>
            </div>
            <div class="form-group mb-2 mt-2">
                <label for="confirmPasswordInput"><bht:localeTag key="settings.confirm_password" /></label>
                <input type="password" name="confirmPassword" class="bg-light form-control" maxlength="50" id="confirmPasswordInput"
                       pattern="(?=.*\w)(?=.*\d)[0-9a-zA-Z]{8,32}" required>
                <div class="invalid-feedback">
                    <bht:localeTag key="settings.passwords_do_not_match" />
                </div>
            </div>
            <div class="py-2 pb-3">
                <button type="submit" class="btn btn-secondary w-100" id="changePasswordButton" disabled><bht:localeTag key="settings.change_password" /></button>
            </div>
        </form>
    </div>

    <jsp:include page="shared/footer.jsp"/>
</body>
</html>
