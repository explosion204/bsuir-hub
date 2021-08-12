$(document).ready(function () {
    invalidateCache();
    updateGradeStyle();

    let bodyBlock = $('body');
    let studentId = bodyBlock.data('student-id');
    let subjectId = bodyBlock.data('subject-id');
    let userId = bodyBlock.data('user-id');

    let gradesTable = $('#gradesTable').DataTable({
        dom: '<"toolbar">rt',
        scrollX: false,
        scrollY: '70vh',
        scroller: {
            loadingIndicator: true
        },
        ordering: false,
        processing: true,
        serverSide: true,
        drawCallback: function () { onDataLoaded(gradesTable, userId, studentId, subjectId); },
        ajax: {
            url: '/ajax/get_grades',
            data: function (d) {
                d.requestType = 'jquery_datatable';
                d.studentId = studentId;
                d.subjectId = subjectId;
            }
        },
        columns: [
            {
                data: null,
                className: 'text-center align-middle',
                width: '5%',
                render: function () {
                    return '<i class="btn far fa-comments fa-lg"></i>';
                }
            },
            { data: null, width: '35%' },
            { data: null, width: '20%' },
            { data: null, width: '20%' },
            { data: null, width: '20%' }
        ]
    });

    $('div.toolbar').html(`
        <div class="input-group mb-1">
            <button id="createButton" type="button" class="btn btn-secondary">New grade</button>
            <select id="gradeSelect" class="form-select">
                <option value="1">1</option>
                <option value="2">2</option>
                <option value="3">3</option>
                <option value="4">4</option>
                <option value="5">5</option>
                <option value="6">6</option>
                <option value="7">7</option>
                <option value="8">8</option>
                <option value="9">9</option>
                <option value="10">10</option>
            </select>
        </div>
    `);

    $('#createButton').click(function () {
        let gradeValue = $('#gradeSelect').val();
        onGradeCreate(gradesTable, studentId, subjectId, gradeValue);
    });

    $('#gradesTable tbody').on('click', 'td i', function () {
        let gradeId = $(this).closest('tr').data('grade-id');
        let teacherId = $(this).closest('tr').data('teacher-id');
        $('body').data('active-grade-id', gradeId);
        let commentsTable = initializeCommentsTable(gradeId, teacherId, subjectId, studentId);
        commentsTable.search('').draw();
        $('#commentsModal').modal('show');

        let modifiedUrl = window.location.href + `&gradeId=${gradeId}`;
        window.history.pushState('', '', modifiedUrl);
    });

    $('#commentsModal').on('hide.bs.modal', function (e) {
        let url = new URL(window.location.href);
        let params = new URLSearchParams(url.search);
        params.delete('gradeId');
        let modifiedUrl = window.location.origin + window.location.pathname + '?' + params.toString();
        window.history.pushState('', '', modifiedUrl);
    });
})

function onDataLoaded(table, userId, studentId, subjectId) {
    let grades = [];
    table.rows().data().each(function (value, index) {
        grades.push(value);

        FETCH_QUEUE.append(fetchUser, [value.teacherId], function (entity) {
            // teacher name
            let teacherName = entity.lastName + ' ' + entity.firstName + ' ' + entity.patronymic;
            let teacherCell = table.cell(index, 1).node();
            $(teacherCell).text(teacherName);
        });

        // date
        let date = value.date;
        let dateCell = table.cell(index, 2).node();
        $(dateCell).text(`${date.day}.${date.month}.${date.year}`);

        // grade value
        let gradeCell = table.cell(index, 3).node();
        let gradeSelect = $(`
                <select id="gradeSelect" class="form-select">
                    <option value="1">1</option>
                    <option value="2">2</option>
                    <option value="3">3</option>
                    <option value="4">4</option>
                    <option value="5">5</option>
                    <option value="6">6</option>
                    <option value="7">7</option>
                    <option value="8">8</option>
                    <option value="9">9</option>
                    <option value="10">10</option>
                </select>
        `);

        gradeSelect.val(value.value);
        $(gradeCell).html(gradeSelect);

        // action buttons
        let actionCell = table.cell(index, 4).node();
        let updateButton = $(`<button class="btn btn-secondary me-2" data-grade-id="${value.entityId}">Update</button>`);
        let deleteButton = $(`<button class="btn btn-secondary me-2" data-grade-id="${value.entityId}">Delete</button>`)

        updateButton.click(function () {
            let gradeValue = gradeSelect.val();
            onGradeUpdate.call(this, studentId, subjectId, gradeValue);
        });

        deleteButton.click(function () {
            onGradeDelete.call(this, table, studentId, subjectId);
        });

        $(actionCell).empty();
        $(actionCell).append(updateButton, deleteButton);

        let row = table.row(index).node();
        $(row).data('grade-id', value.entityId);
        $(row).data('teacher-id', value.teacherId);
    });

    // optional gradeId url parameter
    useGradeIdParam(grades, subjectId, studentId);
}

function onGradeCreate(gradesTable, studentId, subjectId, gradeValue) {
    $.ajax({
        method: 'POST',
        url: '/grades/ajax/create_grade',
        data: {
            studentId: studentId,
            subjectId: subjectId,
            gradeValue: gradeValue
        },
        success: function (response) {
            let data = JSON.parse(response);

            if (data && data.status) {
                gradesTable.draw();
                updateAverageGrade(studentId, subjectId);
                // TODO: success notifications
            }
        }
    })
}

function onGradeUpdate(studentId, subjectId, gradeValue) {
    $.ajax({
        method: 'POST',
        url: '/grades/ajax/update_grade',
        data: {
            id: $(this).data('grade-id'),
            studentId: studentId,
            subjectId: subjectId,
            gradeValue: gradeValue
        },
        success: function (response) {
            let data = JSON.parse(response);

            if (data && data.status) {
                updateAverageGrade(studentId, subjectId);
            }
        }
    })
}

function onGradeDelete(gradeTable, studentId, subjectId) {
    $.ajax({
        method: 'POST',
        url: '/grades/ajax/delete_grade',
        data: {
            id: $(this).data('grade-id'),
            studentId: studentId,
            subjectId: subjectId,
        },
        success: function (response) {
            let data = JSON.parse(response);

            if (data && data.status) {
                gradeTable.draw();
                updateAverageGrade(studentId, subjectId);
            }
        }
    })
}

function updateAverageGrade(studentId, subjectId) {
    fetchAverageGrade(studentId, subjectId, function (data) {
        let avg = data.avgValue;
        $('#avgGrade').text(avg);
        updateGradeStyle();
    });
}