/* data: Array [[formId, submitId], ...] */
function configureValidation(data) {
    data.forEach(function (formData) {
        let formId = formData[0];
        let submitId = formData[1];

        let form = $(`#${formId}`);
        let inputs = form.find('input').toArray();
        inputs.forEach(function (input) {
            $(input).keyup(function () {
                form.addClass('was-validated');

                if (inputs.every(input => input.checkValidity())) {
                    $(`#${submitId}`).attr('disabled', false);
                } else {
                    $(`#${submitId}`).attr('disabled', true);
                }
            })
        })
    })
}

function isImage(file) {
    let fileReader = new FileReader();
    fileReader.onloadend = function (e) {
        let array = (new Uint8Array(e.target.result)).subarray(0, 4);
        let header = "";

        for (let byte in array) {
            header += byte.toString(16);
        }

        console.log(header);
        switch (header) {
            case "89504e47": // image/png
            case "ffd8ffe0": // image/jpeg
            case "ffd8ffe1":
            case "ffd8ffe2":
            case "ffd8ffe3":
            case "ffd8ffe8":
                return true;
        }

        return false;
    }
}