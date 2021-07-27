$(document).ready(function () {
    setActiveNavItem(1);
    configureValidation([['userForm', 'saveButton']]);

    let form = $('#userForm');

    form.submit(function () {
        $('#saveButton').attr('disabled', true);
    });

    form.on('input', function () {
        let passwordInput = $('#passwordInput')[0];
        let confirmPasswordInput = $('#confirmPasswordInput')[0];
        confirmPasswordInput.setCustomValidity(
            passwordInput.value !== confirmPasswordInput.value ? "passwords do not match" : ""
        );
    })

    $('#profileImage').on('error', function () {
        this.src = '/static/images/default_profile.jpg';
    });

    $('#uploadPicture').click(function () {
       $('#filePicker').trigger('click');
    });


    $('#filePicker').change(function () {
        uploadProfileImage.call(this, $('#targetId').val());
    });
})