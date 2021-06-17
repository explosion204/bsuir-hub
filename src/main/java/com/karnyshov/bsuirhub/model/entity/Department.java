package com.karnyshov.bsuirhub.model.entity;

public class Department extends AbstractEntity {
    private String name;
    private String shortName;
    private boolean archived;
    private long facultyId;
    private String specialtyAlias;

    public Department(long entityId, String name, String shortName, boolean archived, long facultyId,
                      String specialtyAlias) {
        super(entityId);
        this.name = name;
        this.shortName = shortName;
        this.archived = archived;
        this.facultyId = facultyId;
        this.specialtyAlias = specialtyAlias;
    }

    public Department(String name, String shortName, boolean archived, long facultyId, String specialtyAlias) {
        this(DEFAULT_ID, name, shortName, archived, facultyId, specialtyAlias);
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

    public void setName(String name) {
        this.name = name;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public void setArchived(boolean archived) {
        this.archived = archived;
    }

    public void setFacultyId(long facultyId) {
        this.facultyId = facultyId;
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
