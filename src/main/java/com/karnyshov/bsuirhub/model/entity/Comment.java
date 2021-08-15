package com.karnyshov.bsuirhub.model.entity;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * {@code Group} class represents a comment entity.
 * @see AbstractEntity
 * @author Dmitry Karnyshov
 */
public class Comment extends AbstractEntity {
    /**
     * Foreign key to {@link Grade} entity.
     * */
    private long gradeId;

    /**
     * Foreign key to {@link User} entity.
     * */
    private long userId;

    private String text;
    private LocalDateTime creationTime;

    private Comment(CommentBuilder builder) {
        super(builder);
        this.gradeId = builder.gradeId;
        this.userId = builder.userId;
        this.text = builder.text;
        this.creationTime = builder.creationTime;
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

    public LocalDateTime getCreationTime() {
        return creationTime;
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
        builder.append("text = ").append(text).append(", ");
        builder.append("creation time = ").append(text);

        return builder.toString();
    }

    /**
     * {@code GroupBuilder} is a subclass of {@link AbstractBuilder} class and used for building
     * the comment entity.
     * @author Dmitry Karnyshov
     */
    public static class CommentBuilder extends AbstractEntity.AbstractBuilder {
        private long gradeId;
        private long userId;
        private String text;
        private LocalDateTime creationTime;

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

        public CommentBuilder setCreationTime(LocalDateTime creationTime) {
            this.creationTime = creationTime;
            return this;
        }

        public CommentBuilder of(Comment comment) {
            super.of(comment);
            this.gradeId = comment.gradeId;
            this.userId = comment.userId;
            this.text = comment.text;
            this.creationTime = comment.creationTime;
            return this;
        }

        public Comment build() {
            return new Comment(this);
        }
    }
}
