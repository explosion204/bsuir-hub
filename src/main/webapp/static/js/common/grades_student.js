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
                width: '5%',
                render: function () {
                    return '<i class="btn far fa-comments fa-lg"></i>';
                }
            },
            { data: null, width: '35%' },
            { data: null, width: '20%' },
            { data: null, width: '20%' }
        ]
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

        // grade
        let gradeCell = table.cell(index, 3).node();
        $(gradeCell).text(value.value);

        let row = table.row(index).node();
        $(row).data('grade-id', value.entityId);
        $(row).data('teacher-id', value.teacherId);
    })

    // optional gradeId url parameter
    useGradeIdParam(grades, subjectId, studentId);
}