const MAX_FILE_SIZE = 524288;

function uploadProfileImage(targetId) {
    let file = this.files[0];

    if (file) {
        if (file.size >= MAX_FILE_SIZE) {
            $.confirm({
                title: 'Error',
                content: 'Chosen file is too big',
                buttons: {
                    'OK': function () {
                        window.location.reload();
                    },
                }
            });

            return;
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
                    formData.append('targetId', targetId);
                    $.ajax({
                        url: '/ajax/upload_profile_image',
                        method: 'POST',
                        data: formData,
                        contentType: false,
                        processData: false,
                        success: function (data) {
                            window.location.reload();
                        },
                        error: function () {
                            $.confirm({
                                title: 'Error',
                                content: 'Server-side error occurred',
                                buttons: {
                                    'OK': function () {
                                        window.location.reload();
                                    },
                                }
                            });
                        }
                    });
                });
            } else {
                $.confirm({
                    title: 'Error',
                    content: 'Invalid image type. Supported only JPG, JPEG, PNG',
                    buttons: {
                        'OK': function () {
                            window.location.reload();
                        },
                    }
                });
            }
        }

        fileReader.readAsArrayBuffer(file);
    }
}