$(document).ready(function () {
    $(window).keydown(function(event){
        if (event.keyCode === 13) {
            event.preventDefault();
            return false;
        }
    });

    configureValidation([['forgotPasswordForm', 'sendEmailButton']]);
    $('#forgotPasswordForm').submit(function () {
        console.log('reset')
        $('#sendEmailButton').attr('disabled', true);
    })
})