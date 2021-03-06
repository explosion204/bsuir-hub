$(document).ready(function () {
    const INITIAL_EMAIL = "initialEmail";

    configureValidation([['emailForm', 'changeEmailButton'], ['passwordForm', 'changePasswordButton']]);

    $('#emailForm').submit(function () {
        $('#changeEmailButton').attr('disabled', true);
    });

    let emailInput = $('#emailInput');
    $('#resendConfirmation').attr('disabled', !emailInput[0].checkValidity());
    sessionStorage.setItem(INITIAL_EMAIL, emailInput.val());

    emailInput.keyup(function () {
        $('#changeEmailButton').attr('disabled',
            emailInput.val() === sessionStorage.getItem(INITIAL_EMAIL) || !emailInput[0].checkValidity());
        $('#resendConfirmation').attr('disabled',
            emailInput.val() !== sessionStorage.getItem(INITIAL_EMAIL) || !emailInput[0].checkValidity());
    });

    $('#passwordForm').on('input', function () {
        let passwordInput = $('#passwordInput')[0];
        let confirmPasswordInput = $('#confirmPasswordInput')[0];
        confirmPasswordInput.setCustomValidity(
            passwordInput.value !== confirmPasswordInput.value ? "passwords do not match" : ""
        );
    });

    $('#uploadProfileImage').click(function () {
        $('#filePicker').trigger('click');
    });

    $('#filePicker').change(function () {
        let userId = $('body').data('user-id');
        uploadProfileImage.call(this, userId);
    });
});