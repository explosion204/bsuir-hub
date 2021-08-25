$(document).ready(function () {
    setActiveNavItem(4);
    invalidateCache();
    configureValidation([['groupForm', 'saveButton']]);

    let mainBlock = $('main');
    let departmentId = mainBlock.data('department-id');
    let headmanId = mainBlock.data('headman-id');
    let curatorId = mainBlock.data('curator-id');
    let localeCode = mainBlock.data('locale-code');

    $('#groupForm').submit(function () {
        sessionStorage.setItem('departmentId', this.departmentId.value);
        sessionStorage.setItem('curatorId', this.curatorId.value);
        $('#saveButton').attr('disabled', true);
    });

    if (!(!!departmentId)) {
        departmentId = sessionStorage.getItem('departmentId');
    }

    if (!(!!curatorId)) {
        curatorId = sessionStorage.getItem('curatorId');
    }

    sessionStorage.removeItem('departmentId');
    sessionStorage.removeItem('curatorId');

    initializeDepartmentSelect($('#departmentSelect'), '100%', departmentId);
    initializeStudentSelect($('#headmanSelect'), '100%', headmanId);
    initializeTeacherSelect($('#curatorSelect'), '100%', curatorId);

    let assignmentsTable =  $('#assignmentsTable').DataTable({
        dom: '<"toolbar">rt',
        language: {
            url: `/static/lib/datatables/locale/${localeCode}.json`
        },
        scrollX: false,
        scrollY: '50vh',
        scrollResize: true,
        ordering: false,
        scroller: {
            loadingIndicator: true,
            displayBuffer: 10 // 100 before next draw
        },
        processing: true,
        serverSide: true,
        drawCallback: function () { onAssignmentsLoaded(assignmentsTable); },
        ajax: {
            url: '/ajax/get_assignments',
            data: function (d) {
                d.requestType = 'jquery_datatable';
                d.filterCriteria = 'group';
            }
        },
        search: {
            search: $('#groupId').val()
        },
        columns: [
            { data: null }
        ],
        initComplete: function () {
            initToolbar(assignmentsTable);
        }
    });
})

function onAssignmentsLoaded(assignmentsTable) {
    assignmentsTable.rows().data().each(function (value, index) {
        console.log(value);

        let selectionBody = $(`<div class="input-group"><input hidden value="${value.entityId}"></div>`);
        let subjectSelect = $(`<select></select>`);
        let teacherSelect = $(`<select></select>`);
        let saveButton = $(`<button class="btn btn-secondary create-button">${$('#admin_save').text()}</button>`);
        let deleteButton = $(`<button class="btn btn-secondary create-button">${$('#admin_delete').text()}</button>`);
        selectionBody.append(subjectSelect, teacherSelect, saveButton, deleteButton);
        let cell = assignmentsTable.cell(index, 0).node();
        $(cell).html(selectionBody);

        initializeSubjectSelect(subjectSelect, '45%', value.subjectId);
        initializeTeacherSelect(teacherSelect, '35%', value.teacherId);

        saveButton.click(function () {
            let newSubjectId = subjectSelect.val();
            let newTeacherId = teacherSelect.val();
            onAssignmentUpdate.call(this, value.entityId, value.groupId, newSubjectId, newTeacherId);
        });

        deleteButton.click(function () {
            onAssignmentDelete.call(this, assignmentsTable, value.entityId);
        });
    });
}

function initToolbar(assignmentsTable) {
    // initialization
    let selectionBody = $(`<div class="input-group"></div>`);
    let subjectSelect = $(`<select></select>`);
    let teacherSelect = $(`<select></select>`);
    let saveButton = $(`<button class="btn btn-secondary create-button" disabled>${$('#admin_create').text()}</button>`);
    selectionBody.append(subjectSelect, teacherSelect, saveButton);
    $('div.toolbar').append(selectionBody);
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
        onAssignmentCreate.call(this, assignmentsTable, subjectSelect, teacherSelect);
    });
}

function onAssignmentCreate(assignmentsTable, subjectSelect, teacherSelect) {
    let saveButton = this;
    $(saveButton).attr('disabled', true);

    $.ajax({
        method: 'POST',
        url: '/admin/ajax/create_assignment',
        data: {
            groupId: $('#groupId').val(),
            teacherId: teacherSelect.val(),
            subjectId: subjectSelect.val()
        },
        success: function (response) {
            let data = JSON.parse(response);

            if (data && data.status) {
                $('#createToast').toast('show');
                assignmentsTable.draw();
            }

            $(saveButton).attr('disabled', false);
        }
    });
}

function onAssignmentUpdate(assignmentId, groupId, subjectId, teacherId) {
    $(this).attr('disabled', true);
    $.ajax({
       method: 'POST',
       url: '/admin/ajax/update_assignment',
       data: {
           id: assignmentId,
           groupId: groupId,
           subjectId: subjectId,
           teacherId: teacherId
       },
        success: function (response) {
            let data = JSON.parse(response);

            if (data && data.status) {
                $('#updateToast').toast('show');
            }
        }
    });
    $(this).attr('disabled', false);
}

function onAssignmentDelete(assignmentsTable, assignmentId) {
    $(this).attr('disabled', true);
    $.ajax({
        method: 'POST',
        url: '/admin/ajax/delete_assignment',
        data: {
            id: assignmentId
        },
        success: function (response) {
            let data = JSON.parse(response);

            if (data && data.status) {
                assignmentsTable.draw();
                $('#deleteToast').toast('show');
            }
        }
    });
}

function initializeSubjectSelect(select, width, initialId) {
    select.select2({
        language: $('main').data('locale-code'),
        theme: 'bootstrap',
        width: width,
        placeholder: $('#group_choose_subject').text(),
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

    if (!isNaN(Number.parseInt(initialId))) {
        FETCH_QUEUE.append(fetchSubject, [initialId], function (entity) {
            let option = new Option(entity.name, entity.entityId);
            select.append(option).trigger('change');
        });
    }
}

function initializeTeacherSelect(select, width, initialId) {
    select.select2({
        language: $('main').data('locale-code'),
        theme: 'bootstrap',
        width: width,
        placeholder: $('#group_choose_teacher').text(),
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

    if (!isNaN(Number.parseInt(initialId))) {
        FETCH_QUEUE.append(fetchUser, [initialId], function (entity) {
            let name = entity.lastName + ' ' + entity.firstName + ' ' + entity.patronymic;
            let option = new Option(name, entity.entityId);
            select.append(option).trigger('change');
        });
    }
}

function initializeStudentSelect(select, width, initialId) {
    select.select2({
        language: $('main').data('locale-code'),
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

    if (!isNaN(Number.parseInt(initialId))) {
        fetchUser(initialId, function (entity) {
            let name = entity.lastName + ' ' + entity.firstName + ' ' + entity.patronymic;
            let option = new Option(name, entity.entityId);
            select.append(option).trigger('change');
        });
    }
}

function initializeDepartmentSelect(select, width, initialId) {
    select.select2({
        language: $('main').data('locale-code'),
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

    if (!isNaN(Number.parseInt(initialId))) {
        fetchDepartment(initialId, function (entity) {
            let option = new Option(entity.name, entity.entityId);
            select.append(option).trigger('change');
        });
    }
}