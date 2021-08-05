$(document).ready(function () {
    setActiveNavItem(4);
    configureValidation([['groupForm', 'saveButton']]);

    $('#groupForm').submit(function () {
        $('#saveButton').attr('disabled', true);
    });

    let mainBlock = $('main');
    let departmentId = mainBlock.data('department-id');
    let headmanId = mainBlock.data('headman-id');
    let curatorId = mainBlock.data('curator-id');

    initializeDepartmentSelect($('#departmentSelect'), '100%', departmentId);
    initializeStudentSelect($('#headmanSelect'), '100%', headmanId);
    initializeTeacherSelect($('#curatorSelect'), '100%', curatorId);

    addEmptyAssignment();
})

function addExistingAssignment(id, subjectId, teacherId) {
    // initialization
    let selectionBody = $(`<div class="input-group mt-2 mb-2 pb-2"><input hidden value="${id}"></div>`);
    let subjectSelect = $(`<select></select>`);
    let teacherSelect = $(`<select></select>`);
    let saveButton = $('<button class="btn btn-secondary create-button">Save</button>');
    let deleteButton = $('<button class="btn btn-secondary create-button">Delete</button>');
    selectionBody.append(subjectSelect, teacherSelect, saveButton, deleteButton);
    $('.admin-main-area').append(selectionBody);

    initializeSubjectSelect(subjectSelect, '45%', subjectId);
    initializeTeacherSelect(teacherSelect, '35%', teacherId);

    saveButton.click(function () {
        let groupId = $('#groupId').val();
        let newSubjectId = subjectSelect.val();
        let newTeacherId = teacherSelect.val();
        onAssignmentUpdate.call(this, id, groupId, newSubjectId, newTeacherId);
    });

    deleteButton.click(function () {
        onAssignmentDelete.call(this, id);
        selectionBody.remove();
    });
}

function addEmptyAssignment() {
    // initialization
    let selectionBody = $(`<div class="input-group mt-2 mb-2 pb-2"><input hidden></div></div>`);
    let subjectSelect = $(`<select></select>`);
    let teacherSelect = $(`<select></select>`);
    let saveButton = $('<button class="btn btn-secondary create-button" disabled>Create</button>');
    selectionBody.append(subjectSelect, teacherSelect, saveButton);
    $('.admin-main-area').append(selectionBody);

    initializeSubjectSelect(subjectSelect, '45%');
    initializeTeacherSelect(teacherSelect, '45%');

    // validation
    subjectSelect.on('select2:select', function () {
        saveButton.attr('disabled', teacherSelect.val() === null);
    });

    teacherSelect.on('select2:select', function () {
        saveButton.attr('disabled', subjectSelect.val() === null);
    });

    saveButton.click(function () {
        onAssignmentCreate.call(this, selectionBody, subjectSelect, teacherSelect);
    });
}

function onAssignmentCreate(selectionBody, subjectSelect, teacherSelect) {
    let saveButton = this;
    $(saveButton).attr('disabled', true);

    $.ajax({
        method: 'POST',
        url: '/admin/ajax/create_study_assignment',
        data: {
            groupId: $('#groupId').val(),
            teacherId: teacherSelect.val(),
            subjectId: subjectSelect.val()
        },
        success: function (response) {
            let data = JSON.parse(response);

            if (data.status) {
                let deleteButton = $('<button class="btn btn-secondary create-button">Delete</button>');
                initializeTeacherSelect(teacherSelect, '35%');
                $(selectionBody).append(deleteButton);
                $(selectionBody).find('input').attr('val', data.assignmentId);

                $(saveButton).unbind();
                $(saveButton).text('Save');
                $(saveButton).click(function () {
                    let groupId = $('#groupId').val();
                    let newSubjectId = subjectSelect.val();
                    let newTeacherId = teacherSelect.val();
                    onAssignmentUpdate.call(this, data.assignmentId, groupId, newSubjectId, newTeacherId);
                });
                deleteButton.click(function () {
                    onAssignmentDelete.call(this, data.assignmentId);
                    selectionBody.remove();
                });

                addEmptyAssignment();
            }

            $(saveButton).attr('disabled', false);
        }
    });
}

function onAssignmentUpdate(assignmentId, groupId, subjectId, teacherId) {
    $(this).attr('disabled', true);
    $.ajax({
       method: 'POST',
       url: '/admin/ajax/update_study_assignment',
       data: {
           id: assignmentId,
           groupId: groupId,
           subjectId: subjectId,
           teacherId: teacherId
       },
        success: function () {
            $.confirm({
                title: 'Success',
                content: 'Assignment successfully updated',
                buttons: {
                    'OK': function () {

                    },
                }
            });
        }
    });
    $(this).attr('disabled', false);
}

function onAssignmentDelete(assignmentId) {
    $(this).attr('disabled', true);
    $.ajax({
        method: 'POST',
        url: '/admin/ajax/delete_study_assignment',
        data: {
            id: assignmentId
        },
        success: function () {
            $.confirm({
                title: 'Success',
                content: 'Assignment successfully deleted',
                buttons: {
                    'OK': function () {

                    },
                }
            });
        }
    });
}

function initializeSubjectSelect(select, width, initialId) {
    select.select2({
        theme: 'bootstrap',
        width: width,
        placeholder: 'Choose subject',
        ajax: {
            delay: 250,
            url: '/ajax/get_subjects',
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

    if (initialId) {
        fetchSubject(initialId, function (data) {
            let option = new Option(data.entity.name, data.entity.entityId);
            select.append(option).trigger('change');
        });
    }
}

function initializeTeacherSelect(select, width, initialId) {
    select.select2({
        theme: 'bootstrap',
        width: width,
        placeholder: 'Choose teacher',
        ajax: {
            delay: 250,
            url: '/ajax/get_users',
            data: function (params) {
                return {
                    term: params.term || '',
                    page: params.page || 1,
                    pageSize: 10,
                    requestType: 'jquery_select',
                    fetchStudents: false
                }
            },
            processResults: function (data, params) {
                data = JSON.parse(data);
                let mappedData = $.map(data.results, function (item) {
                    item.id = item.entityId;
                    item.text = item.lastName + ' ' + item.firstName + ' ' + item.patronymic;
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

    if (initialId) {
        fetchUser(initialId, function (data) {
            let name = data.entity.lastName + ' ' + data.entity.firstName + ' ' + data.entity.patronymic;
            let option = new Option(name, data.entity.entityId);
            select.append(option).trigger('change');
        });
    }
}

function initializeStudentSelect(select, width, initialId) {
    select.select2({
        theme: 'bootstrap',
        width: width,
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
                    groupId: $('#groupId').val()
                }
            },
            processResults: function (data, params) {
                data = JSON.parse(data);
                let mappedData = $.map(data.results, function (item) {
                    item.id = item.entityId;
                    item.text = item.lastName + ' ' + item.firstName + ' ' + item.patronymic;
                    console.log(item.patronymic);
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

    if (initialId) {
        fetchUser(initialId, function (data) {
            let name = data.entity.lastName + ' ' + data.entity.firstName + ' ' + data.entity.patronymic;
            let option = new Option(name, data.entity.entityId);
            select.append(option).trigger('change');
        });
    }
}

function initializeDepartmentSelect(select, width, initialId) {
    select.select2({
        theme: 'bootstrap',
        width: width,
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

    if (initialId) {
        fetchDepartment(initialId, function (data) {
            let option = new Option(data.entity.name, data.entity.entityId);
            select.append(option).trigger('change');
        });
    }
}