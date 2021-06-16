package com.karnyshov.bsuirhub.model.entity;

public class Department extends AbstractEntity {
    private String name;
    private String shortName;
    private boolean isArchived;
    private Faculty faculty;
    private String specialtyAlias;

    public Department(long entityId, String name, String shortName, boolean isArchived, Faculty faculty,
                String specialtyAlias) {
        super(entityId);
        this.name = name;
        this.shortName = shortName;
        this.isArchived = isArchived;
        this.faculty = faculty;
        this.specialtyAlias = specialtyAlias;
    }

    public Department(String name, String shortName, boolean isArchived, Faculty faculty, String specialtyAlias) {
        this(DEFAULT_ID, name, shortName, isArchived, faculty, specialtyAlias);
    }

    public String getName() {
        return name;
    }

    public String getShortName() {
        return shortName;
    }

    public boolean isArchived() {
        return isArchived;
    }

    public Faculty getFaculty() {
        return faculty;
    }

    public String getSpecialtyAlias() {
        return specialtyAlias;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public void setArchived(boolean archived) {
        isArchived = archived;
    }

    public void setFaculty(Faculty faculty) {
        this.faculty = faculty;
    }

    public void setSpecialtyAlias(String specialtyAlias) {
        this.specialtyAlias = specialtyAlias;
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
