<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Settings</title> <!-- TODO titles -->
    <jsp:include page="shared/head.html" />
    <link href="/static/css/common/settings.css" rel="stylesheet">
    <script src="/static/js/util/validation.js"></script>
    <script src="/static/js/common/reset_password.js"></script>
</head>
<body>
<jsp:include page="shared/header.jsp" />

<div class="wrapper bg-white mt-auto mb-auto w-100 p-4">
    <c:if test="${passwordChangeSuccess}">
        <div class="alert alert-success mt-2" role="alert">
            Password successfully changed
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

    <h4 class="pb-2">Reset password</h4>
    <form id="resetPasswordForm" method="post" action="/login/reset_password">
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
            <button type="submit" class="btn btn-secondary w-100" id="resetPasswordButton" disabled>Reset</button>
        </div>
    </form>
</div>

<jsp:include page="shared/footer.jsp"/>
</body>
</html>
