$(document).ready(function () {
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
            url: '/ajax/get_study_assignments',
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

    bodyBlock.find('.dataTables_scrollBody').addClass('scrollbar');
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
            fetchSubject(subjectId, function (data) {
                let name = data.entity.name;
                let shortName = data.entity.shortName;
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

        // fetch groups by their id
        $.each(concreteAssignments, function (index, value) {
            // clear groups and students tables
            groupsTable.clear();
            studentsTable.search('').draw(); // the only possible workaround to clear ajax-powered table

            let groupId = value.groupId;
            fetchGroup(groupId, function (data) {
                let name = data.entity.name;

                // associate group id with its row
                let row = groupsTable.row.add(
                    $(`<tr><td><div class="lead">${name}</div></td></tr>`)[0]
                ).node();
                groupsTable.draw();
                $(row).data('group-id', groupId);
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
    let studentId = studentsTable.row(this).data().entityId;
    let subjectId = $('#subjectsTable .selected').data('subject-id');

    console.log('Student id: ' + studentId);
    console.log('Subject id: ' + subjectId);
}