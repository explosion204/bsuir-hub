$(document).ready(function () {
    updateGradeStyle();
    let bodyBlock = $('body');
    let groupId = bodyBlock.data('group-id');

    let subjectsTable = $('#subjectsTable').DataTable({
        dom: 'rt',
        scrollX: false,
        scrollY: '50vh',
        scrollResize: true,
        ordering: false,
        scroller: {
            loadingIndicator: true,
            displayBuffer: 10
        },
        processing: true,
        serverSide: true,
        drawCallback: function () { onAssignmentsLoaded(subjectsTable); },
        ajax: {
            url: '/ajax/get_assignments',
            data: function (d) {
                d.requestType = 'jquery_datatable';
                d.filterCriteria = 'group';
            }
        },
        search: {
            search: groupId
        },
        columns: [
            { data: null }
        ]
    });

    $('#subjectsTable tbody').on('click', 'tr', onSubjectsTableSelect);
})

function onAssignmentsLoaded(subjectsTable) {
    let assignments = [];
    subjectsTable.rows().data().each(val => assignments.push(val));

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

function onSubjectsTableSelect() {
    let studentId = $('body').data('user-id');
    let subjectId = $(this).data('subject-id');

    window.location.href = `/grades?subjectId=${subjectId}&studentId=${studentId}`;
}

function updateGradeStyle() {
    let averageStudyGradeSpan = $('#averageStudyGrade');
    let className = getClassName(averageStudyGradeSpan.text());
    averageStudyGradeSpan.removeClass();
    averageStudyGradeSpan.addClass(className);
}