$(document).ready(function () {
    $(window).keydown(function(event){
        if (event.keyCode === 13) {
            event.preventDefault();
            return false;
        }
    });

    configureValidation([['contactForm', 'sendEmailButton']]);
    $('#contactForm').submit(function () {
        $('#sendEmailButton').attr('disabled', true);
    })
})