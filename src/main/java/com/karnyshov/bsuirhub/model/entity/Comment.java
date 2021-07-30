package com.karnyshov.bsuirhub.model.entity;

import java.util.Objects;

public class Comment extends AbstractEntity {
    private long gradeId;
    private long userId;
    private String text;

    private Comment(CommentBuilder builder) {
        super(builder);
        this.gradeId = builder.gradeId;
        this.userId = builder.userId;
        this.text = builder.text;
    }

    public static CommentBuilder builder() {
        return new CommentBuilder();
    }

    public long getGradeId() {
        return gradeId;
    }

    public long getUserId() {
        return userId;
    }

    public String getText() {
        return text;
    }

    @Override
    public int hashCode() {
        int prime = 31;
        int result = prime + super.hashCode();

        result = prime * result + Long.hashCode(gradeId);
        result = prime * result + Long.hashCode(userId);
        result = prime * result + (text != null ? text.hashCode() : 0);

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

        Comment comment = (Comment) obj;
        return super.equals(comment) && comment.gradeId == gradeId && comment.userId == userId
                && Objects.equals(comment.text, text);
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder("Comment (");
        builder.append(super.toString()).append("): ");
        builder.append("grade id = ").append(gradeId).append(", ");
        builder.append("user id = ").append(userId).append(", ");
        builder.append("text = ").append(text);

        return builder.toString();
    }

    public static class CommentBuilder extends AbstractEntity.AbstractBuilder {
        private long gradeId;
        private long userId;
        private String text;

        private CommentBuilder() {

        }

        public CommentBuilder setGradeId(long gradeId) {
            this.gradeId = gradeId;
            return this;
        }

        public CommentBuilder setUserId(long userId) {
            this.userId = userId;
            return this;
        }

        public CommentBuilder setText(String text) {
            this.text = text;
            return this;
        }

        public CommentBuilder of(Comment comment) {
            super.of(comment);
            this.gradeId = comment.gradeId;
            this.userId = comment.userId;
            this.text = comment.text;
            return this;
        }

        public Comment build() {
            return new Comment(this);
        }
    }
}
