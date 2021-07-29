package com.karnyshov.bsuirhub.model.entity;

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
        return super.hashCode(); // TODO: 7/29/2021
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj); // TODO: 7/29/2021
    }

    @Override
    public String toString() {
        return super.toString(); // TODO: 7/29/2021
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
