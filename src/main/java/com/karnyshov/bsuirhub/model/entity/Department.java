package com.karnyshov.bsuirhub.model.entity;

public class Department extends AbstractEntity {
    private String name;
    private String shortName;
    private boolean archived;
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

    public String getName() {
        return name;
    }

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
