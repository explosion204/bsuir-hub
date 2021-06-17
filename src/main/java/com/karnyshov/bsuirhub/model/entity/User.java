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

    private User(UserBuilder builder) {
        super(builder);
        this.login = builder.login;
        this.email = builder.email;
        this.passwordHash = builder.passwordHash;
        this.salt = builder.salt;
        this.userRole = builder.userRole;
        this.userStatus = builder.userStatus;
        this.firstName = builder.firstName;
        this.patronymic = builder.patronymic;
        this.lastName = builder.lastName;
        this.profilePicturePath = builder.profilePicturePath;
    }

    public static UserBuilder builder() {
        return new UserBuilder();
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

    public static class UserBuilder extends AbstractEntity.AbstractBuilder {
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

        private UserBuilder() {

        }

        public UserBuilder setLogin(String login) {
            this.login = login;
            return this;
        }

        public UserBuilder setEmail(String email) {
            this.email = email;
            return this;
        }

        public UserBuilder setPasswordHash(String passwordHash) {
            this.passwordHash = passwordHash;
            return this;
        }

        public UserBuilder setSalt(String salt) {
            this.salt = salt;
            return this;
        }

        public UserBuilder setUserRole(UserRole userRole) {
            this.userRole = userRole;
            return this;
        }

        public UserBuilder setUserStatus(UserStatus userStatus) {
            this.userStatus = userStatus;
            return this;
        }

        public UserBuilder setFirstName(String firstName) {
            this.firstName = firstName;
            return this;
        }

        public UserBuilder setPatronymic(String patronymic) {
            this.patronymic = patronymic;
            return this;
        }

        public UserBuilder setLastName(String lastName) {
            this.lastName = lastName;
            return this;
        }

        public UserBuilder setProfilePicturePath(String profilePicturePath) {
            this.profilePicturePath = profilePicturePath;
            return this;
        }

        public UserBuilder of(User user) {
            this.login = user.login;
            this.email = user.email;
            this.passwordHash = user.passwordHash;
            this.salt = user.salt;
            this.userRole = user.userRole;
            this.userStatus = user.userStatus;
            this.firstName = user.firstName;
            this.patronymic = user.patronymic;
            this.lastName = user.lastName;
            this.profilePicturePath = user.profilePicturePath;
            return this;
        }

        @Override
        public User build() {
            return new User(this);
        }
    }
}
