package com.karnyshov.bsuirhub.model.entity;

import java.util.Objects;

/**
 * {@code Faculty} class represents a faculty entity.
 * @see AbstractEntity
 * @author Dmitry Karnyshov
 */
public class Faculty extends AbstractEntity {
    private String name;
    private String shortName;
    private transient boolean archived;

    private Faculty(FacultyBuilder builder) {
        super(builder);
        this.name = builder.name;
        this.shortName = builder.shortName;
        this.archived = builder.archived;
    }

    public static FacultyBuilder builder() {
        return new FacultyBuilder();
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

        result = prime * result + name.hashCode();
        result = prime * result + shortName.hashCode();
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

        Faculty faculty = (Faculty) obj;
        return super.equals(faculty) && Objects.equals(name, faculty.name) && Objects.equals(shortName, faculty.shortName)
                && archived == faculty.archived;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder("Faculty (");
        builder.append(super.toString()).append("): ");
        builder.append("name = ").append(name).append(", ");
        builder.append("short name = ").append(shortName).append(", ");
        builder.append("is archived = ").append(archived);

        return builder.toString();
    }

    /**
     * {@code FacultyBuilder} is a subclass of {@link AbstractBuilder} class and used for building
     * the faculty entity.
     * @author Dmitry Karnyshov
     */
    public static class FacultyBuilder extends AbstractEntity.AbstractBuilder {
        private String name;
        private String shortName;
        private boolean archived;

        private FacultyBuilder() {
        }

        public FacultyBuilder setName(String name) {
            this.name = name;
            return this;
        }

        public FacultyBuilder setShortName(String shortName) {
            this.shortName = shortName;
            return this;
        }

        public FacultyBuilder setArchived(boolean archived) {
            this.archived = archived;
            return this;
        }

        public FacultyBuilder of(Faculty faculty) {
            super.of(faculty);
            this.name = faculty.name;
            this.shortName = faculty.shortName;
            this.archived = faculty.archived;
            return this;
        }

        @Override
        public Faculty build() {
            return new Faculty(this);
        }
    }
}
