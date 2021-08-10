$(document).ready(function () {
    invalidateCache();
    updateGradeStyle();

    let bodyBlock = $('body');
    let studentId = bodyBlock.data('student-id');
    let subjectId = bodyBlock.data('subject-id');
    let userId = bodyBlock.data('user-id');

    let gradesTable = $('#gradesTable').DataTable({
        dom: 'rt',
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
                width: '10%',
                render: function () {
                    return `
                        <span class="btn comments-control">
                            <i class="expand-chevron fas fa-chevron-down fa-xs"></i>
                            <i class="btn far fa-comments fa-lg"></i>
                        </span>
                    `;
                }
            },
            { data: null, width: '30%' },
            { data: null, width: '20%' },
            { data: null, width: '20%' }
        ]
    });

    $('#gradesTable tbody').on('click', 'td span.comments-control', function () {
        onCommentsSpanClicked.call(this, gradesTable, subjectId, studentId);
    });
})

function onDataLoaded(table) {
    table.rows().data().each(function (value, index) {
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

        // grade
        let gradeCell = table.cell(index, 3).node();
        $(gradeCell).text(value.value);

        let row = table.row(index).node();
        $(row).data('grade-id', value.entityId);
    });
}