package com.karnyshov.bsuirhub.model.entity;

import java.util.Objects;

/**
 * {@code Group} class represents a group entity.
 * @see AbstractEntity
 * @author Dmitry Karnyshov
 */
public class Group extends AbstractEntity {
    /**
     * Foreign key to {@link Department} entity.
     * */
    private long departmentId;

    /**
     * Foreign key to {@link User} entity.
     * */
    private long headmanId;

    /**
     * Foreign key to {@link User} entity.
     * */
    private long curatorId;

    private String name;
    private transient boolean archived;

    private Group(GroupBuilder builder) {
        super(builder);
        this.departmentId = builder.departmentId;
        this.headmanId = builder.headmanId;
        this.curatorId = builder.curatorId;
        this.name = builder.name;
        this.archived = builder.archived;
    }

    public static GroupBuilder builder() {
        return new GroupBuilder();
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

    public boolean isArchived() {
        return archived;
    }

    @Override
    public int hashCode() {
        int prime = 31;
        int result = prime + super.hashCode();

        result = prime * result + Long.hashCode(departmentId);
        result = prime * result + Long.hashCode(headmanId);
        result = prime * result + Long.hashCode(curatorId);
        result = prime * result + (name != null ? name.hashCode() : 0);
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

        Group group = (Group) obj;
        return super.equals(obj) && group.departmentId == departmentId && group.headmanId == headmanId
                && group.curatorId == curatorId && Objects.equals(group.name, name) && group.archived == archived;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder("Group (");
        builder.append(super.toString()).append("): ");
        builder.append("department id = ").append(departmentId).append(", ");
        builder.append("headman id = ").append(headmanId).append(", ");
        builder.append("curator id = ").append(curatorId).append(", ");
        builder.append("is archived = ").append(archived).append(", ");
        builder.append("name = ").append(name);

        return builder.toString();
    }

    /**
     * {@code GroupBuilder} is a subclass of {@link AbstractBuilder} class and used for building
     * the group entity.
     * @author Dmitry Karnyshov
     */
    public static class GroupBuilder extends AbstractEntity.AbstractBuilder {
        private long departmentId;
        private long headmanId;
        private long curatorId;
        private String name;
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

        public GroupBuilder setName(String name) {
            this.name = name;
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
            this.name = group.name;
            this.archived = group.archived;
            return this;
        }

        @Override
        public Group build() {
            return new Group(this);
        }
    }
}
