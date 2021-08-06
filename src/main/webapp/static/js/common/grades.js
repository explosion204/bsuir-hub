$(document).ready(function () {
    $('#gradesTable').DataTable({
        dom: 'rt',
        scrollX: false,
        scrollY: '70vh',
        scrollResize: true,
        // scroller: {
        //     loadingIndicator: true,
        //     displayBuffer: 1
        // },
        // processing: true,
        // serverSide: true,
        // drawCallback: function () { onAssignmentsLoaded(subjectsTable); },
        // ajax: {
        //     url: '/ajax/get_study_assignments',
        //     data: function (d) {
        //         d.requestType = 'jquery_datatable';
        //         d.filterCriteria = 'none';
        //         d.teacherId = teacherId;
        //     }
        // },
        // columns: [
        //     { data: null }
        // ]
    });
})