<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Settings</title> <!-- TODO titles -->
    <jsp:include page="shared/head.html" />
    <link href="/static/css/common/settings.css" rel="stylesheet">
    <script src="/static/js/util/validation.js"></script>
    <script src="/static/js/common/forgot_password.js"></script>
</head>
<body>
<jsp:include page="shared/header.jsp" />

<div class="wrapper bg-white mt-auto mb-auto w-100 p-4">
    <c:if test="${passwordResetLinkSent}">
        <div class="alert alert-success mt-2" role="alert">
            Password reset link sent
        </div>
    </c:if>

    <h4 class="pb-2">Reset password</h4>
    <form id="forgotPasswordForm" method="post" action="/login/send_reset_password_link">
        <div class="form-group mb-2 mt-2">
            <label for="emailInput">Email</label>
            <input type="email" name="email" class="bg-light form-control" maxlength="50" id="emailInput" required>
            <div class="invalid-feedback">
                Invalid email
            </div>
        </div>
        <div class="py-2 pb-3">
            <button type="submit" class="btn btn-secondary w-100" id="sendEmailButton" disabled>Send reset link</button>
        </div>
    </form>
</div>

<jsp:include page="shared/footer.jsp"/>
</body>
</html>
