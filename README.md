# BSUIRHUB #
It is a system that allows interaction between students and teachers through grades and
trackable comments

Role | Description
--- | ---
Guest | Unauthenticated user
Student | Authenticated user
Teacher | Authenticated user
Admin | Fully authorized user, cannot be added via application

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
    * View faculty name and short name
    * View department name and short name
    * View specialty name
    * View group name
    * View group headman
    * View group curator
    * View all students belonging to his group
    * View average grade for all subjects
    * View list of active subjects (subject click => grades overview)
* View grades overview for chosen subject
    * View average grade for chosen subject
    * View grades info including teacher, date and value
    * Leave comments for all his grades (email notifications about new comments will be sent to grade teacher)
    * Receive email notifications about any new comment at this page

### Teacher ###
* Contact application support submitting message and email for feedback
* Change application language
* Resend confirmation link if the previous one is expired
* Change password
* Change profile image
* View teacher's dashboard info
    * View list of subjects assigned to him
    * View list of groups (with faculty and department names) assigned to picked subject
    * View list of students of chosen group (student click => grades overview)
* View grades overview for chosen student and subject
    * View average grade
    * View grades info including teacher, date and value
    * Leave comments only for his grades (email notifications about new comments will be sent to student)
    * Receive email notifications about new comments only for his grade
    * Add new grade (1 - 10)
    * Delete grade

### Admin ###
* All permissions of **teacher**
* Access administration panel
    * Users
        * Add user (student or teacher)
        * Update user
        * Delete user
    * Faculties
        * Add faculty
        * Update faculty
        * Delete faculty
    * Departments
        * Add department
        * Update department
        * Delete department
    * Subjects
        * Add subject
        * Update subject
        * Delete subject
    * Groups
        * Add group
        * Update group
        * Delete group
        * Add assignment (mapping current group to specified teacher and subject)
        * Update assignment
        * Delete assignment