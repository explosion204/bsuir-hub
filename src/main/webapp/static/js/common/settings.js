$(document).ready(function () {
    const INITIAL_EMAIL = "initialEmail";

    configureValidation([['emailForm', 'changeEmailButton'], ['passwordForm', 'changePasswordButton']]);
    $('#profileImage').on('error', function () {
        this.src = '/static/images/default_profile.jpg';
    });

    let emailInput = $('#emailInput');
    sessionStorage.setItem(INITIAL_EMAIL, emailInput.val());
    emailInput.keyup(function () {
        $('#changeEmailButton').attr('disabled', emailInput.val() === sessionStorage.getItem(INITIAL_EMAIL));
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
        let userId = $('#userId').val();
        uploadProfileImage.call(this, userId);
    });
});