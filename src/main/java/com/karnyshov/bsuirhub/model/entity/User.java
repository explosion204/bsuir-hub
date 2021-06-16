package com.karnyshov.bsuirhub.model.entity;

public class User extends AbstractEntity {
    private String login;
    private String email;
    private String passwordHash;
    private String salt;
    private UserRole userRole;
    private UserStatus userStatus;
    private String firstName;
    private String patronymic;
    private String lastName;
    private String profilePicturePath;

    public enum UserRole {
        GUEST, STUDENT, TEACHER, ADMIN
    }

    public enum UserStatus {
        NOT_CONFIRMED, CONFIRMED, DELETED, BLOCKED
    }

    public User(long entityId, String login, String email, String passwordHash, String salt, UserRole userRole,
                UserStatus userStatus, String firstName, String patronymic, String lastName,
                String profilePicturePath) {
        super(entityId);
        this.login = login;
        this.email = email;
        this.passwordHash = passwordHash;
        this.salt = salt;
        this.userRole = userRole;
        this.userStatus = userStatus;
        this.firstName = firstName;
        this.patronymic = patronymic;
        this.lastName = lastName;
        this.profilePicturePath = profilePicturePath;
    }

    public User(String login, String email, String passwordHash, String salt, UserRole userRole,
                UserStatus userStatus, String firstName, String patronymic, String lastName,
                String profilePicturePath) {
        this(DEFAULT_ID, login, email, passwordHash, salt, userRole, userStatus, firstName, patronymic, lastName,
                profilePicturePath);
    }

    public String getLogin() {
        return login;
    }

    public String getEmail() {
        return email;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public String getSalt() {
        return salt;
    }

    public UserRole getUserRole() {
        return userRole;
    }

    public UserStatus getUserStatus() {
        return userStatus;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getPatronymic() {
        return patronymic;
    }

    public String getLastName() {
        return lastName;
    }

    public String getProfilePicturePath() {
        return profilePicturePath;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public void setUserRole(UserRole userRole) {
        this.userRole = userRole;
    }

    public void setUserStatus(UserStatus userStatus) {
        this.userStatus = userStatus;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setPatronymic(String patronymic) {
        this.patronymic = patronymic;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setProfilePicturePath(String profilePicturePath) {
        this.profilePicturePath = profilePicturePath;
    }

    // TODO: 6/16/2021

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
