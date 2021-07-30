package com.karnyshov.bsuirhub.model.entity;

public class GroupTeacherSubject extends AbstractEntity {
    private long groupId;
    private long teacherId;
    private long subjectId;

    private GroupTeacherSubject(GroupTeacherSubjectBuilder builder) {
        super(builder);
        this.groupId = builder.groupId;
        this.teacherId = builder.teacherId;
        this.subjectId = builder.subjectId;
    }

    public static GroupTeacherSubjectBuilder builder() {
        return new GroupTeacherSubjectBuilder();
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

    public static class GroupTeacherSubjectBuilder extends AbstractEntity.AbstractBuilder {
        private long groupId;
        private long teacherId;
        private long subjectId;

        private GroupTeacherSubjectBuilder() {
        }

        public GroupTeacherSubjectBuilder setGroupId(long groupId) {
            this.groupId = groupId;
            return this;
        }

        public GroupTeacherSubjectBuilder setTeacherId(long teacherId) {
            this.teacherId = teacherId;
            return this;
        }

        public GroupTeacherSubjectBuilder setSubjectId(long subjectId) {
            this.subjectId = subjectId;
            return this;
        }

        public GroupTeacherSubjectBuilder of(GroupTeacherSubject entity) {
            super.of(entity);
            this.groupId = entity.groupId;
            this.teacherId = entity.teacherId;
            this.subjectId = entity.subjectId;
            return this;
        }

        public GroupTeacherSubject build() {
            return new GroupTeacherSubject(this);
        }
    }
}
