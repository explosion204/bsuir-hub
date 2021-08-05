$(document).ready(function () {
    setActiveNavItem(1);
    configureValidation([['userForm', 'saveButton']]);

    let form = $('#userForm');

    form.submit(function () {
        $('#saveButton').attr('disabled', true);
    });

    form.on('input', function () {
        let passwordInput = $('#passwordInput')[0];
        let confirmPasswordInput = $('#confirmPasswordInput')[0];
        confirmPasswordInput.setCustomValidity(
            passwordInput.value !== confirmPasswordInput.value ? "passwords do not match" : ""
        );
    })

    $('#uploadPicture').click(function () {
       $('#filePicker').trigger('click');
    });


    $('#filePicker').change(function () {
        uploadProfileImage.call(this, $('#targetId').val());
    });


    let roleSelect = $('#roleSelect');
    let groupSelect = $('#groupSelect');
    let groupSelectBlock = $('#groupSelectBlock');

    roleSelect.on('change', function () {
        if ($(this).val() === '1') { // student
            groupSelectBlock.show();
            groupSelect.select2({
                theme: 'bootstrap',
                width: '100%',
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
            groupSelectBlock.hide();
            if (groupSelect.data('select2')) {
                groupSelect.html(''); // clear
                groupSelect.select2('destroy');
            }
        }
    });

    roleSelect.trigger('change');

    fetchGroup($('main').data('group-id'), function (data) {
        let option = new Option(data.entity.name, data.entity.entityId);
        groupSelect.append(option).trigger('change');
    });
})