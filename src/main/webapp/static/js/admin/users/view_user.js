$(document).ready(function () {
    setActiveNavItem(1);
    configureValidation([['userForm', 'saveButton']]);

    let form = $('#userForm');
    form.submit(function () {
        $('#saveButton').attr('disabled', true);
        sessionStorage.setItem('login', this.login.value);
        sessionStorage.setItem('email', this.email.value);
        sessionStorage.setItem('role', this.role.value);
        sessionStorage.setItem('firstName', this.firstName.value);
        sessionStorage.setItem('patronymic', this.patronymic.value);
        sessionStorage.setItem('lastName', this.lastName.value);
        sessionStorage.setItem('confirmed', this.confirmed.checked);
    });

    let notUniqueLogin = $('body').data('not-unique-login');
    let notUniqueEmail = $('body').data('not-unique-email');

    if (notUniqueLogin === true || notUniqueEmail === true) {
        let role = sessionStorage.getItem('role');
        let firstName = sessionStorage.getItem('firstName');
        let patronymic = sessionStorage.getItem('patronymic');
        let lastName = sessionStorage.getItem('lastName');
        let confirmed = sessionStorage.getItem('confirmed');
        form.find('input[name="role"]').val(role);
        form.find('input[name="firstName"]').val(firstName);
        form.find('input[name="patronymic"]').val(patronymic);
        form.find('input[name="lastName"]').val(lastName);
        form.find('input[name="confirmed"]').prop('checked', confirmed);

        if (!(!!notUniqueLogin)) {
            let login = sessionStorage.getItem('login');
            form.find('input[name="login"]').val(login);
        }

        if (!(!!notUniqueEmail)) {
            let email = sessionStorage.getItem('email');
            form.find('input[name="email"]').val(email);
        }
    }

    sessionStorage.removeItem('login');
    sessionStorage.removeItem('email');
    sessionStorage.removeItem('role');
    sessionStorage.removeItem('firstName');
    sessionStorage.removeItem('patronymic');
    sessionStorage.removeItem('lastName');
    sessionStorage.removeItem('confirmed');

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
                language: $('main').data('locale-code'),
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

    let groupId = Number.parseInt($('main').data('group-id'));
    if (!isNaN(groupId)) {
        fetchGroup(groupId, function (entity) {
            let option = new Option(entity.name, entity.entityId);
            groupSelect.append(option).trigger('change');
        });
    }

    $('#clearGradesButton').click(function () {
        let btn = $(this);
        btn.attr('disabled', true);

        if (confirm(`${$('#user_confirm_clear').text()}`)) {
            $.ajax({
                method: 'POST',
                url: '/admin/ajax/clear_student_grades',
                data: {
                    studentId: $('#targetId').val()
                },
                success: function () {
                    alert(`${$('#user_clear_success').text()}`);
                    btn.attr('disabled', false);
                }
            });
        }
    });
})