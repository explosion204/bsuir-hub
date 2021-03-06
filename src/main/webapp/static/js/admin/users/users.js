$(document).ready(function() {
    setActiveNavItem(1);
    let localeCode = $('main').data('locale-code');
    let table = $('#dataTable').DataTable({
        language: {
            url: `/static/lib/datatables/locale/${localeCode}.json`
        },
        dom: '<"toolbar">rtip',
        scrollY: '70vh',
        scrollX: false,
        scrollResize: true,
        processing: true,
        serverSide: true,
        ordering: false,
        drawCallback: function () {
            table.rows().data().each(function (value, index) {
                console.log(value);
            })
        },
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
            {
                data: null,
                render: function (data, type, row, meta) {
                    switch (row.role) {
                        case 'ADMIN':
                            return $('#role_admin').text();
                        case 'TEACHER':
                            return $('#role_teacher').text();
                        case 'STUDENT':
                            return $('#role_student').text();
                    }
                }
            },
            {
                data: null,
                render: function (data, type, row, meta) {
                    switch (row.status) {
                        case 'CONFIRMED':
                            return $('#user_confirmed').text();
                        case 'NOT_CONFIRMED':
                            return $('#user_not_confirmed').text()
                    }
                }
            },
            { data: 'firstName' },
            { data: 'patronymic' },
            { data: 'lastName' },
            {
                data: null,
                render: function (data, type, row, meta) {
                    let issuerId = Number.parseInt($('main').data('issuer-id'));

                    if (issuerId === row.entityId || row.userRole === 'ADMIN') {
                        return '<a class="btn btn-secondary me-2" href="/admin/users/edit?id=' + row.entityId + '">' + $('#admin_edit').text() + '</a>' +
                            '<form style="display: inline" method="post" action="/admin/users/delete?id=' + row.entityId + '">' +
                            '   <input class="btn btn-secondary" type="submit" value="' + $('#admin_delete').text() + '" disabled>' +
                            '</span>'
                    } else {
                        return '<a class="btn btn-secondary me-2" href="/admin/users/edit?id=' + row.entityId + '">' + $('#admin_edit').text() + '</a>' +
                            '<form style="display: inline" method="post" action="/admin/users/delete?id=' + row.entityId + '">' +
                            '   <input class="btn btn-secondary" type="submit" value="' + $('#admin_delete').text() + '" onclick="return confirmDelete();">' +
                            '</form>'
                    }
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
                    <optgroup label="${$('#admin_search_criteria').text()}">
                        <option value="LOGIN">${$('#user_login').text()}</option>
                        <option value="EMAIL">${$('#user_email').text()}</option>
                        <option value="LAST_NAME">${$('#user_last_name').text()}</option>
                        <option value="ROLE">${$('#user_role').text()}</option>
                        <option value="GROUP">${$('#user_group').text()}</option>
                    </optgroup>
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
        language: $('main').data('locale-code'),
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
                language: $('main').data('locale-code'),
                theme: 'bootstrap',
                width: '65%',
                data: [
                    { id: '3', text: `${$('#role_admin').text()}` },
                    { id: '2', text: `${$('#role_teacher').text()}` },
                    { id: '1', text: `${$('#role_student').text()}` }
                ]
            });
            searchSelect.trigger('select2:select');
        } else if (e.params.data.id === 'GROUP') {
            searchInput.hide();
            searchSelect.show();
            searchSelect.select2({
                language: $('main').data('locale-code'),
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
}