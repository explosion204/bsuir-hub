$(document).ready(function () {
    // adjustNav();
    configureFormValidation();
});

$(window).resize(function () {
    // adjustNav();
})

function adjustNav() {
    if ($(window).width() < 768) {
        $('.messages-icon').hide();
        $('.messages-text').show();
    } else {
        $('.messages-icon').show();
        $('.messages-text').hide();
    }
}

function configureFormValidation() {
    let forms = document.querySelectorAll('.needs-validation');
    Array.prototype.slice.call(forms)
        .forEach(function (form) {
            form.addEventListener('submit', function (event) {
                if (!form.checkValidity()) {
                    event.preventDefault()
                    event.stopPropagation()
                }

                form.classList.add('was-validated')
            }, false)
        });
}