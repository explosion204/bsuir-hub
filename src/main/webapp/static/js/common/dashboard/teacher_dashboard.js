$(document).ready(function () {
    invalidateCache();

    let bodyBlock = $('body');
    let teacherId = bodyBlock.data('teacher-id');
    let subjectsTable = $('#subjectsTable').DataTable({
        dom: 'rt',
        scrollX: false,
        scrollY: '70vh',
        scrollResize: true,
        ordering: false,
        scroller: {
            loadingIndicator: true,
            displayBuffer: 1
        },
        processing: true,
        serverSide: true,
        drawCallback: function () { onAssignmentsLoaded(subjectsTable); },
        ajax: {
            url: '/ajax/get_assignments',
            data: function (d) {
                d.requestType = 'jquery_datatable';
                d.filterCriteria = 'teacher';
            }
        },
        search: {
            search: teacherId
        },
        columns: [
            { data: null }
        ]
    });

    let groupsTable = $('#groupsTable').DataTable({
        dom: 'rt',
        scrollX: false,
        scrollY: '70vh',
        scrollResize: true,
        ordering: false
    });

    let studentsTable = $('#studentsTable').DataTable({
        dom: 'rt',
        scrollX: false,
        scrollY: '70vh',
        scrollResize: true,
        ordering: false,
        scroller: {
            loadingIndicator: true,
            displayBuffer: 1
        },
        processing: true,
        serverSide: true,
        deferRender: true,
        deferLoading: true,
        ajax: {
            url: '/ajax/get_users',
            data: function (d) {
                d.requestType = 'jquery_datatable';
                d.filterCriteria = 'group';
            }
        },
        columns: [
            {
                data: null,
                render: function (data, type, row, meta) {
                    return `<div class="lead">${row.lastName} ${row.patronymic} ${row.firstName}</div>`;
                }
            }
        ]
    });

    $('#subjectsTable tbody').on('click', 'tr', function () {
        onSubjectsTableSelect.call(this, subjectsTable, groupsTable, studentsTable);
    });
    $('#groupsTable tbody').on('click', 'tr', function () {
        onGroupsTableSelect.call(this, groupsTable, studentsTable);
    });
    $('#studentsTable tbody').on('click', 'tr', function () {
        onStudentsTableSelect.call(this, subjectsTable, studentsTable);
    });
});

function onAssignmentsLoaded(subjectsTable) {
    let assignments = [];
    subjectsTable.rows().data().each(val => assignments.push(val));

    // find unique pair of subject id and group id
    let filteredAssignments = assignments.filter(function (item) {
        let key = item.groupId + '|' + item.subjectId;

        if (!this[key]) {
            this[key] = true;
            return true;
        }

        return false;
    }, Object.create(null));

    // save filtered assignments to session storage
    sessionStorage.setItem('assignments', JSON.stringify(filteredAssignments));

    // find unique subject ids
    let subjectIds = $.map(assignments, item => item.subjectId);
    let distinctSubjectIds = subjectIds.filter(function (item, index, array) {
       return index === array.indexOf(item);
    });

    // fetch subject names
    subjectsTable.rows().data().each(function (value, index) {
        let subjectId = distinctSubjectIds[index];
        if (subjectId) {
            fetchSubject(subjectId, function (entity) {
                let name = entity.name;
                let shortName = entity.shortName;
                let cell = subjectsTable.cell(index, 0).node();
                $(cell).html(`<div class="lead">${name}</div><div>${shortName}</div>`);

                // associate subject id with its row
                let row = subjectsTable.row(index).node();
                $(row).data('subject-id', subjectId);
            });
        } else { // remove redundant rows
            $(subjectsTable.row(index).node()).remove();
        }
    });
}

function onSubjectsTableSelect(subjectsTable, groupsTable, studentsTable) {
    if (!$(this).hasClass('selected')) {
        // retrieve associated subject id
        let subjectId = $(this).data('subject-id');
        // retrieve all assignments
        let assignments = JSON.parse(sessionStorage.getItem('assignments'));
        // find all assignments with this subject id
        let concreteAssignments = assignments.filter(a => a.subjectId === subjectId);

        // clear groups table
        groupsTable.clear();
        // clear students table
        studentsTable.search('').draw(); // the only possible workaround to clear ajax-powered table

        // fetch groups by their id
        $.each(concreteAssignments, function (index, value) {
            let groupId = value.groupId;

            // fetch group, faculty, departments names
            FETCH_QUEUE.append(fetchGroup, [groupId], function (group) {
                FETCH_QUEUE.append(fetchDepartment, [group.departmentId], function (department) {
                    FETCH_QUEUE.append(fetchFaculty, [department.facultyId], function (faculty) {
                        let row = groupsTable.row.add(
                            $(`
                                <tr><td>
                                    <div class="lead">${group.name}</div>
                                    <div class="lead">Факультет <span class="h6"> ${faculty.name}</span></div>
                                    <div class="lead">Кафедра <span class="h6"> ${department.name}</span></div>
                                </td></tr>
                            `)[0]
                        ).node();
                        // associate group id with its row
                        $(row).data('group-id', groupId);
                        groupsTable.draw();
                    });
                });
            });
        });

        subjectsTable.$('tr.selected').removeClass('selected');
        $(this).addClass('selected');
    }
}

function onGroupsTableSelect(groupsTable, studentsTable) {
    if (!$(this).hasClass('selected')) {
        // retrieve associated group id
        let groupId = $(this).data('group-id');

        // fetch users
        studentsTable.search(groupId).draw();
        groupsTable.$('tr.selected').removeClass('selected');
        $(this).addClass('selected');
    }
}

function onStudentsTableSelect(subjectsTable, studentsTable) {
    let student = studentsTable.row(this).data();
    let subjectId = $('#subjectsTable .selected').data('subject-id');

    if (student && subjectId) {
        let studentId = student.entityId;
        window.location.href = `/grades?subjectId=${subjectId}&studentId=${studentId}`;
    }
}