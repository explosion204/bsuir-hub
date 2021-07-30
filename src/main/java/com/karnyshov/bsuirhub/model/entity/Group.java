package com.karnyshov.bsuirhub.model.entity;

public class Group extends AbstractEntity {
    private long departmentId;
    private long headmanId;
    private long curatorId;
    private String name;

    private Group(GroupBuilder builder) {
        super(builder);
        this.departmentId = builder.departmentId;
        this.headmanId = builder.headmanId;
        this.curatorId = builder.curatorId;
        this.name = builder.name;
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

    public String getName() {
        return name;
    }

    public static class GroupBuilder extends AbstractEntity.AbstractBuilder {
        private long departmentId;
        private long headmanId;
        private long curatorId;
        private String name;

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

        public GroupBuilder setName(String name) {
            this.name = name;
            return this;
        }

        public GroupBuilder of(Group group) {
            super.of(group);
            this.departmentId = group.departmentId;
            this.headmanId = group.headmanId;
            this.curatorId = group.curatorId;
            this.name = group.name;
            return this;
        }

        @Override
        public Group build() {
            return new Group(this);
        }
    }
}
