package com.karnyshov.bsuirhub.model.entity;

import java.util.Objects;

public class Subject extends AbstractEntity {
    private String name;
    private String shortName;

    private Subject(SubjectBuilder builder) {
        super(builder);
        this.name = builder.name;
        this.shortName = builder.shortName;
    }

    public static SubjectBuilder builder() {
        return new SubjectBuilder();
    }

    public String getName() {
        return name;
    }

    public String getShortName() {
        return shortName;
    }

    @Override
    public int hashCode() {
        int prime = 31;
        int result = prime + super.hashCode();

        result = prime * result + (name != null ? name.hashCode() : 0);
        result = prime * result + (shortName != null ? shortName.hashCode() : 0);

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

        Subject subject = (Subject) obj;
        return super.equals(subject) && Objects.equals(subject.name, name)
                && Objects.equals(subject.shortName, shortName);
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder("Subject (");
        builder.append(super.toString()).append("): ");
        builder.append("name = ").append(name).append(", ");
        builder.append("short name = ").append(shortName);

        return builder.toString();
    }

    public static class SubjectBuilder extends AbstractEntity.AbstractBuilder {
        private String name;
        private String shortName;

        private SubjectBuilder() {
        }

        public SubjectBuilder setName(String name) {
            this.name = name;
            return this;
        }

        public SubjectBuilder setShortName(String shortName) {
            this.shortName = shortName;
            return this;
        }

        public SubjectBuilder of(Subject subject) {
            super.of(subject);
            this.name = subject.name;
            this.shortName = subject.shortName;
            return this;
        }

        @Override
        public Subject build() {
            return new Subject(this);
        }
    }
}
