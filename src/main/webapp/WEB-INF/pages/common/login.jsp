<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<fmt:setLocale value="${sessionScope.locale}" />
<fmt:setBundle basename="locale" />

<c:set var="login_placeholder"><fmt:message key="login.login_placeholder" /></c:set>
<c:set var="password_placeholder"><fmt:message key="login.password_placeholder" /></c:set>
<c:set var="login"><fmt:message key="login.login" /></c:set>

<html>
<head>
    <jsp:include page="shared/head.html" />
    <script src="/static/js/util/validation.js"></script>
    <script src="/static/js/common/login.js"></script>
    <link href="/static/css/common/login.css" rel="stylesheet">
</head>
<body>
    <jsp:include page="shared/header.jsp" />

    <div class="container login-container mt-auto mb-auto">
        <div class="row justify-content-center">
            <div class="col-md-6 login-form">
                <c:if test="${passwordChangeSuccess}">
                    <div class="alert alert-success mt-2" role="alert">
                        Password successfully changed
                    </div>
                </c:if>

                <form id="login" class="needs-validation" method="post" action="/login" novalidate>
                    <div class="form-group mb-3">
                        <input type="text" class="form-control" placeholder="${login_placeholder}" name="login" required />
                        <div class="invalid-feedback"><fmt:message key="login.required" /></div>
                    </div>
                    <div class="form-group mb-3">
                        <input type="password" class="form-control" placeholder="${password_placeholder}" name="password" required />
                        <div class="invalid-feedback"><fmt:message key="login.required" /></div>
                    </div>
                    <div class="form-group mb-3">
                        <!-- TODO button is disabled after submitting invalid data -->
                        <input id="loginButton" type="submit" class="btn-submit" value="${login}" disabled />
                    </div>
                    <c:if test="${not empty authError}">
                        <div class="alert alert-danger" role="alert">
                            <fmt:message key="login.error" />
                        </div>
                    </c:if>
                    <div class="form-group">
                        <a href="/login/forgot_password" class="forget-pwd"><fmt:message key="login.forgot_password" /></a>
                    </div>
                </form>
            </div>
        </div>
    </div>

    <jsp:include page="shared/footer.jsp"/>
</body>
</html>
