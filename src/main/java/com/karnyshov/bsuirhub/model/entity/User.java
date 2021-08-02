package com.karnyshov.bsuirhub.model.entity;

import java.util.Objects;

public class User extends AbstractEntity {
    private String login;
    private String email;
    private transient String passwordHash;
    private transient String salt;
    private UserRole role;
    private UserStatus status;
    private long groupId;
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
        this.role = builder.role;
        this.status = builder.status;
        this.groupId = builder.groupId;
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

    public UserRole getRole() {
        return role;
    }

    public UserStatus getStatus() {
        return status;
    }

    public long getGroupId() {
        return groupId;
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

    @Override
    public int hashCode() {
        int prime = 31;
        int result = prime + super.hashCode();

        result = prime * result + (login != null ? login.hashCode() : 0);
        result = prime * result + (email != null ? email.hashCode() : 0);
        result = prime * result + (passwordHash != null ? passwordHash.hashCode() : 0);
        result = prime * result + (salt != null ? salt.hashCode() : 0);
        result = prime * result + (role != null ? role.hashCode() : 0);
        result = prime * result + (status != null ? status.hashCode() : 0);
        result = prime * result + Long.hashCode(groupId);
        result = prime * result + (firstName != null ? firstName.hashCode() : 0);
        result = prime * result + (patronymic != null ? patronymic.hashCode() : 0);
        result = prime * result + (lastName != null ? lastName.hashCode() : 0);
        result = prime * result + (profilePicturePath != null ? profilePicturePath.hashCode() : 0);

        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }

        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }

        User user = (User) obj;
        return super.equals(user) && Objects.equals(user.login, login) && Objects.equals(user.email, email)
                && Objects.equals(user.passwordHash, passwordHash) && Objects.equals(user.salt, salt)
                && Objects.equals(user.role, role) && Objects.equals(user.status, status)
                && user.groupId == groupId && Objects.equals(user.firstName, firstName)
                && Objects.equals(user.patronymic, patronymic) && Objects.equals(user.lastName, lastName)
                && Objects.equals(user.profilePicturePath, profilePicturePath);
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder("User (");
        builder.append(super.toString()).append("): ");
        builder.append("login = ").append(login).append(", ");
        builder.append("email = ").append(email).append(", ");
        builder.append("password hash = ").append(passwordHash).append(", ");
        builder.append("salt = ").append(salt).append(", ");
        builder.append("user role = ").append(role).append(", ");
        builder.append("user status = ").append(status).append(", ");
        builder.append("group id = ").append(groupId).append(", ");
        builder.append("first name = ").append(firstName).append(", ");
        builder.append("patronymic = ").append(patronymic).append(", ");
        builder.append("last name = ").append(lastName).append(", ");
        builder.append("profile picture path = ").append(profilePicturePath);

        return builder.toString();
    }

    public static class UserBuilder extends AbstractEntity.AbstractBuilder {
        private String login;
        private String email;
        private String passwordHash;
        private String salt;
        private UserRole role;
        private UserStatus status;
        private long groupId;
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

        public UserBuilder setRole(UserRole role) {
            this.role = role;
            return this;
        }

        public UserBuilder setGroupId(long groupId) {
            this.groupId = groupId;
            return this;
        }

        public UserBuilder setStatus(UserStatus status) {
            this.status = status;
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
            super.of(user);
            this.login = user.login;
            this.email = user.email;
            this.passwordHash = user.passwordHash;
            this.salt = user.salt;
            this.role = user.role;
            this.status = user.status;
            this.groupId = user.groupId;
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
