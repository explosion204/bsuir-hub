<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<html>
<head>
    <jsp:include page="shared/head.html" />
    <script src="/static/js/util/validation.js"></script>
    <script src="/static/js/common/student_dashboard.js"></script>
    <link href="/static/css/common/student_dashboard.css" rel="stylesheet">
</head>
<body data-user-id="${sessionScope.user.entityId}"
      data-group-id="${sessionScope.user.groupId}">

<jsp:include page="shared/header.jsp" />

<h1 class="display-4 m-2 text-center">Student dashboard</h1>
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
                Average grade: <span id="averageStudyGrade" class="text-warning">${averageStudyGrade}</span>
            </div>
        </div>
        <div class="lead fs-3 border-bottom p-2 text-center">
            <div class="lead mt-1">Факультет</div>
            <div class="h6 mt-1 text-uppercase">${faculty.name}</div>
            <div class="h6 text-uppercase">(${faculty.shortName})</div>
        </div>
        <div class="lead fs-3 border-bottom p-2 text-center">
            <div class="lead mt-1">Кафедра</div>
            <div class="h6 mt-1 text-uppercase">${department.name}</div>
            <div class="h6 text-uppercase">(${department.shortName})</div>
        </div>
        <div class="lead fs-3 border-bottom p-2 text-center">
            <div class="lead mt-1">Специальность</div>
            <div class="h6 mt-1 text-uppercase">${department.specialtyAlias}</div>
        </div>
        <div class="lead fs-3 p-2 text-center">
            <div class="lead mt-1">Группа <span class="h6">${group.name}</span></div>
            <div class="lead mt-1">Cтароста <span class="h6">${headman.lastName} ${headman.firstName} ${headman.patronymic}</span></div>
            <div class="lead mt-1">Куратор <span class="h6">${curator.lastName} ${curator.firstName} ${curator.patronymic}</span></div>
        </div>
    </div>

    <div class="wrapper w-75 ms-2 mt-2">
        <table id="subjectsTable" class="table table-hover row-border">
            <thead>
            <tr>
                <th>
                    <h1 class="display-6 ms-2">Active subjects</h1>
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