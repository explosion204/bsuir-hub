<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<html>
<head>
    <jsp:include page="shared/head.html" />
    <script src="/static/js/util/validation.js"></script>
    <script src="/static/js/common/teacher_dashboard.js"></script>
    <link href="/static/css/common/teacher.css" rel="stylesheet">
</head>
<body data-teacher-id="${sessionScope.user.entityId}">
    <jsp:include page="shared/header.jsp" />
    <h1 class="display-4 m-2 text-center">Teacher dashboard</h1>
    <main class="d-flex flex-row">
        <div class="wrapper">
            <table id="subjectsTable" class="table table-hover row-border">
                <thead>
                <tr>
                    <th>
                        <h1 class="display-6 ms-2">Your subjects</h1>
                    </th>
                </tr>
                </thead>
                <tbody>
                </tbody>
            </table>
        </div>

        <div class="wrapper">
            <table id="groupsTable" class="table table-hover row-border">
                <thead>
                <tr>
                    <th>
                        <h1 class="display-6 ms-2">Groups</h1>
                    </th>
                </tr>
                </thead>
                <tbody>
                </tbody>
            </table>
        </div>

        <div class="wrapper">
            <table id="studentsTable" class="table table-hover row-border">
                <thead>
                <tr>
                    <th>
                        <h1 class="display-6 ms-2">Students</h1>
                    </th>
                </tr>
                </thead>
                <tbody>
                </tbody>
            </table>
        </div>

    </main>

    <jsp:include page="shared/footer.jsp"/>
</body>
</html>
