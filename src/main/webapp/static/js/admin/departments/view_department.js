$(document).ready(function () {
    setActiveNavItem(3);
    configureValidation([['departmentForm', 'saveButton']]);

    $('#departmentForm').submit(function () {
        $('#saveButton').attr('disabled', true);
    });

    $('#facultySelect').select2({
        theme: 'bootstrap',
        width: '100%',
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
})