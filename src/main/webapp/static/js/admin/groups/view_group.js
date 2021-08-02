$(document).ready(function () {
    setActiveNavItem(4);
    configureValidation([['groupForm', 'saveButton']]);

    $('#groupForm').submit(function () {
        $('#saveButton').attr('disabled', true);
    });

    let groupId = $('#groupId').val();

    $('#departmentSelect').select2({
        theme: 'bootstrap',
        width: '100%',
        maximumInputLength: 50,
        ajax: {
            delay: 250,
            url: '/ajax/get_departments',
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

    $('#headmanSelect').select2({
        theme: 'bootstrap',
        width: '100%',
        maximumInputLength: 50,
        ajax: {
            delay: 250,
            url: '/ajax/get_users',
            data: function (params) {
                return {
                    term: params.term || '',
                    page: params.page || 1,
                    pageSize: 10,
                    requestType: 'jquery_select',
                    fetchStudents: true,
                    groupId: groupId
                }
            },
            processResults: function (data, params) {
                data = JSON.parse(data);
                let mappedData = $.map(data.results, function (item) {
                    item.id = item.entityId;
                    item.text = item.lastName;
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

    $('#curatorSelect').select2({
        theme: 'bootstrap',
        width: '100%',
        ajax: {
            delay: 250,
            url: '/ajax/get_users',
            maximumInputLength: 50,
            data: function (params) {
                return {
                    term: params.term || '',
                    page: params.page || 1,
                    pageSize: 10,
                    requestType: 'jquery_select',
                    fetchStudents: false,
                    groupId: groupId
                }
            },
            processResults: function (data, params) {
                data = JSON.parse(data);
                let mappedData = $.map(data.results, function (item) {
                    item.id = item.entityId;
                    item.text = item.lastName;
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
})