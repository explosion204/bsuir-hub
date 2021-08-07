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
      data-department-id="${targetEntity.departmentId}"
      data-curator-id="${targetEntity.curatorId}" d
      data-headman-id="${targetEntity.headmanId}">

    <jsp:include page="../shared/sidebar.jsp" />

    <div class="admin-main-area w-100 h-auto">
        <h1 class="display-3">
            <c:choose>
                <c:when test="${newEntityPage}">New group</c:when>
                <c:otherwise>Edit group</c:otherwise>
            </c:choose>
        </h1>
        <hr>
        <c:choose>
            <c:when test="${newEntityPage}">
                <form id="groupForm" action="/admin/groups/new" method="post" novalidate>
            </c:when>
            <c:otherwise>
                <form id="groupForm" action="/admin/groups/edit?id=${targetEntity.entityId}" method="post" novalidate>
            </c:otherwise>
        </c:choose>
                <c:if test="${entityUpdateSuccess}">
                    <div class="alert alert-success" role="alert">
                        Group successfully updated
                    </div>
                </c:if>
                <c:if test="${invalidName}">
                    <div class="alert alert-danger" role="alert">
                        Group name must have 1 - 20 alphanumeric characters
                    </div>
                </c:if>
                <c:if test="${notUniqueName}">
                    <div class="alert alert-danger" role="alert">
                        Not unique name
                    </div>
                </c:if>

                <input id="groupId" hidden type="text" name="id" value="${targetEntity.entityId}">
                <div class="form-group mb-2">
                    <label for="nameInput">Name</label>
                    <input type="text" name="name" class="form-control"
                           pattern="[0-9a-zA-Zа-яА-Я]{1,20}"
                           id="nameInput" value="${targetEntity.name}" required>
                    <div class="invalid-feedback">
                        Group name must have 1 - 20 alphanumeric characters
                    </div>
                </div>

                <div class="form-group mb-2">
                    <label for="departmentSelect">Department</label>
                    <select name="departmentId" id="departmentSelect"></select>
                </div>

                <c:if test="${not newEntityPage}">
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
                        <c:if test="${newEntityPage}">disabled</c:if>
                >Save</button>
            </form>

            <h1 class="display-6">Subjects and teachers</h1>
            <table id="assignmentsTable" class="table table-borderless">
                <thead>
                <tr>
                    <th></th>
                </tr>
                </thead>
                <tbody>
                <tr>
                    <td>Assignment</td>
                </tr>
                </tbody>
            </table>
    </div>
</main>

<div class="toast-container">
    <div class="position-fixed top-0 end-0 p-3" style="z-index: 11">
        <div id="createToast" class="toast" role="alert" aria-live="assertive" aria-atomic="true">
            <div class="toast-header">
                <strong class="me-auto">Notification</strong>
                <button type="button" class="btn-close" data-bs-dismiss="toast" aria-label="Close"></button>
            </div>
            <div class="toast-body">
                Assignment successfully created
            </div>
        </div>
    </div>


    <div class="position-fixed top-0 end-0 p-3" style="z-index: 11">
        <div id="updateToast" class="toast" role="alert" aria-live="assertive" aria-atomic="true">
            <div class="toast-header">
                <strong class="me-auto">Notification</strong>
                <button type="button" class="btn-close" data-bs-dismiss="toast" aria-label="Close"></button>
            </div>
            <div class="toast-body">
                Assignment successfully updated
            </div>
        </div>
    </div>

    <div class="position-fixed top-0 end-0 p-3" style="z-index: 11">
        <div id="deleteToast" class="toast" role="alert" aria-live="assertive" aria-atomic="true">
            <div class="toast-header">
                <strong class="me-auto">Notification</strong>
                <button type="button" class="btn-close" data-bs-dismiss="toast" aria-label="Close"></button>
            </div>
            <div class="toast-body">
                Assignment successfully deleted
            </div>
        </div>
    </div>
</div>

</body>
</html>