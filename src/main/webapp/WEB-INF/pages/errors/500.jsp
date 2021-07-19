<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<fmt:setLocale value="${sessionScope.locale}" />
<fmt:setBundle basename="locale" />

<html>
<head>
    <jsp:include page="../shared/head.html" />
    <link rel="stylesheet" href="/static/css/error.css">
</head>
<body>
<jsp:include page="../shared/header.jsp" />

<div id="error">
    <div class="error-container">
        <div class="error">
            <h3><fmt:message key="error.internal_error_title" /></h3>
            <h1><span>5</span><span>0</span><span>0</span></h1>
        </div>
        <h2><fmt:message key="error.internal_error_detail" /></h2>
    </div>
</div>

<jsp:include page="../shared/footer.jsp"/>
</body>
</html>
