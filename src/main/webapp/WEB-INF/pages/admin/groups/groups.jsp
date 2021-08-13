<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="bht" uri="bsuirhub-tags" %>

<!doctype html>
<html lang="en">
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
    <!-- custom -->
    <script src="/static/js/util/async_task_queue.js"></script>
    <script src="/static/js/util/fetch.js"></script>
    <script src="/static/js/util/delay.js"></script>
    <script src="/static/js/admin/groups/groups.js"></script>
</head>
<body data-locale-code="${cookie['localeCode'].value}">

<!-- Locale -->
<div id="admin_create" hidden><bht:localeTag key="admin.create" /></div>
<div id="admin_search" hidden><bht:localeTag key="admin.search" /></div>
<div id="admin_search_criteria" hidden><bht:localeTag key="admin.search_criteria" /></div>
<div id="admin_edit" hidden><bht:localeTag key="admin.edit" /></div>
<div id="admin_delete" hidden><bht:localeTag key="admin.delete" /></div>
<div id="group_name" hidden><bht:localeTag key="group.name" /></div>
<div id="group_department" hidden><bht:localeTag key="group.department" /></div>

<main class="d-flex flex-row h-100">
    <jsp:include page="../shared/sidebar.jsp" />

    <table id="dataTable" class="table table-striped table-borderless table-hover">
        <thead>
        <tr>
            <th>Id</th>
            <th><bht:localeTag key="group.name" /></th>
            <th><bht:localeTag key="group.department" /></th>
            <th><bht:localeTag key="group.headman" /></th>
            <th><bht:localeTag key="group.curator" /></th>
            <th><bht:localeTag key="admin.action" /></th>
        </tr>
        </thead>
        <tfoot>
        <tr>
            <th>Id</th>
            <th><bht:localeTag key="group.name" /></th>
            <th><bht:localeTag key="group.department" /></th>
            <th><bht:localeTag key="group.headman" /></th>
            <th><bht:localeTag key="group.curator" /></th>
            <th><bht:localeTag key="admin.action" /></th>
        </tr>
        </tfoot>
    </table>
</main>

</body>

</html>