$(document).ready(function() {
    setActiveNavItem(2);

    let table = $('#dataTable').DataTable({
        dom: '<"toolbar">rtip',
        scrollY: '70vh',
        scrollX: false,
        scrollResize: true,
        processing: true,
        serverSide: true,
        ordering: false,
        ajax: {
            url: '/ajax/get_faculties',
            data: function (d) {
                d.requestType = "jquery_datatable";
                d.filterCriteria = $('#searchCriteria').val();
            }
        },
        columns: [
            { data: 'entityId' },
            { data: 'name' },
            { data: 'shortName' },
            {
                data: null,
                render: function (data, type, row, meta) {
                    return '<a class="btn btn-secondary me-2" href="/admin/faculties/edit?id=' + row.entityId + '">Edit</a>' +
                    '<form style="display: inline" method="post" action="/admin/faculties/delete?id=' + row.entityId + '">' +
                    '   <input class="btn btn-secondary" type="submit" value="Delete" onclick="return confirmDelete();">' +
                    '</span>'
                }
            }
        ]
    });

    $("div.toolbar").html(`
            <div class="input-group mb-3">
                <button id="createButton" type="button" class="btn btn-secondary">Create faculty</button>
                <select id="searchCriteria" class="form-select">
                    <option disabled>Search criteria</option>
                    <option value="NAME">Name</option>
                    <option value="SHORT_NAME">Short name</option>
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
        window.location.href = '/admin/faculties/new';
    });
});