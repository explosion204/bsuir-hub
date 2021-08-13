<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="bht" uri="bsuirhub-tags" %>

<html>
<head>
    <title><bht:localeTag key="index.home" /></title>
    <jsp:include page="shared/head.html" />
    <!-- jQuery Confirm -->
    <link href="/static/lib/jquery-confirm/css/jquery-confirm.min.css" rel="stylesheet">
    <script src="/static/lib/jquery-confirm/js/jquery-confirm.min.js"></script>
    <!-- custom -->
    <script src="/static/js/common/index.js"></script>
</head>
<body data-email-confirmation-success="${emailConfirmationSuccess}">
    <jsp:include page="shared/header.jsp" />

    <main class="d-flex flex-column h-100">
        <img class="w-25 h-auto ms-auto me-auto mt-5 pt-5 mb-1" src="/static/images/bsuir_logo.png" alt="bsuir_logo">
        <div class="lead fs-2 ms-auto me-auto mb-2 text-uppercase"><bht:localeTag key="index.welcome" /></div>
        <c:if test="${sessionScope.user.role.ordinal() eq 0}">
            <div class="lead fs-4 ms-auto me-auto mb-auto res"><bht:localeTag key="index.login_proposal" /></div>
        </c:if>
    </main>

    <jsp:include page="shared/footer.jsp"/>
</body>
</html>
