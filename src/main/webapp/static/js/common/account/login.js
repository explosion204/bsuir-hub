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
        sessionStorage.setItem('login', this.login.value);
    })

    if ($('body').data('auth-error') === true) {
        let login = sessionStorage.getItem('login');
        $('#login').find('input[name="login"]').val(login);
    }

    sessionStorage.removeItem('login');
})