<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="bht" uri="bsuirhub-tags" %>

<html>
<head>
    <title><bht:localeTag key="admin.groups" /></title>
    <jsp:include page="../shared/head.html" />
    <!-- DataTables -->
    <script src="/static/lib/datatables/js/jquery.dataTables.min.js"></script>
    <link href="/static/lib/datatables/css/dataTables.bootstrap5.min.css" rel="stylesheet">
    <script src="/static/lib/datatables/js/dataTables.bootstrap5.min.js"></script>
    <!-- jQuery Select2 -->
    <link href="/static/lib/jquery-select2/css/select2.min.css" rel="stylesheet">
    <link href="/static/lib/jquery-select2-bootstrap/css/select2-bootstrap.min.css" rel="stylesheet">
    <script src="/static/lib/jquery-select2/js/select2.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/select2@4.0.13/dist/js/i18n/${cookie['localeCode'].value}.js"></script>
    <!-- DataTables Scroller -->
    <link href="/static/lib/datatables/css/scroller.dataTables.min.css">
    <script src="/static/lib/datatables/js/dataTables.scroller.min.js"></script>
    <!-- custom -->
    <script src="/static/js/util/async_task_queue.js"></script>
    <script src="/static/js/util/fetch.js"></script>
    <script src="/static/js/util/validation.js"></script>
    <script src="/static/js/admin/groups/view_group.js"></script>
</head>
<body>

<!-- Locale -->
<div id="group_choose_subject" hidden><bht:localeTag key="group.choose_subject" /></div>
<div id="group_choose_teacher" hidden><bht:localeTag key="group.choose_teacher" /></div>
<div id="admin_create" hidden><bht:localeTag key="admin.create" /></div>
<div id="admin_save" hidden><bht:localeTag key="admin.save" /></div>
<div id="admin_delete" hidden><bht:localeTag key="admin.delete" /></div>

<main class="d-flex flex-row h-100"
      data-locale-code="${cookie['localeCode'].value}"
      data-department-id="${targetEntity.departmentId}"
      data-curator-id="${targetEntity.curatorId}" d
      data-headman-id="${targetEntity.headmanId}">

    <jsp:include page="../shared/sidebar.jsp" />

    <div class="admin-main-area w-100 h-auto">
        <h1 class="display-3">
            <c:choose>
                <c:when test="${newEntityPage}"><bht:localeTag key="group.new_group" /></c:when>
                <c:otherwise><bht:localeTag key="group.edit_group" /></c:otherwise>
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
                        <bht:localeTag key="group.group_updated" />
                    </div>
                </c:if>
                <c:if test="${validationError}">
                    <div class="alert alert-danger" role="alert">
                        <bht:localeTag key="model.validation_error" />d
                    </div>
                </c:if>
                <c:if test="${notUniqueName}">
                    <div class="alert alert-danger" role="alert">
                        <bht:localeTag key="group.not_unique_name" />
                    </div>
                </c:if>

                <input id="groupId" hidden type="text" name="id" value="${targetEntity.entityId}">
                <div class="form-group mb-2">
                    <label for="nameInput"><bht:localeTag key="group.name" /></label>
                    <input type="text" name="name" class="form-control"
                           pattern="[0-9a-zA-Zа-яА-Я]{1,20}"
                           id="nameInput" value="${targetEntity.name}" required>
                    <div class="invalid-feedback">
                        <bht:localeTag key="group.group_name_validation" />
                    </div>
                </div>

                <div class="form-group mb-2">
                    <label for="departmentSelect"><bht:localeTag key="group.department" /></label>
                    <select name="departmentId" id="departmentSelect"></select>
                </div>

                <c:if test="${not newEntityPage}">
                    <div class="form-group mb-2">
                        <label for="headmanSelect"><bht:localeTag key="group.headman" /></label>
                        <select name="headmanId" id="headmanSelect"></select>
                    </div>
                </c:if>

                <div class="form-group mb-2">
                    <label for="curatorSelect"><bht:localeTag key="group.curator" /></label>
                    <select name="curatorId" id="curatorSelect" required></select>
                </div>
                <button type="submit" class="btn btn-secondary mb-2" id="saveButton"
                        <c:if test="${newEntityPage}">disabled</c:if>
                ><bht:localeTag key="admin.save" /></button>
            </form>

            <c:if test="${not newEntityPage}">
                <h1 class="display-6"><bht:localeTag key="group.subjects_and_teachers" /></h1>
                <table id="assignmentsTable" class="table table-borderless">
                    <thead>
                    <tr>
                        <th></th>
                    </tr>
                    </thead>
                    <tbody>
                    <tr>
                        <td><bht:localeTag key="group.assignments" /></td>
                    </tr>
                    </tbody>
                </table>
            </c:if>
    </div>
</main>

<div class="toast-container">
    <div class="position-fixed top-0 end-0 p-3" style="z-index: 11">
        <div id="createToast" class="toast" role="alert" aria-live="assertive" aria-atomic="true">
            <div class="toast-header">
                <strong class="me-auto"><bht:localeTag key="group.notification" /></strong>
                <button type="button" class="btn-close" data-bs-dismiss="toast" aria-label="Close"></button>
            </div>
            <div class="toast-body">
                <bht:localeTag key="group.assignment_created" />
            </div>
        </div>
    </div>


    <div class="position-fixed top-0 end-0 p-3" style="z-index: 11">
        <div id="updateToast" class="toast" role="alert" aria-live="assertive" aria-atomic="true">
            <div class="toast-header">
                <strong class="me-auto"><bht:localeTag key="group.notification" /></strong>
                <button type="button" class="btn-close" data-bs-dismiss="toast" aria-label="Close"></button>
            </div>
            <div class="toast-body">
                <bht:localeTag key="group.assignment_updated" />
            </div>
        </div>
    </div>

    <div class="position-fixed top-0 end-0 p-3" style="z-index: 11">
        <div id="deleteToast" class="toast" role="alert" aria-live="assertive" aria-atomic="true">
            <div class="toast-header">
                <strong class="me-auto"><bht:localeTag key="group.notification" /></strong>
                <button type="button" class="btn-close" data-bs-dismiss="toast" aria-label="Close"></button>
            </div>
            <div class="toast-body">
                <bht:localeTag key="group.assignment_deleted" />
            </div>
        </div>
    </div>
</div>

</body>
</html>