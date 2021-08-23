<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="bht" uri="bsuirhub-tags" %>

<html>
<head>
    <title><bht:localeTag key="reset.reset_password" /></title>
    <jsp:include page="shared/head.html" />
    <link href="/static/css/common/settings.css" rel="stylesheet">
    <script src="/static/js/util/validation.js"></script>
    <script src="/static/js/common/account/reset_password.js"></script>
</head>
<body>
<jsp:include page="shared/header.jsp" />

<c:if test="${validationError}">
    <div class="alert alert-danger" role="alert">
        <bht:localeTag key="model.validation_error" />
    </div>
</c:if>

<div class="wrapper bg-white mt-auto mb-auto w-100 p-4">
    <h4 class="pb-2"><bht:localeTag key="reset.reset_password" /></h4>
    <form id="resetPasswordForm" method="post" action="/login/reset_password">
        <div class="form-group mb-2 mt-2">
            <label for="passwordInput"><bht:localeTag key="reset.new_password" /></label>
            <input type="password" name="password" class="bg-light form-control" maxlength="50" id="passwordInput"
                   pattern="(?=.*\w)(?=.*\d)[0-9a-zA-Z]{8,32}" required>
            <div class="invalid-feedback">
                <bht:localeTag key="reset.password_validation" />
            </div>
        </div>
        <div class="form-group mb-2 mt-2">
            <label for="confirmPasswordInput"><bht:localeTag key="reset.confirm_password" /></label>
            <input type="password" name="confirmPassword" class="bg-light form-control" maxlength="50" id="confirmPasswordInput"
                   pattern="(?=.*[a-zA-Z])(?=.*\d)[a-zA-Z0-9]{8,32}" required>
            <div class="invalid-feedback">
                <bht:localeTag key="reset.passwords_do_not_match" />
            </div>
        </div>
        <div class="py-2 pb-3">
            <button type="submit" class="btn btn-secondary w-100" id="resetPasswordButton" disabled><bht:localeTag key="reset.reset" /></button>
        </div>
    </form>
</div>

<jsp:include page="shared/footer.jsp"/>
</body>
</html>
