$(document).ready(function () {
    $(window).keydown(function(event){
        if (event.keyCode === 13) {
            event.preventDefault();
            return false;
        }
    });

    configureValidation([['resetPasswordForm', 'resetPasswordButton']]);
    let resetPasswordForm = $('#resetPasswordForm');
    resetPasswordForm.submit(function () {
        $('#resetPasswordButton').attr('disabled', true);
    })

    resetPasswordForm.on('input', function () {
        let passwordInput = $('#passwordInput')[0];
        let confirmPasswordInput = $('#confirmPasswordInput')[0];
        confirmPasswordInput.setCustomValidity(
            passwordInput.value !== confirmPasswordInput.value ? "passwords do not match" : ""
        );
    });
})