package com.karnyshov.bsuirhub.model.entity;

import java.util.Objects;

/**
 * {@code Subject} class represents a subject entity.
 * @see AbstractEntity
 * @author Dmitry Karnyshov
 */
public class Subject extends AbstractEntity {
    private String name;
    private String shortName;
    private boolean archived;

    private Subject(SubjectBuilder builder) {
        super(builder);
        this.name = builder.name;
        this.shortName = builder.shortName;
        this.archived = builder.archived;
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

    public boolean isArchived() {
        return archived;
    }

    @Override
    public int hashCode() {
        int prime = 31;
        int result = prime + super.hashCode();

        result = prime * result + (name != null ? name.hashCode() : 0);
        result = prime * result + (shortName != null ? shortName.hashCode() : 0);
        result = prime * result + Boolean.hashCode(archived);

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
        builder.append("is archived = ").append(archived).append(", ");
        builder.append("short name = ").append(shortName);

        return builder.toString();
    }

    /**
     * {@code SubjectBuilder} is a subclass of {@link AbstractBuilder} class and used for building
     * the subject entity.
     * @author Dmitry Karnyshov
     */
    public static class SubjectBuilder extends AbstractEntity.AbstractBuilder {
        private String name;
        private String shortName;
        private boolean archived;

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

        public SubjectBuilder setArchived(boolean archived) {
            this.archived = archived;
            return this;
        }

        public SubjectBuilder of(Subject subject) {
            super.of(subject);
            this.name = subject.name;
            this.shortName = subject.shortName;
            this.archived = subject.archived;
            return this;
        }

        @Override
        public Subject build() {
            return new Subject(this);
        }
    }
}
