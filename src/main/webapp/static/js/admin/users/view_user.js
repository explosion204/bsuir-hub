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

    $('#uploadPicture').click(function () {
       $('#filePicker').trigger('click');
    });

    $('#filePicker').change(function () {
        let file = this.files[0];

        if (file) {
            console.log(this.files[0]);
            ImageTools.resize(file, {
                width: 150
            }, function (blob, didItResize) {
                let formData = new FormData();
                formData.append('file', blob);
                formData.append('issuerId', $('#issuerId').val());
                formData.append('targetId', $('#targetId').val());
                $.ajax({
                    url: '/api/upload_profile_image',
                    method: 'POST',
                    data: formData,
                    contentType: false,
                    processData: false,
                    success: function (data) {
                        window.location.reload();
                    },
                    error: function () {
                        alert('error');
                    }
                });
            });
        }
    });
})