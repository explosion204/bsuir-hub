$(document).ready(function () {
    // TODO: hide table init and redraw blinking
    // TODO: ajax optimization

    updateGradeStyle();

    let body = $('body');
    let studentId = body.data('student-id');
    let subjectId = body.data('subject-id');
    let teacherId = body.data('teacher-id');

    let gradesTable = $('#gradesTable').DataTable({
        dom: '<"toolbar">rt',
        scrollX: false,
        scrollY: '50vh',
        scrollResize: true,
        scroller: {
            loadingIndicator: true,
            displayBuffer: 2
        },
        ordering: false,
        processing: true,
        serverSide: true,
        drawCallback: function () { onDataLoaded(gradesTable, teacherId, studentId, subjectId); },
        ajax: {
            url: '/ajax/get_grades',
            data: function (d) {
                d.requestType = 'jquery_datatable';
                d.studentId = studentId;
                d.subjectId = subjectId;
            }
        },
        columns: [
            { data: null },
            { data: null, },
            { data: null },
            { data: null }
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
        onGradeCreate(gradesTable, teacherId, studentId, subjectId, gradeValue);
    });
})

function onDataLoaded(table, teacherId, studentId, subjectId) {
    table.rows().data().each(function (value, index) {
        fetchUser(value.teacherId, function (data) {
            // teacher name
            let teacherName = data.entity.lastName + ' ' + data.entity.firstName + ' ' + data.entity.patronymic;
            let teacherCell = table.cell(index, 0).node();
            $(teacherCell).text(teacherName);

            // date
            let date = value.date;
            let dateCell = table.cell(index, 1).node();
            $(dateCell).text(`${date.day}.${date.month}.${date.year}`);

            // grade value
            let gradeCell = table.cell(index, 2).node();
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
            let actionCell = table.cell(index, 3).node();
            let updateButton = $(`<button class="btn btn-secondary me-2" data-grade-id="${value.entityId}">Update</button>`);
            let deleteButton = $(`<button class="btn btn-secondary me-2" data-grade-id="${value.entityId}">Delete</button>`)

            updateButton.click(function () {
                let gradeValue = gradeSelect.val();
                onGradeUpdate.call(this, teacherId, studentId, subjectId, gradeValue);
            });

            deleteButton.click(function () {
                onGradeDelete.call(this, table, studentId, subjectId);
            });

            $(actionCell).empty();
            $(actionCell).append(updateButton, deleteButton);
        });
    })
}

function onGradeCreate(gradesTable, teacherId, studentId, subjectId, gradeValue) {
    $.ajax({
        method: 'POST',
        url: '/grades/ajax/create_grade',
        data: {
            teacherId: teacherId,
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

function onGradeUpdate(teacherId, studentId, subjectId, gradeValue) {
    $.ajax({
        method: 'POST',
        url: '/grades/ajax/update_grade',
        data: {
            id: $(this).data('grade-id'),
            teacherId: teacherId,
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

function updateGradeStyle() {
    let averageStudyGradeSpan = $('#avgGrade');
    let className = getClassName(averageStudyGradeSpan.text());
    averageStudyGradeSpan.removeClass();
    averageStudyGradeSpan.addClass(className);
}