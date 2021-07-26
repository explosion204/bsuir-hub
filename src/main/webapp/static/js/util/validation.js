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