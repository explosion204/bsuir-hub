<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<html>
<head>
    <jsp:include page="shared/head.html" />
    <script src="/static/js/util/validation.js"></script>
    <script src="/static/js/common/grades.js.js"></script>
    <link href="/static/css/common/student.css" rel="stylesheet">
</head>
<body data-teacher-id="${sessionScope.user.entityId}">
    <jsp:include page="shared/header.jsp" />

    <div class="display-6 text-center">Методы численного анализа</div>
    <div class="lead text-center border-bottom mt-2 pb-2">
        <div class="fs-5">Карнышов Дмитрий Георгиевич</div>
        <div class="fs-6">Average subject grade: <span class="text-success">8.0</span></div>
    </div>
    <div class="wrapper ms-5 me-5">
        <table id="gradesTable" class="table table-hover row-border">
            <thead>
            <tr>
                <th>Teacher</th>
                <th>Date</th>
                <th>Grade</th>
            </tr>
            </thead>
            <tbody>
            <tr>
                <td>
                    <div class="lead fs-5">Teacher #1</div>
                </td>
                <td>
                    <div class="lead fs-5">Date #1</div>
                </td>
                <td>
                    <div class="lead fs-5 text-success">9</div>
                </td>
            </tr>
            <tr>
                <td>
                    <div class="lead fs-5">Teacher #1</div>
                </td>
                <td>
                    <div class="lead fs-5">Date #1</div>
                </td>
                <td>
                    <div class="lead fs-5 text-success">9</div>
                </td>
            </tr>
            <tr>
                <td>
                    <div class="lead fs-5">Teacher #1</div>
                </td>
                <td>
                    <div class="lead fs-5">Date #1</div>
                </td>
                <td>
                    <div class="lead fs-5 text-success">9</div>
                </td>
            </tr>
            <tr>
                <td>
                    <div class="lead fs-5">Teacher #1</div>
                </td>
                <td>
                    <div class="lead fs-5">Date #1</div>
                </td>
                <td>
                    <div class="lead fs-5 text-success">9</div>
                </td>
            </tr>
            </tbody>
        </table>
    </div>

    <jsp:include page="shared/footer.jsp"/>
</body>
</html>
