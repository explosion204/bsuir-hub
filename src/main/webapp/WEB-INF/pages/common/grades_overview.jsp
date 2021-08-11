<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="role" value="${sessionScope.user.role.ordinal()}" />

<html>
<head>
    <jsp:include page="shared/head.html" />
    <script src="/static/js/util/validation.js"></script>
    <link href="/static/css/common/student_dashboard.css" rel="stylesheet">
    <link href="/static/css/common/grades.css" rel="stylesheet">
    <script src="/static/js/common/grades_shared.js"></script>
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
      data-user-id="${sessionScope.user.entityId}"
      data-grade-id="${gradeId}">

    <jsp:include page="shared/header.jsp" />

    <div class="display-6 text-center">${subject.name} <b>(${subject.shortName})</b></div>
    <div class="lead text-center border-bottom mt-2 pb-2">
        <div class="fs-5">${student.lastName} ${student.patronymic} ${student.firstName}</div>
        <div class="fs-6">Average subject grade: <span id="avgGrade">${averageStudyGrade}</span></div>
    </div>
    <div class="wrapper ms-5 me-5">
        <table id="gradesTable" class="table row-border ms-2">
            <thead>
            <tr>
                <th></th>
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

    <div class="modal fade" id="commentsModal" tabindex="-1" aria-hidden="true">
        <div class="modal-dialog modal-lg">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title" id="exampleModalLabel">Comments</h5>
                    <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                </div>
                <div class="modal-body">
                    <table id="commentsTable" class="comments-table table">
                        <thead>
                        <tr>
                            <th></th>
                        </tr>
                        </thead>
                        <tbody>
                        </tbody>
                    </table>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Close</button>
                </div>
            </div>
        </div>
    </div>

    <jsp:include page="shared/footer.jsp"/>
</body>
</html>
