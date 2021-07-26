const LOCALE = 'locale';
const DEFAULT_LOCALE = 'ru';

function setLocale(locale) {
    sessionStorage.setItem(LOCALE, locale);

    $.ajax({
        method: 'POST',
        url: '/api/set_locale',
        data: {
            'locale': locale
        },
        success: function () {
            window.location.reload();
        }
    });
}