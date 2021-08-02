$(document).ready(function () {
    setActiveNavItem(5);
    configureValidation([['subjectForm', 'saveButton']]);

    $('#subjectForm').submit(function () {
        $('#saveButton').attr('disabled', true);
    });
})