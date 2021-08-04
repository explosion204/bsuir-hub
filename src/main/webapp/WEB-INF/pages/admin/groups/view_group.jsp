<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<html>
<head>
    <jsp:include page="../shared/head.html" />
    <script src="/static/js/util/validation.js"></script>
    <script src="/static/js/admin/groups/view_group.js"></script>
</head>
<body>
<main class="d-flex flex-row h-100"
      data-department-id="${target_entity.departmentId}"
      data-curator-id="${target_entity.curatorId}" d
      data-headman-id="${target_entity.headmanId}">

    <jsp:include page="../shared/sidebar.jsp" />

    <div class="admin-main-area w-100 h-auto">
        <h1 class="display-3">
            <c:choose>
                <c:when test="${new_entity_page}">New group</c:when>
                <c:otherwise>Edit group</c:otherwise>
            </c:choose>
        </h1>
        <hr>
        <c:choose>
            <c:when test="${new_entity_page}">
                <form id="groupForm" action="/admin/groups/new" method="post" novalidate>
            </c:when>
            <c:otherwise>
                <form id="groupForm" action="/admin/groups/edit?id=${target_entity.entityId}" method="post" novalidate>
            </c:otherwise>
        </c:choose>
                <c:if test="${entity_update_success}">
                    <div class="alert alert-success" role="alert">
                        Group successfully updated
                    </div>
                </c:if>
                <c:if test="${invalid_name}">
                    <div class="alert alert-danger" role="alert">
                        Group name must have 1 - 20 alphanumeric characters
                    </div>
                </c:if>
                <c:if test="${not_unique_name}">
                    <div class="alert alert-danger" role="alert">
                        Not unique name
                    </div>
                </c:if>

                <input id="groupId" hidden type="text" name="id" value="${target_entity.entityId}">
                <div class="form-group mb-2">
                    <label for="nameInput">Name</label>
                    <input type="text" name="name" class="form-control"
                           pattern="[0-9a-zA-Zа-яА-Я]{1,20}"
                           id="nameInput" value="${target_entity.name}" required>
                    <div class="invalid-feedback">
                        Group name must have 1 - 20 alphanumeric characters
                    </div>
                </div>

                <div class="form-group mb-2">
                    <label for="departmentSelect">Department</label>
                    <select name="departmentId" id="departmentSelect"></select>
                </div>

                <c:if test="${not new_entity_page}">
                    <div class="form-group mb-2">
                        <label for="headmanSelect">Headman</label>
                        <select name="headmanId" id="headmanSelect"></select>
                    </div>
                </c:if>

                <div class="form-group mb-2">
                    <label for="curatorSelect">Curator</label>
                    <select name="curatorId" id="curatorSelect" required></select>
                </div>
                <button type="submit" class="btn btn-secondary mb-2" id="saveButton"
                        <c:if test="${new_entity_page}">disabled</c:if>
                >Save</button>
            </form>

            <h1 class="display-6">Subjects and teachers</h1>
    </div>
</main>
</body>

<script>
    <c:forEach var="assignment" items="${study_assignments}">
        addExistingAssignment(${assignment.entityId}, ${assignment.subjectId}, ${assignment.teacherId});
    </c:forEach>
    $('script').remove();
</script>
</html>