<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<html>
<head>
    <jsp:include page="../shared/head.html" />
    <script src="/static/js/util/validation.js"></script>
    <script src="/static/js/admin/groups/view_group.js"></script>
</head>
<body>
<main class="d-flex flex-row h-100">
    <jsp:include page="../shared/sidebar.jsp" />

    <div class="admin-main-area w-100 h-auto">
        <div class="container">
            <h1 class="display-3">
                <c:choose>
                    <c:when test="${new_entity_page}">New group</c:when>
                    <c:otherwise>Edit groups</c:otherwise>
                </c:choose>
            </h1>
        </div>
        <hr>
        <c:choose>
            <c:when test="${new_entity_page}">
                <form id="groupForm" action="/admin/groups/new" method="post">
            </c:when>
            <c:otherwise>
                <form id="groupForm" action="/admin/groups/edit?id=${target_entity.entityId}" method="post">
            </c:otherwise>
        </c:choose>
                <c:if test="${entity_update_success}">
                    <div class="alert alert-success" role="alert">
                        Group successfully updated
                    </div>
                </c:if>
                <c:if test="${invalid_name}">
                    <div class="alert alert-danger" role="alert">
                        Group name must have 1 - 20 alphabetic characters
                    </div>
                </c:if>

                <input id="groupId" hidden type="text" name="id" value="${target_entity.entityId}">
                <div class="form-group me-5 ms-5 mb-2">
                    <label for="nameInput">Name</label>
                    <input type="text" name="name" class="form-control"
                           pattern="[0-9a-zA-Zа-яА-Я]{1,20}"
                           id="nameInput" value="${target_entity.name}" required>
                    <div class="invalid-feedback">
                        Group name must have 1 - 20 alphabetic characters
                    </div>
                </div>

                <div class="form-group me-5 ms-5 mb-2">
                    <label for="departmentSelect">Department</label>
                    <select name="departmentId" id="departmentSelect">
                        <option value="${department_id}">${department_name}</option>
                    </select>
                </div>

                <c:if test="${not new_entity_page}">
                    <div class="form-group me-5 ms-5 mb-2">
                        <label for="headmanSelect">Headman</label>
                        <select name="headmanId" id="headmanSelect">
                            <option value="${headman_id}">${headman_name}</option>
                        </select>
                    </div>
                </c:if>

                <div class="form-group me-5 ms-5 mb-2">
                    <label for="curatorSelect">Curator</label>
                    <select name="curatorId" id="curatorSelect" required>
                        <option value="${curator_id}">${curator_name}</option>
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