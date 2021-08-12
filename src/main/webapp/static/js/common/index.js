$(document).ready(function () {
    let body = $('body');

    if (body.data('email-confirmation-success')) {
        $.confirm({
            title: 'Success',
            content: 'Email successfully confirmed!',
            buttons: {
                'OK': function () { },
            }
        });
    }
})