<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<html>
<head>
    <jsp:include page="shared/head.html" />
    <script src="/static/js/util/validation.js"></script>
    <script src="/static/js/common/student_dashboard.js"></script>
    <link href="/static/css/common/student.css" rel="stylesheet">
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
                <div class="h6 mt-1">Группа <span id="groupName">${groupName}</span></div>
            </div>
        </div>
        <div class="lead fs-3 border-bottom text-center p-2">
            <div>
                Average exam grade: <span id="averageExamGrade" class="text-success"></span>
            </div>
            <div>
                Average study grade: <span id="averageStudyGrade" class="text-warning">${averageStudyGrade}</span>
            </div>
        </div>
        <table id="examResultsTable" class="table table-hover row-border">
            <thead>
            <tr>
                <th>
                    <h1 class="display-6 ms-2">Exam results</h1>
                </th>
            </tr>
            </thead>
            <tbody>
            <tr>
                <td>
                    <div class="d-flex flex-row lead">
                        <div class="ms-2 me-auto">Subject #1</div>
                        <span class=" text-success ms-auto me-2 fs-5">8</span>
                    </div>
                </td>
            </tr>
            </tbody>
        </table>
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
            <tr>
                <td><div class="ms-2 lead">Subject #1</div></td>
            </tr>
            <tr>
                <td><div class="ms-2 lead">Subject #1</div></td>
            </tr>
            <tr>
                <td><div class="ms-2 lead">Subject #1</div></td>
            </tr>
            <tr>
                <td><div class="ms-2 lead">Subject #1</div></td>
            </tr>
            <tr>
                <td><div class="ms-2 lead">Subject #1</div></td>
            </tr>
            <tr>
                <td><div class="ms-2 lead">Subject #1</div></td>
            </tr>
            <tr>
                <td><div class="ms-2 lead">Subject #1</div></td>
            </tr>
            </tbody>
        </table>
    </div>
</main>

<jsp:include page="shared/footer.jsp"/>
</body>
</html>
