function useGradeIdParam(allGrades, subjectId, studentId) {
    let gradeId = $('body').data('grade-id');
    let filteredGrades = $.grep(allGrades, function (value) {
        return value.entityId === gradeId;
    });

    if (filteredGrades.length === 1) {
        let teacherId = filteredGrades[0].teacherId;
        $('body').data('active-grade-id', gradeId)
        initializeCommentsTable(gradeId, teacherId, subjectId, studentId);
        $('#commentsModal').modal('show');
    }
}

function initializeCommentsTable(gradeId, teacherId, subjectId, studentId) {
    let commentsTable = $('#commentsTable').DataTable();
    let localeCode = $('body').data('locale-code');
    if ($.fn.dataTable.isDataTable('#commentsTable')) {
        commentsTable.destroy();
    }

    commentsTable = $('#commentsTable').DataTable({
        dom: '<"new-comment">rt',
        language: {
            url: `/static/lib/datatables/locale/${localeCode}.json`
        },
        scrollX: false,
        scrollY: '50vh',
        scrollResize: true,
        scroller: {
            loadingIndicator: true,
            displayBuffer: 3
        },
        ordering: false,
        processing: true,
        serverSide: true,
        drawCallback: function () { onCommentsLoaded(commentsTable, subjectId, studentId); },
        ajax: {
            url: '/ajax/get_comments',
            data: function (d) {
                d.requestType = 'jquery_datatable';
                d.gradeId = $('body').data('active-grade-id');
            }
        },
        columns: [
            { data: null }
        ],
        initComplete: function () {
            commentsTableInitComplete(commentsTable, gradeId, teacherId, subjectId, studentId);
        }
    });

    return commentsTable;
}

function commentsTableInitComplete(commentsTable, gradeId, teacherId, subjectId, studentId) {
    console.log('init complete');
    let userId = $('body').data('user-id');

    if (userId === studentId || userId === teacherId) {
        $('div.new-comment').html(`
            <div class="input-group mb-1">
                <textarea id="commentsTextArea" class="form-control" maxlength="255" placeholder="${$('#grades_comment_text').text()}"></textarea>
                <button id="newCommentButton" type="button" class="btn btn-secondary" disabled>${$('#grades_new_comment').text()}</button>
            </div>
        `);

        let commentTextArea = $('#commentsTextArea');
        let newCommentButton = $('#newCommentButton');

        commentTextArea.keyup(function () {
            let length = $(this).val().trim().length;
            newCommentButton.attr('disabled', !(length > 0 && length < 256));
        });

        newCommentButton.click(function () {
            onCommentCreate(commentsTable, gradeId, subjectId, studentId, commentTextArea.val());
        });
    } else {
        $('div.new-comment').html('');
    }
}

function onCommentsLoaded(commentsTable, subjectId, studentId) {
    commentsTable.rows().data().each(function (value, index) {
        let commentBody = $('<div class="wrapper d-flex flex-column"></div>');

        let headerDiv = $('<div class="d-flex flex-row border-bottom pb-2"></div>');
        let nameDiv = $('<div class="lead me-auto"></div>');
        let image = ($(`<img class="profile-image rounded-circle mt-auto mb-auto me-1">`));

        let dateObject = value.creationTime.date;
        let timeObject = value.creationTime.time;
        let day = dateObject.day;
        let month = dateObject.month;
        let year  = dateObject.year;
        let hour = timeObject.hour;
        let minute = Intl.NumberFormat('en-US', {
            minimumIntegerDigits: 2
        }).format(timeObject.minute);
        let time = `${day}.${month}.${year} ${hour}:${minute}`;
        let timeDiv = $(`<div>${time}</div>`);
        headerDiv.append(image, nameDiv, timeDiv);

        let userId = $('body').data('user-id');

        if (userId === value.userId) {
            let deleteIcon = $('<i class="btn far fa-trash-alt ms-2 me-1 mb-2"></i>');
            headerDiv.append(deleteIcon);

            deleteIcon.click(function () {
                onCommentDelete(commentsTable, value.entityId, value.gradeId, subjectId, studentId);
            })
        }

        let textDiv = $(`<td><div>${value.text}</div></td>`);
        commentBody.append(headerDiv, textDiv);
        let row = commentsTable.row(index).node();
        $(row).html(commentBody);

        FETCH_QUEUE.append(fetchUser, [value.userId], function (entity) {
            let name = entity.lastName + ' ' + entity.firstName + ' ' + entity.patronymic;
            nameDiv.text(name);
            image.attr('src', `/static/images/profile/${entity.profileImageName}`);
        });
    });
}

function onCommentCreate(commentsTable, gradeId, subjectId, studentId, text) {
    $.ajax({
        method: 'POST',
        url: '/grades/ajax/create_comment',
        data: {
            subjectId: subjectId,
            studentId: studentId,
            gradeId: gradeId,
            commentText: text
        },
        success: function (response) {
            let data = JSON.parse(response);

            if (data && data.status) {
                commentsTable.draw();
                $('#commentsTextArea').val('');
                $('#newCommentButton').attr('disabled', true);
            }
        }
    })
}

function onCommentDelete(commentsTable, commentId, gradeId, subjectId, studentId) {
    $.ajax({
        method: 'POST',
        url: '/grades/ajax/delete_comment',
        data: {
            id: commentId,
            gradeId: gradeId,
            subjectId: subjectId,
            studentId: studentId
        },
        success: function (response) {
            let data = JSON.parse(response);

            if (data && data.status) {
                commentsTable.draw();
            }
        }
    })
}

function updateGradeStyle() {
    let averageStudyGradeSpan = $('#avgGrade');
    let className = getClassName(averageStudyGradeSpan.text());
    averageStudyGradeSpan.removeClass();
    averageStudyGradeSpan.addClass(className);
}