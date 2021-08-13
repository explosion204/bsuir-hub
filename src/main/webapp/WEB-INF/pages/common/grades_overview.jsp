<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="bht" uri="bsuirhub-tags" %>
<c:set var="role" value="${sessionScope.user.role.ordinal()}" />

<html>
<head>
    <title><bht:localeTag key="grades.grades_overview" /></title>
    <jsp:include page="shared/head.html" />
    <!-- Datatables -->
    <script src="/static/lib/datatables/js/jquery.dataTables.min.js"></script>
    <link href="/static/lib/datatables/css/dataTables.bootstrap5.min.css" rel="stylesheet">
    <script src="/static/lib/datatables/js/dataTables.bootstrap5.min.js"></script>
    <!-- DataTables Scroller -->
    <link href="/static/lib/datatables/css/scroller.dataTables.min.css">
    <script src="/static/lib/datatables/js/dataTables.scroller.min.js"></script>
    <!-- Font Awesome -->
    <link href="/static/lib/fontawesome/css/all.min.css" rel="stylesheet">
    <!-- custom -->
    <script src="/static/js/util/async_task_queue.js"></script>
    <script src="/static/js/util/fetch.js"></script>
    <script src="/static/js/util/grade_styler.js"></script>
    <link href="/static/css/common/student_dashboard.css" rel="stylesheet">
    <link href="/static/css/common/grades.css" rel="stylesheet">
    <script src="/static/js/common/grades/grades_shared.js"></script>
    <c:choose>
        <c:when test="${role eq 1}">
            <script src="/static/js/common/grades/grades_student.js"></script>
        </c:when>
        <c:when test="${role eq 2 or role eq 3}">
            <script src="/static/js/common/grades/grades_teacher.js"></script>
        </c:when>
    </c:choose>
</head>
<body data-locale-code="${cookie['localeCode'].value}"
      data-student-id="${student.entityId}"
      data-subject-id="${subject.entityId}"
      data-user-id="${sessionScope.user.entityId}"
      data-grade-id="${gradeId}">

    <!-- Locale -->
    <div id="grades_new_grade" hidden><bht:localeTag key="grades.new_grade" /></div>
    <div id="grades_update" hidden><bht:localeTag key="grades.update" /></div>
    <div id="grades_delete" hidden><bht:localeTag key="grades.delete" /></div>
    <div id="grades_new_comment" hidden><bht:localeTag key="grades.new_comment" /></div>
    <div id="grades_comment_text" hidden><bht:localeTag key="grades.comment_text" /></div>

    <jsp:include page="shared/header.jsp" />

    <div class="display-6 text-center">${subject.name} <b>(${subject.shortName})</b></div>
    <div class="lead text-center border-bottom mt-2 pb-2">
        <div class="fs-5">${student.lastName} ${student.patronymic} ${student.firstName}</div>
        <div class="fs-6"><bht:localeTag key="grades.average_subject_grade" />: <span id="avgGrade">${averageStudyGrade}</span></div>
    </div>
    <div class="wrapper ms-5 me-5">
        <table id="gradesTable" class="table row-border ms-2">
            <thead>
            <tr>
                <th></th>
                <th><bht:localeTag key="grades.teacher" /></th>
                <th><bht:localeTag key="grades.date" /></th>
                <th><bht:localeTag key="grades.grade" /></th>
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
                    <h5 class="modal-title" id="exampleModalLabel"><bht:localeTag key="grades.comments" /></h5>
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
                    <button type="button" class="btn btn-secondary" data-bs-dismiss="modal"><bht:localeTag key="grades.close" /></button>
                </div>
            </div>
        </div>
    </div>

    <jsp:include page="shared/footer.jsp"/>
</body>
</html>
