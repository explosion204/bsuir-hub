package com.karnyshov.bsuirhub.model.entity;

import java.util.Date;

public class Grade extends AbstractEntity {
    private Value value;
    private boolean isExam;
    private User teacher;
    private User student;
    private Subject subject;
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

    public Grade(long entityId, Value value, boolean isExam, User teacher, User student, Subject subject, String comment,
                Date date) {
        super(entityId);
        this.value = value;
        this.isExam = isExam;
        this.teacher = teacher;
        this.student = student;
        this.subject = subject;
        this.comment = comment;
        this.date = date;
    }

    public Grade(Value value, boolean isExam, User teacher, User student, Subject subject, String comment, Date date) {
        this(DEFAULT_ID, value, isExam, teacher, student, subject, comment, date);
    }

    public Value getValue() {
        return value;
    }

    public boolean isExam() {
        return isExam;
    }

    public User getTeacher() {
        return teacher;
    }

    public User getStudent() {
        return student;
    }

    public Subject getSubject() {
        return subject;
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

    public void setTeacher(User teacher) {
        this.teacher = teacher;
    }

    public void setStudent(User student) {
        this.student = student;
    }

    public void setSubject(Subject subject) {
        this.subject = subject;
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
