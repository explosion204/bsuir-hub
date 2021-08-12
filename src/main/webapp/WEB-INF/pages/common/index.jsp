<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<html>
<head>
    <jsp:include page="shared/head.html" />
    <!-- jQuery Confirm -->
    <link href="/static/lib/jquery-confirm/css/jquery-confirm.min.css" rel="stylesheet">
    <script src="/static/lib/jquery-confirm/js/jquery-confirm.min.js"></script>
    <!-- custom -->
    <script src="/static/js/common/index.js"></script>
</head>
<body data-email-confirmation-success="${emailConfirmationSuccess}">
    <jsp:include page="shared/header.jsp" />
    <jsp:include page="shared/footer.jsp"/>
</body>
</html>
