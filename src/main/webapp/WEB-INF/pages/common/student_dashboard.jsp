<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="bht" uri="bsuirhub-tags" %>

<html>
<head>
    <title><bht:localeTag key="dashboard.student" /></title>
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
    <script src="/static/js/util/grade_styler.js"></script>
    <script src="/static/js/common/dashboard/student_dashboard.js"></script>
    <link href="/static/css/common/student_dashboard.css" rel="stylesheet">
</head>
<body data-user-id="${sessionScope.user.entityId}"
      data-group-id="${sessionScope.user.groupId}">

<jsp:include page="shared/header.jsp" />

<h1 class="display-4 m-2 text-center"><bht:localeTag key="dashboard.student" /></h1>
<main class="d-flex flex-row">
    <div class="wrapper w-25 ms-2 mt-2">
        <div class="d-flex flex-row border-bottom p-2">
            <p class="profile-image-holder dashboard mt-auto mb-auto">
                <img id="profileImage" class="profile-image dashboard" src="/static/images/profile/${sessionScope.user.profileImageName}">
            </p>
            <div class="ms-3 text-wrap">
                <div class="lead fs-2">${sessionScope.user.lastName}</div>
                <div class="lead">${sessionScope.user.firstName} ${sessionScope.user.patronymic}</div>
            </div>
        </div>
        <div class="lead fs-3 border-bottom text-center p-2">
            <div>
                <bht:localeTag key="dashboard.average_grade" />: <span id="averageStudyGrade" class="text-warning">${averageStudyGrade}</span>
            </div>
        </div>
        <div class="lead fs-3 border-bottom p-2 text-center">
            <div class="lead mt-1"><bht:localeTag key="dashboard.faculty" /></div>
            <div class="h6 mt-1 text-uppercase">${faculty.name}</div>
            <div class="h6 text-uppercase">(${faculty.shortName})</div>
        </div>
        <div class="lead fs-3 border-bottom p-2 text-center">
            <div class="lead mt-1"><bht:localeTag key="dashboard.department" /></div>
            <div class="h6 mt-1 text-uppercase">${department.name}</div>
            <div class="h6 text-uppercase">(${department.shortName})</div>
        </div>
        <div class="lead fs-3 border-bottom p-2 text-center">
            <div class="lead mt-1"><bht:localeTag key="dashboard.specialty" /></div>
            <div class="h6 mt-1 text-uppercase">${department.specialtyAlias}</div>
        </div>
        <div id="groupDiv" class="lead fs-3 p-2 text-center">
            <div class="lead mt-1"><bht:localeTag key="dashboard.group" /> <span class="h6">${group.name}</span></div>
            <div class="lead mt-1"><bht:localeTag key="dashboard.headman" /> <span class="h6">${headman.lastName} ${headman.firstName} ${headman.patronymic}</span></div>
            <div class="lead mt-1"><bht:localeTag key="dashboard.curator" /> <span class="h6">${curator.lastName} ${curator.firstName} ${curator.patronymic}</span></div>
        </div>
    </div>

    <div class="wrapper w-75 ms-2 mt-2">
        <table id="subjectsTable" class="table table-hover row-border">
            <thead>
            <tr>
                <th>
                    <h1 class="display-6 ms-2"><bht:localeTag key="dashboard.active_subjects" /></h1>
                </th>
            </tr>
            </thead>
            <tbody>
            </tbody>
        </table>
    </div>
</main>

<div class="modal fade" id="studentsModal" tabindex="-1" aria-hidden="true">
    <div class="modal-dialog modal-lg">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title" id="exampleModalLabel"><bht:localeTag key="dashboard.students_of_group" /> ${group.name}</h5>
                <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
            </div>
            <div class="modal-body">
                <table id="studentsTable" class="table">
                    <tbody>
                    </tbody>
                </table>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-secondary" data-bs-dismiss="modal"><bht:localeTag key="grades.close" /></button>
            </div>
        </div>
    </div>
</div>

<jsp:include page="shared/footer.jsp"/>
</body>
</html>
