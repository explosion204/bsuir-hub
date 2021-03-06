<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="bht" uri="bsuirhub-tags" %>

<html>
<head>
    <title><bht:localeTag key="error.internal_error_title" /></title>
    <jsp:include page="../shared/head.html" />
    <link rel="stylesheet" href="/static/css/error.css">
</head>
<body>
<jsp:include page="../shared/header.jsp" />

<div id="error">
    <div class="error-container">
        <div class="error">
            <h3><bht:localeTag key="error.internal_error_title" /></h3>
            <h1><span>5</span><span>0</span><span>0</span></h1>
        </div>
        <h2><bht:localeTag key="error.internal_error_detail" /></h2>
    </div>
</div>

<jsp:include page="../shared/footer.jsp"/>
</body>
</html>
