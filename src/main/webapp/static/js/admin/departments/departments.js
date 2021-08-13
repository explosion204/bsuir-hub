$(document).ready(function() {
    setActiveNavItem(3);
    invalidateCache();
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
        drawCallback: function () { onDataLoaded(table); },
        ajax: {
            url: '/ajax/get_departments',
            data: function (d) {
                d.requestType = "jquery_datatable";
                d.filterCriteria = $('#searchCriteria').val();
            }
        },
        columns: [
            { data: 'entityId' },
            { data: 'name' },
            { data: 'shortName' },
            { data: 'specialtyAlias' },
            {
                data: null,
                render: function (data, type, row, meta) {
                    return '<a class="link-secondary" href="/admin/faculties/edit?id=' + row.facultyId + '"></a>'
                }
            },
            {
                data: null,
                render: function (data, type, row, meta) {
                    return '<a class="btn btn-secondary me-2" href="/admin/departments/edit?id=' + row.entityId + '">' + $('#admin_edit').text() + '</a>' +
                    '<form style="display: inline" method="post" action="/admin/departments/delete?id=' + row.entityId + '">' +
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
                    <option value="NAME">${$('#department_name').text()}</option>
                    <option value="SHORT_NAME">${$('#department_short_name').text()}</option>
                    <option value="FACULTY">${$('#department_faculty').text()}</option>
                </select>
                <input id="searchInput" maxlength="50" type="text" class="form-control w-50" placeholder="${$('#admin_search').text()}">
                <select id="searchSelect"></select>
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
        if (e.params.data.id === 'FACULTY') {
            searchInput.hide();
            searchSelect.show();
            searchSelect.select2({
                language: $('body').data('locale-code'),
                theme: 'bootstrap',
                width: '65%',
                maximumInputLength: 50,
                ajax: {
                    delay: 250,
                    url: '/ajax/get_faculties',
                    data: function (params) {
                        return {
                            term: params.term || '',
                            page: params.page || 1,
                            pageSize: 10,
                            requestType: 'jquery_select',
                        }
                    },
                    processResults: function (data, params) {
                        data = JSON.parse(data);
                        let mappedData = $.map(data.results, function (item) {
                            item.id = item.entityId;
                            item.text = item.name;
                            return item;
                        });
                        params.page = params.page || 1;

                        return {
                            results: mappedData,
                            pagination: {
                                more: data.paginationMore
                            }
                        }
                    }
                }
            });

        } else {
            searchSelect.html(''); // clear
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
        window.location.href = '/admin/departments/new';
    });
}

function onDataLoaded(table) {
    table.rows().data().each(function (value, index) {
        FETCH_QUEUE.append(fetchFaculty, [value.facultyId], function (entity) {
            let facultyName = entity.shortName;
            let cell = table.cell(index, 4).node();
            $(cell).find('a').text(facultyName);
        });
    })
}