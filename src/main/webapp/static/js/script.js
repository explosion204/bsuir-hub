const LOCALE = 'locale';
const DEFAULT_LOCALE = 'ru';

window.onload = function () {
    configureFormValidation();
    eventListeners();
}

// $(document).ready(function () {
//     // adjustNav(); todo
//     configureFormValidation();
// });

$(window).resize(function () {
    // adjustNav(); todo
})

function eventListeners() {
    // $('#login-btn').submit(function () {
    //    alert('kkk');
    // });
    // document.getElementById('login-btn').onsubmit = function () {
    //     alert('kkk');
    // }

    document.forms.login.onsubmit = function () {
        $('#login-btn').attr('disabled', 'disabled');
    }
}

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

function setLocale(locale) {
    sessionStorage.setItem(LOCALE, locale);

    $.ajax({
        method: 'POST',
        url: '/set_locale',
        data: {
            'locale': locale
        },
        success: function () {
            window.location.reload();
        }
    });
}