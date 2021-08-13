<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="bht" uri="bsuirhub-tags" %>

<html>
<head>
    <title><bht:localeTag key="admin.departments" /></title>

    <jsp:include page="../shared/head.html" />
    <!-- jQuery Select2 -->
    <link href="/static/lib/jquery-select2/css/select2.min.css" rel="stylesheet">
    <link href="/static/lib/jquery-select2-bootstrap/css/select2-bootstrap.min.css" rel="stylesheet">
    <script src="/static/lib/jquery-select2/js/select2.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/select2@4.0.13/dist/js/i18n/${cookie['localeCode'].value}.js"></script>
    <!-- custom -->
    <script src="/static/js/util/async_task_queue.js"></script>
    <script src="/static/js/util/fetch.js"></script>
    <script src="/static/js/util/validation.js"></script>
    <script src="/static/js/admin/departments/view_department.js"></script>
</head>
<body data-locale-code="${cookie['localeCode'].value}">
<main class="d-flex flex-row h-100"
      data-faculty-id="${targetEntity.facultyId}">

    <jsp:include page="../shared/sidebar.jsp" />

    <div class="admin-main-area w-100 h-auto">
        <div class="container">
            <h1 class="display-3">
                <c:choose>
                    <c:when test="${newEntityPage}"><bht:localeTag key="department.new_department"/></c:when>
                    <c:otherwise><bht:localeTag key="department.edit_department" /></c:otherwise>
                </c:choose>
            </h1>
        </div>
        <hr>
        <c:choose>
            <c:when test="${newEntityPage}">
                <form id="departmentForm" action="/admin/departments/new" method="post">
            </c:when>
            <c:otherwise>
                <form id="departmentForm" action="/admin/departments/edit?id=${targetEntity.entityId}" method="post">
            </c:otherwise>
        </c:choose>
                <c:if test="${entityUpdateSuccess}">
                    <div class="alert alert-success" role="alert">
                        <bht:localeTag key="department.department_updated" />
                    </div>
                </c:if>
                <c:if test="${validationError}">
                    <div class="alert alert-danger" role="alert">
                        <bht:localeTag key="model.validation_error" />
                    </div>
                </c:if>

                <input hidden type="text" name="id" value="${targetEntity.entityId}">
                <div class="form-group me-5 ms-5 mb-2">
                    <label for="nameInput"><bht:localeTag key="department.name" /></label>
                    <input type="text" name="name" class="form-control"
                           pattern="(?=[a-zA-Zа-яА-Я])([a-zA-Zа-яА-Я\s]{1,50})(?<=[a-zA-Zа-яА-Я])"
                           id="nameInput" value="${targetEntity.name}" required>
                    <div class="invalid-feedback">
                        <bht:localeTag key="department.department_name_validation" />
                    </div>
                </div>
                <div class="form-group me-5 ms-5 mb-2">
                    <label for="shortNameInput"><bht:localeTag key="department.short_name" /></label>
                    <input type="text" name="shortName" class="form-control"
                           pattern="[a-zA-Zа-яА-Я]{1,15}"
                           id="shortNameInput" value="${targetEntity.shortName}" required>
                    <div class="invalid-feedback">
                        <bht:localeTag key="department.department_short_name_validation" />
                    </div>
                </div>
                <div class="form-group me-5 ms-5 mb-2">
                    <label for="specialtyAliasInput"><bht:localeTag key="department.specialty_alias" /></label>
                    <input type="text" name="specialtyAlias" class="form-control"
                           pattern="(?=[a-zA-Zа-яА-Я,])([a-zA-Zа-яА-Я\s,]{1,100})(?<=[a-zA-Zа-яА-Я,])"
                           id="specialtyAliasInput" value="${targetEntity.specialtyAlias}" required>
                    <div class="invalid-feedback">
                        <bht:localeTag key="department.department_specialty_validation" />
                    </div>
                </div>
                <div class="form-group me-5 ms-5 mb-2">
                    <label for="facultySelect"><bht:localeTag key="department.faculty" /></label>
                    <select name="facultyId" id="facultySelect" required></select>
                </div>
                <button type="submit" class="btn btn-secondary me-5 ms-5 mb-2" id="saveButton"
                        <c:if test="${newEntityPage}">disabled</c:if>
                ><bht:localeTag key="admin.save" /></button>
            </form>
    </div>
</main>
</body>
</html>