$(document).ready(function () {
    // adjustNav();
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