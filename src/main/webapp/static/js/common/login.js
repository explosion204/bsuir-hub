$(document).ready(function () {
    $(window).keydown(function(event){
        if (event.keyCode === 13) {
            event.preventDefault();
            return false;
        }
    });

    configureValidation([['login', 'loginButton']]);
    $('#login').submit(function () {
        $('#loginButton').attr('disabled', true);
    })
})