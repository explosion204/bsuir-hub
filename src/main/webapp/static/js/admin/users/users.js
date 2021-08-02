$(document).ready(function() {
    setActiveNavItem(1);

    let table = $('#dataTable').DataTable({
        dom: '<"toolbar">rtip',
        scrollY: '70vh',
        scrollX: false,
        scrollResize: true,
        processing: true,
        serverSide: true,
        ordering: false,
        ajax: {
            url: '/ajax/get_users',
            data: function (d) {
                d.requestType = "jquery_datatable";
                d.filterCriteria = $('#searchCriteria').val();
            }
        },
        columns: [
            { data: 'entityId' },
            { data: 'login' },
            { data: 'email' },
            { data: 'role' },
            { data: 'status' },
            { data: 'firstName' },
            { data: 'patronymic' },
            { data: 'lastName' },
            {
                data: null,
                render: function (data, type, row, meta) {
                    let issuerId = Number.parseInt($("#issuerId").val());

                    if (issuerId === row.entityId || row.userRole === 'ADMIN') {
                        return '<a class="btn btn-secondary me-2" href="/admin/users/edit?id=' + row.entityId + '">Edit</a>' +
                            '<form style="display: inline" method="post" action="/admin/users/delete?id=' + row.entityId + '">' +
                            '   <input class="btn btn-secondary" type="submit" value="Delete" disabled>' +
                            '</span>'
                    } else {
                        return '<a class="btn btn-secondary me-2" href="/admin/users/edit?id=' + row.entityId + '">Edit</a>' +
                            '<form style="display: inline" method="post" action="/admin/users/delete?id=' + row.entityId + '">' +
                            '   <input class="btn btn-secondary" type="submit" value="Delete" onclick="return confirmDelete();">' +
                            '</span>'
                    }
                }
            }
        ]
    });

    $("div.toolbar").html(`
            <div class="input-group mb-3">
                <button id="createButton" type="button" class="btn btn-secondary create-button">Create</button>
                <select id="searchCriteria" class="form-select">
                    <optgroup label="Search criteria">
                        <option value="LOGIN">Login</option>
                        <option value="EMAIL">Email</option>
                        <option value="LAST_NAME">Last name</option>
                        <option value="ROLE">Role</option>
                    </optgroup>
                </select>
                <input id="searchInput" type="text" class="form-control w-50" placeholder="Search">
                <select id="searchSelect">
                    <option value="3">Admin</option>
                    <option value="2">Teacher</option>
                    <option value="1">Student</option>
                </select>
            </div>
        `
    );

    let searchCriteria = $('#searchCriteria');
    let searchInput = $('#searchInput');
    let searchSelect = $('#searchSelect');

    searchSelect.hide();

    searchCriteria.select2({
        minimumResultsForSearch: -1,
        theme: 'bootstrap',
        width: '10%'
    });

    searchCriteria.on('select2:select', function (e) {
        if (e.params.data.id === 'ROLE') {
            searchInput.hide();
            searchSelect.show();
            searchSelect.select2({
                theme: 'bootstrap',
                width: '65%',
            });
            searchSelect.trigger('select2:select');
        } else {
            table.search(searchInput.val()).draw(); // reset search results
            searchSelect.select2('destroy'); // destroy
            searchSelect.hide();
            searchInput.show();
        }
    });

    searchInput.keyup(delay(function () {
        let searchValue = $('#searchInput').val();
        table.search(searchValue).draw();
    }, 250));

    searchSelect.on('select2:select', function (e) {
        let searchValue = $(this).val();
        table.search(searchValue).draw();
    });

    $('#createButton').click(function () {
        window.location.href = '/admin/users/new';
    });
});