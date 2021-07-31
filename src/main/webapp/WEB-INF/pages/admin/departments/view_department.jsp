<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<html>
<head>
    <jsp:include page="../shared/head.html" />
    <script src="/static/js/util/validation.js"></script>
    <script src="/static/js/admin/departments/view_department.js"></script>
</head>
<body>
<main class="d-flex flex-row h-100">
    <jsp:include page="../shared/sidebar.jsp" />

    <!-- for AJAX purposes -->
    <input hidden id="facultyId" value="${target_entity.facultyId}">

    <div class="admin-main-area w-100 h-auto">
        <div class="container">
            <h1 class="display-3">
                <c:choose>
                    <c:when test="${new_entity_page}">New department</c:when>
                    <c:otherwise>Edit department</c:otherwise>
                </c:choose>
            </h1>
        </div>
        <hr>
        <c:choose>
            <c:when test="${new_entity_page}">
                <form id="departmentForm" action="/admin/departments/new" method="post">
            </c:when>
            <c:otherwise>
                <form id="departmentForm" action="/admin/departments/edit?id=${target_entity.entityId}" method="post">
            </c:otherwise>
        </c:choose>
                <c:if test="${entity_update_success}">
                    <div class="alert alert-success" role="alert">
                        Department successfully updated
                    </div>
                </c:if>
                <c:if test="${invalid_name}">
                    <div class="alert alert-danger" role="alert">
                        Department name must have 1 - 50 alphabetic and whitespace (except for trailing and leading) characters
                    </div>
                </c:if>
                <c:if test="${invalid_short_name}">
                    <div class="alert alert-danger" role="alert">
                        Department short name must have 1 - 15 alphabetic characters
                    </div>
                </c:if>
                <c:if test="${invalid_specialty_alias}">
                    <div class="alert alert-danger" role="alert">
                        Department short name must have 1 - 15 alphabetic characters
                    </div>
                </c:if>

                <input hidden type="text" name="id" value="${target_entity.entityId}">
                <div class="form-group me-5 ms-5 mb-2">
                    <label for="nameInput">Name</label>
                    <input type="text" name="name" class="form-control"
                           pattern="(?=[0-9a-zA-Zа-яА-Я])([0-9a-zA-Zа-яА-Я\s]{1,50})(?<=[0-9a-zA-Zа-яА-Я])"
                           id="nameInput" value="${target_entity.name}" required>
                    <div class="invalid-feedback">
                        Department name must have 1 - 50 alphabetic and whitespace (except for trailing and leading) characters
                    </div>
                </div>
                <div class="form-group me-5 ms-5 mb-2">
                    <label for="shortNameInput">Short name</label>
                    <input type="text" name="shortName" class="form-control"
                           pattern="[0-9a-zA-Zа-яА-Я]{1,15}"
                           id="shortNameInput" value="${target_entity.shortName}" required>
                    <div class="invalid-feedback">
                        Department short name must have 1 - 15 alphabetic characters
                    </div>
                </div>
                <div class="form-group me-5 ms-5 mb-2">
                    <label for="specialtyAliasInput">Specialty alias</label>
                    <input type="text" name="specialtyAlias" class="form-control"
                           pattern="(?=[0-9a-zA-Zа-яА-Я,])([0-9a-zA-Zа-яА-Я\s,]{1,100})(?<=[0-9a-zA-Zа-яА-Я,])"
                           id="specialtyAliasInput" value="${target_entity.specialtyAlias}" required>
                    <div class="invalid-feedback">
                        Department specialty alias must have 1 - 100 alphabetic and whitespace (except for trailing and leading) characters
                    </div>
                </div>
                <div class="form-group me-5 ms-5 mb-2">
                    <label for="facultySelect">Faculty</label>
                    <select name="facultyId" id="facultySelect" required>
                        <option value="${faculty_id}">${faculty_name}</option>
                    </select>
                </div>
                <button type="submit" class="btn btn-secondary me-5 ms-5 mb-2" id="saveButton"
                        <c:if test="${new_entity_page}">disabled</c:if>
                >Save</button>
            </form>
    </div>
</main>
</body>
</html>