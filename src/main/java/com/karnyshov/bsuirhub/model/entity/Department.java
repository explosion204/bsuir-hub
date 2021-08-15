package com.karnyshov.bsuirhub.model.entity;

import java.util.Objects;

/**
 * {@code Department} class represents a department entity.
 * @see AbstractEntity
 * @author Dmitry Karnyshov
 */
public class Department extends AbstractEntity {
    private String name;
    private String shortName;
    private transient boolean archived;

    /**
     * Foreign key to {@link Faculty} entity.
     * */
    private long facultyId;

    private String specialtyAlias;

    private Department(DepartmentBuilder builder) {
        super(builder);
        this.name = builder.name;
        this.shortName = builder.shortName;
        this.archived = builder.archived;
        this.facultyId = builder.facultyId;
        this.specialtyAlias = builder.specialtyAlias;
    }

    public static DepartmentBuilder builder() {
        return new DepartmentBuilder();
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

    public long getFacultyId() {
        return facultyId;
    }

    public String getSpecialtyAlias() {
        return specialtyAlias;
    }

    @Override
    public int hashCode() {
        int prime = 31;
        int result = prime + super.hashCode();

        result = prime * result + (name != null ? name.hashCode() : 0);
        result = prime * result + (shortName != null ? shortName.hashCode() : 0);
        result = prime * result + Boolean.hashCode(archived);
        result = prime * result + Long.hashCode(facultyId);
        result = prime * result + (specialtyAlias != null ? specialtyAlias.hashCode() : 0);

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

        Department department = (Department) obj;
        return super.equals(department) && Objects.equals(department.name, name)
                && Objects.equals(department.shortName, shortName) && department.archived == archived
                && department.facultyId == facultyId && Objects.equals(department.specialtyAlias, specialtyAlias);
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder("Department (");
        builder.append(super.toString()).append("): ");
        builder.append("name = ").append(name).append(", ");
        builder.append("short name = ").append(shortName).append(", ");
        builder.append("is archived = ").append(archived).append(", ");
        builder.append("faculty id = ").append(facultyId).append(", ");
        builder.append("specialty alias = ").append(specialtyAlias);

        return builder.toString();
    }

    /**
     * {@code DepartmentBuilder} is a subclass of {@link AbstractBuilder} class and used for building
     * the department entity.
     * @author Dmitry Karnyshov
     */
    public static class DepartmentBuilder extends AbstractEntity.AbstractBuilder {
        private String name;
        private String shortName;
        private boolean archived;
        private long facultyId;
        private String specialtyAlias;

        private DepartmentBuilder() {
        }

        public DepartmentBuilder setName(String name) {
            this.name = name;
            return this;
        }

        public DepartmentBuilder setShortName(String shortName) {
            this.shortName = shortName;
            return this;
        }

        public DepartmentBuilder setArchived(boolean archived) {
            this.archived = archived;
            return this;
        }

        public DepartmentBuilder setFacultyId(long facultyId) {
            this.facultyId = facultyId;
            return this;
        }

        public DepartmentBuilder setSpecialtyAlias(String specialtyAlias) {
            this.specialtyAlias = specialtyAlias;
            return this;
        }

        public DepartmentBuilder of(Department department) {
            super.of(department);
            this.name = department.name;
            this.shortName = department.shortName;
            this.archived = department.archived;
            this.facultyId = department.facultyId;
            this.specialtyAlias = department.specialtyAlias;
            return this;
        }

        @Override
        public Department build() {
            return new Department(this);
        }
    }
}
