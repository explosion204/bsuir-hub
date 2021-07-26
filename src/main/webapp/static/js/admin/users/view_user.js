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

    const MAX_FILE_SIZE = 524288;
    $('#filePicker').change(function () {
        let file = this.files[0];

        if (file) {
            if (file.size >= MAX_FILE_SIZE) {
                alert('too big file'); // todo
                window.location.reload();
            }

            let fileReader = new FileReader();
            fileReader.onloadend = function (e) {
                let array = (new Uint8Array(e.target.result)).subarray(0, 4);
                let header = "";

                for (let i = 0; i < array.length; i++) {
                    header += array[i].toString(16);
                }

                console.log(header);
                let isValidImage;
                switch (header) {
                    case "89504e47": // image/png
                    case "ffd8ffe0": // image/jpeg
                    case "ffd8ffe1":
                    case "ffd8ffe2":
                    case "ffd8ffe3":
                    case "ffd8ffe8":
                        isValidImage = true;
                        break;
                    default:
                        isValidImage = false;
                }

                if (isValidImage) {
                    ImageTools.resize(file, {
                        height: 150
                    }, function (blob) {
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
                                alert('error'); // todo
                            }
                        });
                    });
                } else {
                    alert("image is invalid"); // todo
                    window.location.reload();
                }
            }

            fileReader.readAsArrayBuffer(file);
        }
    });
})