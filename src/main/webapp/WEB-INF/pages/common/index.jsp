<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<html>
<head>
    <jsp:include page="shared/head.html" />
</head>
<body>
    <jsp:include page="shared/header.jsp" />
    <jsp:include page="shared/footer.jsp"/>
</body>
</html>

<c:if test="${emailConfirmationSuccess}">
    <script>
        $.confirm({
            title: 'Success',
            content: 'Email successfully confirmed!',
            buttons: {
                'OK': function () {

                },
            }
        });
    </script>
</c:if>
