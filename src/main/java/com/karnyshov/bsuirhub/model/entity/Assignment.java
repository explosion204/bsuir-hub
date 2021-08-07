package com.karnyshov.bsuirhub.model.entity;

public class Assignment extends AbstractEntity {
    private long groupId;
    private long teacherId;
    private long subjectId;

    private Assignment(AssignmentBuilder builder) {
        super(builder);
        this.groupId = builder.groupId;
        this.teacherId = builder.teacherId;
        this.subjectId = builder.subjectId;
    }

    public static AssignmentBuilder builder() {
        return new AssignmentBuilder();
    }

    public long getGroupId() {
        return groupId;
    }

    public long getTeacherId() {
        return teacherId;
    }

    public long getSubjectId() {
        return subjectId;
    }

    @Override
    public int hashCode() {
        int prime = 31;
        int result = prime + super.hashCode();

        result = prime * result + Long.hashCode(groupId);
        result = prime * result + Long.hashCode(teacherId);
        result = prime * result + Long.hashCode(subjectId);

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

        Assignment entity = (Assignment) obj;
        return super.equals(entity) && entity.groupId == groupId && entity.teacherId == teacherId
                && entity.subjectId == subjectId;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder("Assignment (");
        builder.append(super.toString()).append("): ");
        builder.append("group id = ").append(groupId).append(", ");
        builder.append("teacher id = ").append(teacherId).append(", ");
        builder.append("subject id = ").append(subjectId);

        return builder.toString();
    }

    public static class AssignmentBuilder extends AbstractEntity.AbstractBuilder {
        private long groupId;
        private long teacherId;
        private long subjectId;

        private AssignmentBuilder() {
        }

        public AssignmentBuilder setGroupId(long groupId) {
            this.groupId = groupId;
            return this;
        }

        public AssignmentBuilder setTeacherId(long teacherId) {
            this.teacherId = teacherId;
            return this;
        }

        public AssignmentBuilder setSubjectId(long subjectId) {
            this.subjectId = subjectId;
            return this;
        }

        public AssignmentBuilder of(Assignment entity) {
            super.of(entity);
            this.groupId = entity.groupId;
            this.teacherId = entity.teacherId;
            this.subjectId = entity.subjectId;
            return this;
        }

        public Assignment build() {
            return new Assignment(this);
        }
    }
}
