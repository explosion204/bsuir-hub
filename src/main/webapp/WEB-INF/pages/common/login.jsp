<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="bht" uri="bsuirhub-tags" %>

<c:set var="login_placeholder"><bht:localeTag key="login.login_placeholder" /></c:set>
<c:set var="password_placeholder"><bht:localeTag key="login.password_placeholder" /></c:set>
<c:set var="login"><bht:localeTag key="login.login" /></c:set>

<html>
<head>
    <title><bht:localeTag key="login.login" /></title>
    <jsp:include page="shared/head.html" />
    <script src="/static/js/util/validation.js"></script>
    <script src="/static/js/common/account/login.js"></script>
    <link href="/static/css/common/login.css" rel="stylesheet">
</head>
<body data-auth-error="${authError}">
    <jsp:include page="shared/header.jsp" />

    <div class="container login-container mt-auto mb-auto">
        <div class="row justify-content-center">
            <div class="col-md-6 login-form">
                <c:if test="${passwordChangeSuccess}">
                    <div class="alert alert-success mt-2" role="alert">
                        <bht:localeTag key="login.password_changed" />
                    </div>
                </c:if>
                <c:if test="${validationError}">
                    <div class="alert alert-danger" role="alert">
                        <bht:localeTag key="model.validation_error" />
                    </div>
                </c:if>

                <form id="login" class="needs-validation" method="post" action="/login" novalidate>
                    <div class="form-group mb-3">
                        <input type="text" class="form-control" placeholder="${login_placeholder}" name="login" required />
                        <div class="invalid-feedback"><bht:localeTag key="login.field_required" /></div>
                    </div>
                    <div class="form-group mb-3">
                        <input type="password" class="form-control" placeholder="${password_placeholder}" name="password" required />
                        <div class="invalid-feedback"><bht:localeTag key="login.field_required" /></div>
                    </div>
                    <div class="form-group mb-3">
                        <input hidden name="returnUrl" value="${returnUrl}">
                        <input id="loginButton" type="submit" class="btn-submit" value="${login}" disabled />
                    </div>
                    <c:if test="${not empty authError}">
                        <div class="alert alert-danger" role="alert">
                            <bht:localeTag key="login.login_error" />
                        </div>
                    </c:if>
                    <div class="form-group">
                        <a href="/login/forgot_password" class="forget-pwd"><bht:localeTag key="login.forgot_password" /></a>
                    </div>
                </form>
            </div>
        </div>
    </div>

    <jsp:include page="shared/footer.jsp"/>
</body>
</html>
