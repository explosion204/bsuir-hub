$(document).ready(function() {
    setActiveNavItem(4);

    let table = $('#dataTable').DataTable({
        dom: '<"toolbar">rtip',
        scrollY: '70vh',
        scrollX: false,
        scrollResize: true,
        processing: true,
        serverSide: true,
        ordering: false,
        ajax: {
            url: '/ajax/get_groups',
            data: function (d) {
                d.requestType = "jquery_datatable";
                d.filterCriteria = $('#searchCriteria').val();
            }
        },
        columns: [
            { data: 'entityId' },
            { data: 'name' },
            {
                data: null,
                render: function (data, type, row, meta) {
                    return '<a class="link-secondary" href="/admin/departments/edit?id=' + row.departmentId + '">'
                            + row.departmentName + '</a>'
                }
            },
            {
                data: null,
                render: function (data, type, row, meta) {
                    return '<a class="link-secondary" href="/admin/users/edit?id=' + row.headmanId + '">'
                        + row.headmanLastName + '</a>'
                }
            },
            {
                data: null,
                render: function (data, type, row, meta) {
                    return '<a class="link-secondary" href="/admin/users/edit?id=' + row.curatorId + '">'
                        + row.curatorLastName + '</a>'
                }
            },
            {
                data: null,
                render: function (data, type, row, meta) {
                    return '<a class="btn btn-secondary me-2" href="/admin/groups/edit?id=' + row.entityId + '">Edit</a>' +
                    '<form style="display: inline" method="post" action="/admin/groups/delete?id=' + row.entityId + '">' +
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
                    <option value="DEPARTMENT">Department</option>
                </select>
                <input id="searchInput" maxlength="20" type="text" class="form-control w-50" placeholder="Search">
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
        if (e.params.data.id === 'DEPARTMENT') {
            searchInput.hide();
            searchSelect.show();
            searchSelect.select2({
                theme: 'bootstrap',
                width: '65%',
                maximumInputLength: 50,
                ajax: {
                    delay: 250,
                    url: '/ajax/get_departments',
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
        window.location.href = '/admin/groups/new';
    });
});