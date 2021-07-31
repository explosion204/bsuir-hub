/* data: Array [[formId, submitId], ...] */
function configureValidation(data) {
    data.forEach(function (formData) {
        let formId = formData[0];
        let submitId = formData[1];

        let form = $(`#${formId}`);
        let inputs = form.find('input, select').toArray();
        inputs.forEach(function (input) {
            $(input).keyup(() => performValidation(form, inputs, input, submitId))
                .on('select2:select', () => performValidation(form, inputs, input, submitId))
        });
    })
}

function performValidation(form, inputs, input, submitId) {
    form.addClass('was-validated');

    if (inputs.every(input => input.checkValidity())) {
        console.log(input);
        console.log('valid');
        $(`#${submitId}`).attr('disabled', false);
    } else {
        console.log(input);
        console.log('invalid');
        $(`#${submitId}`).attr('disabled', true);
    }
}