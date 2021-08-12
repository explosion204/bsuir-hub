<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<html>
<head>
    <jsp:include page="../shared/head.html" />
    <!-- custom -->
    <script src="/static/js/util/validation.js"></script>
    <script src="/static/js/admin/faculties/view_faculty.js"></script>
</head>
<body>
<main class="d-flex flex-row h-100">
    <jsp:include page="../shared/sidebar.jsp" />

    <div class="admin-main-area w-100 h-auto">
        <div class="container">
            <h1 class="display-3">
                <c:choose>
                    <c:when test="${newEntityPage}">New faculty</c:when>
                    <c:otherwise>Edit faculty</c:otherwise>
                </c:choose>
            </h1>
        </div>
        <hr>
        <c:choose>
            <c:when test="${newEntityPage}">
                <form id="facultyForm" action="/admin/faculties/new" method="post">
            </c:when>
            <c:otherwise>
                <form id="facultyForm" action="/admin/faculties/edit?id=${targetEntity.entityId}" method="post">
            </c:otherwise>
        </c:choose>
                <c:if test="${entityUpdateSuccess}">
                    <div class="alert alert-success" role="alert">
                        Faculty successfully updated
                    </div>
                </c:if>
                <c:if test="${validationError}">
                    <div class="alert alert-danger" role="alert">
                        A server-side validation error occurred
                    </div>
                </c:if>

                <input hidden type="text" name="id" value="${targetEntity.entityId}">
                <div class="form-group me-5 ms-5 mb-2">
                    <label for="nameInput">Name</label>
                    <input type="text" name="name" class="form-control"
                           pattern="(?=[a-zA-Zа-яА-Я])([0-9a-zA-Zа-яА-Я\s]{1,50})(?<=[a-zA-Zа-яА-Я])"
                           id="nameInput" value="${targetEntity.name}" required>
                    <div class="invalid-feedback">
                        Faculty name must have 1 - 50 alphabetic and whitespace (except for trailing and leading) characters
                    </div>
                </div>
                <div class="form-group me-5 ms-5 mb-2">
                    <label for="shortNameInput">Short name</label>
                    <input type="text" name="shortName" class="form-control"
                           pattern="[a-zA-Zа-яА-Я]{1,15}"
                           id="shortNameInput" value="${targetEntity.shortName}" required>
                    <div class="invalid-feedback">
                        Faculty short name must have 1 - 15 alphabetic characters
                    </div>
                </div>
                <button type="submit" class="btn btn-secondary me-5 ms-5 mb-2" id="saveButton"
                        <c:if test="${newEntityPage}">disabled</c:if>
                >Save</button>
            </form>
    </div>
</main>
</body>
</html>