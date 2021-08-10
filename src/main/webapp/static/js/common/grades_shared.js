function onCommentsSpanClicked(gradesTable, subjectId, studentId) {
    let tr = $(this).closest('tr');
    let row = gradesTable.row(tr);
    let gradeId = tr.data('grade-id');

    if (row.child.isShown()) {
        // close
        let expandChevron = $('.shown .expand-chevron');
        expandChevron.removeClass('fa-chevron-up');
        expandChevron.addClass('fa-chevron-down');

        row.child.hide();
        tr.removeClass('shown');
    }
    else {
        // open
        let commentsTableLayout = $(`
            <table class="comments-table table">
                <thead>
                <tr>
                    <th></th>
                </tr>
                </thead>
                <tbody>
                </tbody>
            </table>
        `);
        row.child(commentsTableLayout).show();
        tr.addClass('shown');

        let commentsTable = commentsTableLayout.DataTable({
            dom: '<"newComment">rt',
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
                    d.gradeId = gradeId;
                }
            },
            columns: [
                { data: null }
            ]
        });

        let newCommentAreaBody = $('<div class="input-group mb-1"></div>');
        let commentTextArea = $(`<textarea class="form-control" maxlength="255" placeholder="Comment text"></textarea>`);
        let newCommentButton = $(`<button type="button" class="btn btn-secondary" disabled>New comment</button>`)
        newCommentAreaBody.append(commentTextArea, newCommentButton);

        $('div.newComment').html(newCommentAreaBody);

        commentTextArea.keyup(function () {
            let length = $(this).val().trim().length;
            newCommentButton.attr('disabled', !(length > 0 && length < 256));
        });

        newCommentButton.click(function () {
            onCommentCreate(commentsTable, gradeId, subjectId, studentId, commentTextArea.val());
        });

        let expandChevron = $('.shown .expand-chevron');
        expandChevron.removeClass('fa-chevron-down');
        expandChevron.addClass('fa-chevron-up');
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

        let textDiv = $(`<div>${value.text}</div>`);
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