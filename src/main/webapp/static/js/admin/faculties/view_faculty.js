$(document).ready(function () {
    setActiveNavItem(2);
    configureValidation([['facultyForm', 'saveButton']]);

    $('#facultyForm').submit(function () {
        $('#saveButton').attr('disabled', true);
    });
})