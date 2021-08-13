<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="bht" uri="bsuirhub-tags" %>

<html>
<head>
    <title><bht:localeTag key="admin.subjects" /></title>
    <jsp:include page="../shared/head.html" />
    <!-- custom -->
    <script src="/static/js/util/validation.js"></script>
    <script src="/static/js/admin/subjects/view_subject.js"></script>
</head>
<body>
<main class="d-flex flex-row h-100">
    <jsp:include page="../shared/sidebar.jsp" />

    <div class="admin-main-area w-100 h-auto">
        <div class="container">
            <h1 class="display-3">
                <c:choose>
                    <c:when test="${newEntityPage}"><bht:localeTag key="subject.new_subject" /></c:when>
                    <c:otherwise><bht:localeTag key="subject.edit_subject" /></c:otherwise>
                </c:choose>
            </h1>
        </div>
        <hr>
        <c:choose>
            <c:when test="${newEntityPage}">
                <form id="subjectForm" action="/admin/subjects/new" method="post">
            </c:when>
            <c:otherwise>
                <form id="subjectForm" action="/admin/subjects/edit?id=${targetEntity.entityId}" method="post">
            </c:otherwise>
        </c:choose>
                <c:if test="${entityUpdateSuccess}">
                    <div class="alert alert-success" role="alert">
                        <bht:localeTag key="subject.subject_updated" />
                    </div>
                </c:if>

                <c:if test="${validationError}">
                    <div class="alert alert-danger" role="alert">
                        <bht:localeTag key="model.validation_error" />
                    </div>
                </c:if>

                <input hidden type="text" name="id" value="${targetEntity.entityId}">
                <div class="form-group me-5 ms-5 mb-2">
                    <label for="nameInput"><bht:localeTag key="subject.name" /></label>
                    <input type="text" name="name" class="form-control"
                           pattern="(?=[a-zA-Zа-яА-Я])([0-9a-zA-Zа-яА-Я,\s]{1,50})(?<=[a-zA-Zа-яА-Я])"
                           id="nameInput" value="${targetEntity.name}" required>
                    <div class="invalid-feedback">
                        <bht:localeTag key="subject.subject_name_validation" />
                    </div>
                </div>
                <div class="form-group me-5 ms-5 mb-2">
                    <label for="shortNameInput"><bht:localeTag key="subject.short_name" /></label>
                    <input type="text" name="shortName" class="form-control"
                           pattern="[a-zA-Zа-яА-Я]{1,15}"
                           id="shortNameInput" value="${targetEntity.shortName}" required>
                    <div class="invalid-feedback">
                        <bht:localeTag key="subject.subject_short_name_validation" />
                    </div>
                </div>
                <button type="submit" class="btn btn-secondary me-5 ms-5 mb-2" id="saveButton"
                        <c:if test="${newEntityPage}">disabled</c:if>
                ><bht:localeTag key="admin.save" /></button>
            </form>
    </div>
</main>
</body>
</html>