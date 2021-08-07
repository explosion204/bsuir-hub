package com.karnyshov.bsuirhub.model.entity;

import java.time.LocalDate;
import java.util.Objects;

public class Grade extends AbstractEntity {
    private Value value;
    private boolean isExam;
    private long teacherId;
    private long studentId;
    private long subjectId;
    private LocalDate date; // TODO: 8/7/2021 Local Date vs Date ???

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
        NOT_PASSED,
        ONE,
        TWO,
        THREE,
        FOUR,
        FIVE,
        SIX,
        SEVEN,
        EIGHT,
        NINE,
        TEN,
        PASSED;
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

    public LocalDate getDate() {
        return date;
    }

    @Override
    public int hashCode() {
        int prime = 31;
        int result = prime + super.hashCode();

        result = prime * result + (value != null ? value.hashCode() : 0);
        result = prime * result + Boolean.hashCode(isExam);
        result = prime * result + Long.hashCode(teacherId);
        result = prime * result + Long.hashCode(studentId);
        result = prime * result + Long.hashCode(subjectId);
        result = prime * result + (date != null ? date.hashCode() : 0);

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

        Grade grade = (Grade) obj;
        return super.equals(grade) && Objects.equals(grade.value, value) && grade.isExam == isExam
                && grade.teacherId == teacherId && grade.studentId == studentId && grade.subjectId == subjectId
                && Objects.equals(grade.date, date);
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder("Grade (");
        builder.append(super.toString()).append("): ");
        builder.append("value = ").append(value).append(", ");
        builder.append("is exam = ").append(isExam).append(", ");
        builder.append("teacher id = ").append(teacherId).append(", ");
        builder.append("student id = ").append(studentId).append(", ");
        builder.append("subject id = ").append(subjectId).append(", ");
        builder.append("date = ").append(date);

        return builder.toString();
    }

    public static class GradeBuilder extends AbstractEntity.AbstractBuilder {
        private Value value;
        private boolean isExam;
        private long teacherId;
        private long studentId;
        private long subjectId;
        private LocalDate date;

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

        public GradeBuilder setDate(LocalDate date) {
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
