function setActiveNavItem(position) {
    $(window).keydown(function(event){
        if (event.keyCode === 13) {
            event.preventDefault();
            return false;
        }
    });

    $(`#navbar li:nth-child(${position}) a`).addClass('active emphasized-sidebar-item');
}