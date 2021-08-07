function getClassName(gradeValue) {
    gradeValue = Number.parseFloat(gradeValue);
    let styleName;
    if (gradeValue < 5) {
        styleName = 'text-danger';
    } else if (gradeValue >= 5 && gradeValue <= 8) {
        styleName = 'text-warning';
    } else {
        styleName = 'text-success';
    }

    return styleName;
}