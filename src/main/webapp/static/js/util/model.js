function fetchDepartment(id, callback) {
    $.ajax({
        method: 'GET',
        url: '/ajax/get_departments',
        data: {
            id: id,
            requestType: 'fetch_by_id'
        },
        success: function (response) {
            let data = JSON.parse(response);

            if (data && data.status) {
                callback(data);
            }
        }
    })
}

function fetchUser(id, callback) {
    $.ajax({
        method: 'GET',
        url: '/ajax/get_users',
        data: {
            id: id,
            requestType: 'fetch_by_id'
        },
        success: function (response) {
            let data = JSON.parse(response);

            if (data && data.status) {
                callback(data);
            }
        }
    })
}

function fetchGroup(id, callback) {
    $.ajax({
        method: 'GET',
        url: '/ajax/get_groups',
        data: {
            id: id,
            requestType: 'fetch_by_id'
        },
        success: function (response) {
            let data = JSON.parse(response);

            if (data && data.status) {
                callback(data);
            }
        }
    })
}

function fetchFaculty(id, callback) {
    $.ajax({
        method: 'GET',
        url: '/ajax/get_faculties',
        data: {
            id: id,
            requestType: 'fetch_by_id'
        },
        success: function (response) {
            let data = JSON.parse(response);

            if (data && data.status) {
                callback(data);
            }
        }
    })
}

function fetchSubject(id, callback) {
    $.ajax({
        method: 'GET',
        url: '/ajax/get_subjects',
        data: {
            id: id,
            requestType: 'fetch_by_id'
        },
        success: function (response) {
            let data = JSON.parse(response);

            if (data && data.status) {
                callback(data);
            }
        }
    })
}