$(document).ready(function () {
    // TODO: hide table init and redraw blinking
    // TODO: ajax optimization

    updateGradeStyle();

    let body = $('body');
    let studentId = body.data('student-id');
    let subjectId = body.data('subject-id');

    let gradesTable = $('#gradesTable').DataTable({
        dom: 'rt',
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
        drawCallback: function () { onDataLoaded(gradesTable); },
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
            { data: 'value' }
        ]
    });
})

function onDataLoaded(table) {
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
        });
    })
}

function updateGradeStyle() {
    let averageStudyGradeSpan = $('#avgGrade');
    let className = getClassName(averageStudyGradeSpan.text());
    averageStudyGradeSpan.removeClass();
    averageStudyGradeSpan.addClass(className);
}