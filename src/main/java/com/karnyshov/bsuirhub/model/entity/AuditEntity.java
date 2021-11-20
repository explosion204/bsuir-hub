package com.karnyshov.bsuirhub.model.entity;

import java.time.LocalDateTime;
import java.util.Objects;

public class AuditEntity extends AbstractEntity {
    private long userId;
    private String request;
    private LocalDateTime timestamp;

    private AuditEntity(AuditEntityBuilder builder) {
        super(builder);
        this.userId = builder.userId;
        this.request = builder.request;
        this.timestamp = builder.timestamp;
    }

    public static AuditEntityBuilder builder() {
        return new AuditEntityBuilder();
    }

    public long getUserId() {
        return userId;
    }

    public String getRequest() {
        return request;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    @Override
    public int hashCode() {
        int prime = 31;
        int result = prime + super.hashCode();

        result = prime * result + Long.hashCode(userId);
        result = prime * result + (request != null ? request.hashCode() : 0);
        result = prime * result + (timestamp != null ? timestamp.hashCode() : 0);

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

        AuditEntity auditEntity = (AuditEntity) obj;
        return super.equals(auditEntity) && auditEntity.userId == userId && Objects.equals(auditEntity.request, request)
                && Objects.equals(auditEntity.timestamp, timestamp);
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder("AuditEntity (");
        builder.append(super.toString()).append("): ");
        builder.append("user id = ").append(userId).append(", ");
        builder.append("request = ").append(request).append(", ");
        builder.append("timestamp = ").append(timestamp);

        return builder.toString();
    }

    public static class AuditEntityBuilder extends AbstractEntity.AbstractBuilder {
        private long userId;
        private String request;
        private LocalDateTime timestamp;

        private AuditEntityBuilder() {

        }

        public AuditEntityBuilder setUserId(long userId) {
            this.userId = userId;
            return this;
        }

        public AuditEntityBuilder setRequest(String request) {
            this.request = request;
            return this;
        }

        public AuditEntityBuilder setTimestamp(LocalDateTime timestamp) {
            this.timestamp = timestamp;
            return this;
        }

        public AuditEntityBuilder of(AuditEntity auditEntity) {
            super.of(auditEntity);
            this.userId = auditEntity.userId;
            this.request = auditEntity.request;
            this.timestamp = auditEntity.timestamp;
            return this;
        }

        @Override
        public AbstractEntity build() {
            return new AuditEntity(this);
        }
    }
}
