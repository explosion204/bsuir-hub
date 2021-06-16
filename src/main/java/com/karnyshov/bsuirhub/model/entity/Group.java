package com.karnyshov.bsuirhub.model.entity;

public class Group extends AbstractEntity {
    private Department department;
    private User headman;
    private User curator;
    private String groupNumber;

    public Group(long entityId, Department department, User headman, User curator, String groupNumber) {
        super(entityId);
        this.department = department;
        this.headman = headman;
        this.curator = curator;
        this.groupNumber = groupNumber;
    }

    public Group(Department department, User headman, User curator, String groupNumber) {
        this(DEFAULT_ID, department, headman, curator, groupNumber);
    }

    public Department getDepartment() {
        return department;
    }

    public User getHeadman() {
        return headman;
    }

    public User getCurator() {
        return curator;
    }

    public String getGroupNumber() {
        return groupNumber;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }

    public void setHeadman(User headman) {
        this.headman = headman;
    }

    public void setCurator(User curator) {
        this.curator = curator;
    }

    public void setGroupNumber(String groupNumber) {
        this.groupNumber = groupNumber;
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
