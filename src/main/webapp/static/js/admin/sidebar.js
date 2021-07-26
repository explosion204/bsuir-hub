function setActiveNavItem(position) {
    $(`#navbar li:nth-child(${position}) a`).addClass('active emphasized-sidebar-item');
}