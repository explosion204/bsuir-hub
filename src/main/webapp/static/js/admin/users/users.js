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
                        <option value="GROUP">Group</option>
                    </optgroup>
                </select>
                <input id="searchInput" maxlength="50" type="text" class="form-control w-50" placeholder="Search">
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
        if (searchSelect.data('select2')) {
            searchSelect.html(''); // clear
            searchSelect.select2('destroy');
        }

        if (e.params.data.id === 'ROLE') {
            searchInput.hide();
            searchSelect.show();
            searchSelect.select2({
                theme: 'bootstrap',
                width: '65%',
                data: [
                    { id: '3', text: 'Admin' },
                    { id: '2', text: 'Teacher' },
                    { id: '1', text: 'Student' }
                ]
            });
            searchSelect.trigger('select2:select');
        } else if (e.params.data.id === 'GROUP') {
            searchInput.hide();
            searchSelect.show();
            searchSelect.select2({
                theme: 'bootstrap',
                width: '65%',
                maximumInputLength: 20,
                ajax: {
                    delay: 250,
                    url: '/ajax/get_groups',
                    data: function (params) {
                        return {
                            term: params.term || '',
                            page: params.page || 1,
                            pageSize: 10,
                            requestType: 'jquery_select'
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
            table.search(searchInput.val()).draw(); // reset search results
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