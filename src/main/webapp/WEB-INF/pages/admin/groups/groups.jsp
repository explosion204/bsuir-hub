<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!doctype html>
<html lang="en">
<head>
    <jsp:include page="../shared/head.html" />
    <script src="/static/js/admin/groups/groups.js"></script>
</head>
<body>
<main class="d-flex flex-row h-100">
    <jsp:include page="../shared/sidebar.jsp" />

    <table id="dataTable" class="table table-striped table-borderless table-hover">
        <thead>
        <tr>
            <th>Id</th>
            <th>Name</th>
            <th>Department</th>
            <th>Headman</th>
            <th>Curator</th>
            <th>Action</th>
        </tr>
        </thead>
        <tfoot>
        <tr>
            <th>Id</th>
            <th>Name</th>
            <th>Department</th>
            <th>Headman</th>
            <th>Curator</th>
            <th>Action</th>
        </tr>
        </tfoot>
    </table>
</main>

</body>

</html>

<script>
    function confirmDelete() {
        return confirm("Attention, you're trying to delete group. Are you sure?");
    }
</script>