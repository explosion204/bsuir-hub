function setLocale(localeCode) {
    $.ajax({
        method: 'POST',
        url: window.location.pathname,
        data: {
            'localeCode': localeCode
        },
        success: function () {
            window.location.reload();
        }
    });
}