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
            url: '/api/get_users',
            data: function (d) {
                d.requestType = "jquery_datatable";
                d.filterCriteria = $('#searchCriteria').val();
            }
        },
        columns: [
            { data: 'entityId' },
            { data: 'login' },
            { data: 'email' },
            { data: 'userRole' },
            { data: 'userStatus' },
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
                <button id="createButton" type="button" class="btn btn-secondary">Create user</button>
                <select id="searchCriteria" class="form-select">
                    <option disabled>Search criteria</option>
                    <option value="LOGIN">Login</option>
                    <option value="EMAIL">Email</option>
                    <option value="LAST_NAME">Last name</option>
                </select>
                <input id="searchInput" type="text" class="form-control w-50" placeholder="Start typing...">
                <button id="searchButton" type="button" class="btn btn-secondary">Search</button>
                <button id="clearButton" type="button" class="btn btn-secondary me-2">Clear</button>
            </div>
        `
    );

    $('#searchInput').keyup(function(e) {
        let searchValue = $('#searchInput').val();
        if ((e.key === 'Enter' || e.keyCode === 13) && searchValue !== '') {
            table.search(searchValue).draw();
        }
    });

    $('#searchButton').click(function () {
        table.search($('#searchInput').val()).draw();
    });

    $('#clearButton').click(function () {
        $('#searchInput').val('');
        table.search('').draw();
    });

    $('#createButton').click(function () {
        window.location.href = '/admin/users/new';
    });
});