<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="bht" uri="bsuirhub-tags" %>

<!doctype html>
<html lang="en">
<head>
    <bht:localeTag key="users" />
    <jsp:include page="../shared/head.html" />
    <!-- DataTables -->
    <script src="/static/lib/datatables/js/jquery.dataTables.min.js"></script>
    <link href="/static/lib/datatables/css/dataTables.bootstrap5.min.css" rel="stylesheet">
    <script src="/static/lib/datatables/js/dataTables.bootstrap5.min.js"></script>
    <!-- jQuery Select2 -->
    <link href="/static/lib/jquery-select2/css/select2.min.css" rel="stylesheet">
    <link href="/static/lib/jquery-select2-bootstrap/css/select2-bootstrap.min.css" rel="stylesheet">
    <script src="/static/lib/jquery-select2/js/select2.min.js"></script>
    <!-- jQuery Confirm -->
    <link href="/static/lib/jquery-confirm/css/jquery-confirm.min.css" rel="stylesheet">
    <script src="/static/lib/jquery-confirm/js/jquery-confirm.min.js"></script>
    <!-- custom -->
    <script src="/static/js/util/delay.js"></script>
    <script src="/static/js/admin/users/users.js"></script>
</head>
<body>
<main class="d-flex flex-row h-100" data-issuer-id="${sessionScope.user.entityId}">
    <jsp:include page="../shared/sidebar.jsp" />

    <table id="dataTable" class="table table-striped table-borderless table-hover">
        <thead>
        <tr>
            <th>Id</th>
            <th><bht:localeTag key="user_login" /></th>
            <th>Email</th>
            <th>Role</th>
            <th>Status</th>
            <th>First name</th>
            <th>Patronymic</th>
            <th>Last name</th>
            <th>Action</th>
        </tr>
        </thead>
        <tfoot>
        <tr>
            <th>Id</th>
            <th>Login</th>
            <th>Email</th>
            <th>Role</th>
            <th>Status</th>
            <th>First name</th>
            <th>Patronymic</th>
            <th>Last name</th>
            <th>Action</th>
        </tr>
        </tfoot>
    </table>
</main>

</body>

</html>

<script>
    function confirmDelete() {
        return confirm("Attention, you're trying to delete user. Are you sure?");
    }
</script>