package com.karnyshov.bsuirhub.model.entity;

import java.util.Date;

public class Grade extends AbstractEntity {
    private Value value;
    private boolean isExam;
    private long teacherId;
    private long studentId;
    private long subjectId;
    private Date date;

    private Grade(GradeBuilder builder) {
        super(builder);
        this.value = builder.value;
        this.isExam = builder.isExam;
        this.teacherId = builder.teacherId;
        this.studentId = builder.studentId;
        this.subjectId = builder.subjectId;
        this.date = builder.date;
    }

    public enum Value {
        ONE("1"),
        TWO("2"),
        THREE("3"),
        FOUR("4"),
        FIVE("5"),
        SIX("6"),
        SEVEN("7"),
        EIGHT("8"),
        NINE("9"),
        TEN("10"),
        PASSED("passed"),
        NOT_PASSED("not_passed");

        private final String value;

        Value(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return value;
        }
    }

    public static GradeBuilder builder() {
        return new GradeBuilder();
    }

    public Value getValue() {
        return value;
    }

    public boolean getIsExam() {
        return isExam;
    }

    public long getTeacherId() {
        return teacherId;
    }

    public long getStudentId() {
        return studentId;
    }

    public long getSubjectId() {
        return subjectId;
    }

    public Date getDate() {
        return date;
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

    public static class GradeBuilder extends AbstractEntity.AbstractBuilder {
        private Value value;
        private boolean isExam;
        private long teacherId;
        private long studentId;
        private long subjectId;
        private String comment;
        private Date date;

        private GradeBuilder() {

        }

        public GradeBuilder setValue(Value value) {
            this.value = value;
            return this;
        }

        public GradeBuilder setIsExam(boolean isExam) {
            this.isExam = isExam;
            return this;
        }

        public GradeBuilder setTeacherId(long teacherId) {
            this.teacherId = teacherId;
            return this;
        }

        public GradeBuilder setStudentId(long studentId) {
            this.studentId = studentId;
            return this;
        }

        public GradeBuilder setSubjectId(long subjectId) {
            this.subjectId = subjectId;
            return this;
        }

        public GradeBuilder setComment(String comment) {
            this.comment = comment;
            return this;
        }

        public GradeBuilder setDate(Date date) {
            this.date = date;
            return this;
        }

        public GradeBuilder of(Grade grade) {
            super.of(grade);
            this.value = grade.value;
            this.isExam = grade.isExam;
            this.teacherId = grade.teacherId;
            this.studentId = grade.studentId;
            this.subjectId = grade.subjectId;
            this.date = grade.date;
            return this;
        }

        @Override
        public Grade build() {
            return new Grade(this);
        }
    }
}
