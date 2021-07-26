$(document).ready(function () {
    configureValidation([['login', 'loginButton']]);
    $('#login').submit(function () {
        $('#loginButton').attr('disabled', true);
    })
})