package com.karnyshov.bsuirhub.model.entity;

public class Group extends AbstractEntity {
    private long departmentId;
    private long headmanId;
    private long curatorId;
    private String groupNumber;
    private int course;
    boolean archived;

    public Group(long entityId, long departmentId, long headmanId, long curatorId, String groupNumber, int course,
                 boolean archived) {
        super(entityId);
        this.departmentId = departmentId;
        this.headmanId = headmanId;
        this.curatorId = curatorId;
        this.groupNumber = groupNumber;
        this.course = course;
        this.archived = archived;
    }

    public Group(long departmentId, long headmanId, long curatorId, String groupNumber, int course, boolean archived) {
        this(DEFAULT_ID, departmentId, headmanId, curatorId, groupNumber, course, archived);
    }

    public long getDepartmentId() {
        return departmentId;
    }

    public long getHeadmanId() {
        return headmanId;
    }

    public long getCuratorId() {
        return curatorId;
    }

    public String getGroupNumber() {
        return groupNumber;
    }

    public int getCourse() {
        return course;
    }

    public boolean isArchived() {
        return archived;
    }

    public void setDepartmentId(long departmentId) {
        this.departmentId = departmentId;
    }

    public void setHeadmanId(long headmanId) {
        this.headmanId = headmanId;
    }

    public void setCuratorId(long curatorId) {
        this.curatorId = curatorId;
    }

    public void setGroupNumber(String groupNumber) {
        this.groupNumber = groupNumber;
    }

    public void setCourse(int course) {
        this.course = course;
    }

    public void setArchived(boolean archived) {
        this.archived = archived;
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
