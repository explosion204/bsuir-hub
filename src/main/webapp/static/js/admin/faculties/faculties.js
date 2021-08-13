$(document).ready(function() {
    setActiveNavItem(2);
    let bodyBlock = $('body');
    let localeCode = bodyBlock.data('locale-code');
    let table = $('#dataTable').DataTable({
        dom: '<"toolbar">rtip',
        language: {
            url: `/static/lib/datatables/locale/${localeCode}.json`
        },
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
                    return '<a class="btn btn-secondary me-2" href="/admin/faculties/edit?id=' + row.entityId + '">' + $('#admin_edit').text() + '</a>' +
                    '<form style="display: inline" method="post" action="/admin/faculties/delete?id=' + row.entityId + '">' +
                    '   <input class="btn btn-secondary" type="submit" value="' + $('#admin_delete').text() + '" onclick="return confirmDelete();">' +
                    '</form>'
                }
            }
        ],
        initComplete: function () {
            dataTableInitComplete(table);
        }
    });
});

function dataTableInitComplete(table) {
    $("div.toolbar").html(`
            <div class="input-group mb-3">
                <button id="createButton" type="button" class="btn btn-secondary create-button">${$('#admin_create').text()}</button>
                <select id="searchCriteria" class="form-select">
                    <option disabled>${$('#admin_search_criteria').text()}</option>
                    <option value="NAME">${$('#faculty_name').text()}</option>
                    <option value="SHORT_NAME">${$('#faculty_short_name').text()}</option>
                </select>
                <input id="searchInput" maxlength="50" type="text" class="form-control w-50" placeholder="${$('#admin_search').text()}">
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
        window.location.href = '/admin/faculties/new';
    });
}