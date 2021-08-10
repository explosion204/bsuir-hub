function invalidateCache() {
    sessionStorage.removeItem('cachedDepartments');
    sessionStorage.removeItem('cachedUsers');
    sessionStorage.removeItem('cachedGroups');
    sessionStorage.removeItem('cachedFaculties');
    sessionStorage.removeItem('cachedSubjects');
}

function fetchDepartment(id, callback) {
    let cachedDepartments = JSON.parse(sessionStorage.getItem('cachedDepartments'));

    if (cachedDepartments === null) {
        cachedDepartments = {};
        sessionStorage.setItem('cachedDepartments', '{}');
    }

    if (id in cachedDepartments) {
        callback(cachedDepartments[id]);
    } else {
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
                    cachedDepartments[id] = data.entity;
                    sessionStorage.setItem('cachedDepartments', JSON.stringify(cachedDepartments));
                    callback(data.entity);
                }
            }
        });
    }
}

function fetchUser(id, callback) {
    let cachedUsers = JSON.parse(sessionStorage.getItem('cachedUsers'));

    if (cachedUsers === null) {
        cachedUsers = {};
        sessionStorage.setItem('cachedUsers', '{}');
    }

    if (id in cachedUsers) {
        callback(cachedUsers[id]);
    } else {
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
                    cachedUsers[id] = data.entity;
                    sessionStorage.setItem('cachedUsers', JSON.stringify(cachedUsers));
                    callback(data.entity);
                }
            }
        });
    }
}

function fetchGroup(id, callback) {
    let cachedGroups = JSON.parse(sessionStorage.getItem('cachedGroups'));

    if (cachedGroups === null) {
        cachedGroups = {};
        sessionStorage.setItem('cachedGroups', '{}');
    }

    if (id in cachedGroups) {
        callback(cachedGroups[id]);
    } else {
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
                    cachedGroups[id] = data.entity;
                    sessionStorage.setItem('cachedGroups', JSON.stringify(cachedGroups));
                    callback(data.entity);
                }
            }
        });
    }
}

function fetchFaculty(id, callback) {
    let cachedFaculties = JSON.parse(sessionStorage.getItem('cachedFaculties'));

    if (cachedFaculties === null) {
        cachedFaculties = {};
        sessionStorage.setItem('cachedFaculties', '{}');
    }

    if (id.toString() in cachedFaculties) {
        callback(cachedFaculties[id]);
    } else {
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
                    cachedFaculties[id] = data.entity;
                    sessionStorage.setItem('cachedFaculties', JSON.stringify(cachedFaculties));
                    callback(data.entity);
                }
            }
        });
    }
}

function fetchSubject(id, callback) {
    let cachedSubjects = JSON.parse(sessionStorage.getItem('cachedSubjects'));

    if (cachedSubjects === null) {
        cachedSubjects = {};
        sessionStorage.setItem('cachedSubjects', '{}');
    }

    if (id in cachedSubjects) {
        callback(cachedSubjects[id]);
    } else {
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
                    cachedSubjects[id] = data.entity;
                    sessionStorage.setItem('cachedSubjects', JSON.stringify(cachedSubjects));
                    callback(data.entity);
                }
            }
        });
    }
}

function fetchAverageGrade(studentId, subjectId, callback) {
    $.ajax({
        method: 'GET',
        url: '/ajax/get_average_grade',
        data: {
            studentId: studentId,
            subjectId: subjectId
        },
        success: function (response) {
            let data = JSON.parse(response);

            if (data && data.status) {
                callback(data);
            }
        }
    })
}