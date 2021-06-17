package com.karnyshov.bsuirhub.model.entity;

import java.util.Date;

public class Grade extends AbstractEntity {
    private Value value;
    private boolean isExam;
    private long teacherId;
    private long studentId;
    private long subjectId;
    private String comment;
    private Date date;

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
        PASSED("Passed"), // TODO: 6/16/2021 locale
        NOT_PASSED("Not Passed"); // TODO: 6/16/2021 locale

        private final String value;

        Value(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return value;
        }
    }

    public Grade(long entityId, Value value, boolean isExam, long teacherId, long studentId, long subjectId,
                 String comment, Date date) {
        super(entityId);
        this.value = value;
        this.isExam = isExam;
        this.teacherId = teacherId;
        this.studentId = studentId;
        this.subjectId = subjectId;
        this.comment = comment;
        this.date = date;
    }

    public Grade(Value value, boolean isExam, long teacherId, long studentId, long subjectId, String comment, Date date) {
        this(DEFAULT_ID, value, isExam, teacherId, studentId, subjectId, comment, date);
    }

    public Value getValue() {
        return value;
    }

    public boolean isExam() {
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

    public String getComment() {
        return comment;
    }

    public Date getDate() {
        return date;
    }

    public void setValue(Value value) {
        this.value = value;
    }

    public void setExam(boolean exam) {
        isExam = exam;
    }

    public void setTeacherId(long teacherId) {
        this.teacherId = teacherId;
    }

    public void setStudentId(long studentId) {
        this.studentId = studentId;
    }

    public void setSubjectId(long subjectId) {
        this.subjectId = subjectId;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public void setDate(Date date) {
        this.date = date;
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
