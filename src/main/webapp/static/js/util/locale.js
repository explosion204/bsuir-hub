function setLocale(localeCode) {
    let params = new URLSearchParams(window.location.search);
    let data = paramsToObject(params);
    data.localeCode = localeCode;
    $.ajax({
        method: 'GET',
        url: window.location.pathname,
        data: data,
        success: function () {
            window.location.reload();
        }
    });
}

function paramsToObject(entries) {
    const result = {}
    for (const [key, value] of entries) {
        result[key] = value;
    }
    return result;
}