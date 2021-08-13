<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="bht" uri="bsuirhub-tags" %>

<html>
<head>
    <title><bht:localeTag key="dashboard.teacher" /></title>
    <jsp:include page="shared/head.html" />
    <!-- Datatables -->
    <script src="/static/lib/datatables/js/jquery.dataTables.min.js"></script>
    <link href="/static/lib/datatables/css/dataTables.bootstrap5.min.css" rel="stylesheet">
    <script src="/static/lib/datatables/js/dataTables.bootstrap5.min.js"></script>
    <!-- DataTables Scroller -->
    <link href="/static/lib/datatables/css/scroller.dataTables.min.css">
    <script src="/static/lib/datatables/js/dataTables.scroller.min.js"></script>
    <!-- custom -->
    <script src="/static/js/util/async_task_queue.js"></script>
    <script src="/static/js/util/fetch.js"></script>
    <script src="/static/js/common/dashboard/teacher_dashboard.js"></script>
    <link href="/static/css/common/teacher_dashboard.css" rel="stylesheet">
</head>
<body data-locale-code="${cookie['localeCode'].value}" data-teacher-id="${sessionScope.user.entityId}">
    <jsp:include page="shared/header.jsp" />
    <h1 class="display-4 m-2 text-center"><bht:localeTag key="dashboard.teacher" /></h1>
    <main class="d-flex flex-row">
        <div class="wrapper">
            <table id="subjectsTable" class="table table-hover row-border">
                <thead>
                <tr>
                    <th>
                        <h1 class="display-6 ms-2"><bht:localeTag key="dashboard.your_subjects" /></h1>
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
                        <h1 class="display-6 ms-2"><bht:localeTag key="dashboard.groups" /></h1>
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
                        <h1 class="display-6 ms-2"><bht:localeTag key="dashboard.students" /></h1>
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
