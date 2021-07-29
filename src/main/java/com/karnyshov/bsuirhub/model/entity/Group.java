package com.karnyshov.bsuirhub.model.entity;

public class Group extends AbstractEntity {
    private long departmentId;
    private long headmanId;
    private long curatorId;
    private String groupName;
    private int course;
    boolean archived;

    private Group(GroupBuilder builder) {
        super(builder);
        this.departmentId = builder.departmentId;
        this.headmanId = builder.headmanId;
        this.curatorId = builder.curatorId;
        this.groupName = builder.groupName;
        this.course = builder.course;
        this.archived = builder.archived;
    }

    public static GroupBuilder builder() {
        return new GroupBuilder();
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

    public long getDepartmentId() {
        return departmentId;
    }

    public long getHeadmanId() {
        return headmanId;
    }

    public long getCuratorId() {
        return curatorId;
    }

    public String getGroupName() {
        return groupName;
    }

    public int getCourse() {
        return course;
    }

    public boolean isArchived() {
        return archived;
    }

    public static class GroupBuilder extends AbstractEntity.AbstractBuilder {
        private long departmentId;
        private long headmanId;
        private long curatorId;
        private String groupName;
        private int course;
        private boolean archived;

        private GroupBuilder() {
        }

        public GroupBuilder setDepartmentId(long departmentId) {
            this.departmentId = departmentId;
            return this;
        }

        public GroupBuilder setHeadmanId(long headmanId) {
            this.headmanId = headmanId;
            return this;
        }

        public GroupBuilder setCuratorId(long curatorId) {
            this.curatorId = curatorId;
            return this;
        }

        public GroupBuilder setGroupName(String groupName) {
            this.groupName = groupName;
            return this;
        }

        public GroupBuilder setCourse(int course) {
            this.course = course;
            return this;
        }

        public GroupBuilder setArchived(boolean archived) {
            this.archived = archived;
            return this;
        }

        public GroupBuilder of(Group group) {
            super.of(group);
            this.departmentId = group.departmentId;
            this.headmanId = group.headmanId;
            this.curatorId = group.curatorId;
            this.groupName = group.groupName;
            this.course = group.course;
            this.archived = group.archived;
            return this;
        }

        @Override
        public Group build() {
            return new Group(this);
        }
    }
}
