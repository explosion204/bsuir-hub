<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="role" value="${sessionScope.user.role.ordinal()}" />

<html>
<head>
    <jsp:include page="shared/head.html" />
    <script src="/static/js/util/validation.js"></script>
    <link href="/static/css/common/student.css" rel="stylesheet">
    <link href="/static/css/common/grades.css" rel="stylesheet">

    <c:choose>
        <c:when test="${role eq 1}">
            <script src="/static/js/common/grades_student.js"></script>
        </c:when>
        <c:when test="${role eq 2 or role eq 3}">
            <script src="/static/js/common/grades_teacher.js"></script>
        </c:when>
    </c:choose>
</head>
<body data-student-id="${student.entityId}"
      data-subject-id="${subject.entityId}"
      data-teacher-id="${sessionScope.user.entityId}">

    <jsp:include page="shared/header.jsp" />

    <div class="display-6 text-center">${subject.name} <b>(${subject.shortName})</b></div>
    <div class="lead text-center border-bottom mt-2 pb-2">
        <div class="fs-5">${student.lastName} ${student.patronymic} ${student.firstName}</div>
        <div class="fs-6">Average subject grade: <span id="avgGrade">${averageStudyGrade}</span></div>
    </div>
    <div class="wrapper ms-5 me-5">
        <table id="gradesTable" class="table table-hover row-border">
            <thead>
            <tr>
                <th>Teacher</th>
                <th>Date</th>
                <th>Grade</th>
                <c:if test="${role eq 2 or role eq 3}">
                    <th>Action</th>
                </c:if>
            </tr>
            </thead>
            <tbody>
            </tbody>
        </table>
    </div>

    <jsp:include page="shared/footer.jsp"/>
</body>
</html>
