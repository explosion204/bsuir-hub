$(document).ready(function() {
    setActiveNavItem(5);

    let table = $('#dataTable').DataTable({
        dom: '<"toolbar">rtip',
        scrollY: '70vh',
        scrollX: false,
        scrollResize: true,
        processing: true,
        serverSide: true,
        ordering: false,
        ajax: {
            url: '/ajax/get_subjects',
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
                    return '<a class="btn btn-secondary me-2" href="/admin/subjects/edit?id=' + row.entityId + '">Edit</a>' +
                    '<form style="display: inline" method="post" action="/admin/subjects/delete?id=' + row.entityId + '">' +
                    '   <input class="btn btn-secondary" type="submit" value="Delete" onclick="return confirmDelete();">' +
                    '</span>'
                }
            }
        ]
    });

    $("div.toolbar").html(`
            <div class="input-group mb-3">
                <button id="createButton" type="button" class="btn btn-secondary create-button">Create</button>
                <select id="searchCriteria" class="form-select">
                    <option disabled>Search criteria</option>
                    <option value="NAME">Name</option>
                    <option value="SHORT_NAME">Short name</option>
                </select>
                <input id="searchInput" maxlength="50" type="text" class="form-control w-50" placeholder="Search">
            </div>
        `
    );

    $('#searchCriteria').select2({
        minimumResultsForSearch: -1,
        theme: 'bootstrap',
        width: '10%'
    });

    $('#searchInput').keyup(delay(function () {
        let searchValue = $('#searchInput').val();
        table.search(searchValue).draw();
    }, 250));

    $('#createButton').click(function () {
        window.location.href = '/admin/subjects/new';
    });
});