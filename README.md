# BSUIRHUB #
Application that allows interaction between students and teachers through grades and trackable comments. It provides flexible tools for linking university domain models to each other (e.g. assignment assembles group, teacher and subject). Trackable comments via email notifications guarantee relatively fast response from both sides. Application does not support open registration, i.e. administrator have to create user manually and provide credentials to students or teachers. Administrator is not obliged to be experienced in computer science to use the application successfully.

Role | Description
--- | ---
Guest | Unauthenticated user
Student | Authenticated user
Teacher | Authenticated user
Admin | Authenticated and fully authorized user, this role can be obtained only through direct access to database

User status |
--- |
Not confirmed (by email) |
Confirmed (by email) |
Deleted |

### Guest ###
* Authenticate with login and password received from admin beforehand
* Contact application support submitting message and email for feedback
* Send reset link to email (necessarily confirmed) and use it to set a new password
* Change application language

### Student ###
* Contact application support submitting message and email for feedback
* Change application language
* Change email with subsequent confirmation
* Resend confirmation link if the previous one is expired
* Change password
* Change profile image
* View student's dashboard info
    * View name and short name of faculty
    * View name and short name of department
    * View name (alias) of specialty
    * View name of group
    * View headman of group
    * View curator of group (allowed to be absent)
    * View all students of current group
    * View average grade for all subjects
    * View list of active subjects (subject click => go to grades overview page)
* View grades overview for chosen subject
    * View average grade for chosen subject
    * View grades info including teacher, date and value
    * Leave comments for all student's grades (email notifications about new comments will be sent to creator (teacher) of the grade)
    * Delete own comments
    * Receive email notifications about any new comment at this page

### Teacher ###
* Contact application support submitting message and email for feedback
* Change application language
* Resend confirmation link if the previous one is expired
* Change password
* Change profile image
* View teacher's dashboard info
    * View list of subjects assigned to teacher
    * View list of groups (with faculty and department names) assigned to chosen subject and teacher
    * View list of students of chosen group (student click => go to grades overview page)
* View grades overview for chosen student and subject
    * View student's average grade for chosen subject
    * View grades info including teacher, date and value
    * Leave comments only for grades that associated with teacher (email notifications about new comments will be sent to student associated with the grade)
    * Delete own comments
    * Receive email notifications about new comments only for grade that associated with teacher
    * Add new grade (in from 1 to 10)
    * Delete own grade

### Admin ###
* All permissions of **teacher**
* Access administration panel
    * Users
        * View users
        * Add user (student or teacher)
        * Update user
        * Delete user
        * Delete all associated grades and comments
        * Search by criteria: login, email, last name, role, group
    * Faculties
        * View faculties
        * Add faculty
        * Update faculty
        * Delete faculty
        * Search by criteria: name, short name
    * Departments
        * View departments
        * Add department
        * Update department
        * Delete department
        * Search by criteria: name, short name, faculty
    * Subjects
        * View subjects
        * Add subject
        * Update subject
        * Delete subject
        * Search by criteria: name, short name
    * Groups
        * View groups
        * Add group
        * Update group
        * Delete group
        * Search by criteria: name, short name, department
        * Add assignment (mapping current group to specified teacher and subject)
        * Update assignment
        * Delete assignment

![Database scheme](https://raw.githubusercontent.com/explosion204/bsuir-hub/master/db/scheme.png)
