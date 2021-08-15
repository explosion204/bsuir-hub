package com.karnyshov.bsuirhub.model.entity;

/**
 * {@code AbstractEntity} is a top abstract class in the hierarchy of domain entities.
 * @author Dmitry Karnyshov
 */
public abstract class AbstractEntity {
    private long entityId;

    protected AbstractEntity(AbstractBuilder builder) {
        this.entityId = builder.entityId;
    }

    public long getEntityId() {
        return entityId;
    }

    @Override
    public int hashCode() {
        return Long.hashCode(entityId);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }

        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }

        AbstractEntity entity = (AbstractEntity) obj;
        return entityId == entity.entityId;
    }

    @Override
    public String toString() {
        return "id = " + entityId;
    }

    /**
     * A top class in the hierarchy of inner builder classes.
     */
    public static abstract class AbstractBuilder {
        private long entityId;

        public AbstractBuilder setEntityId(long entityId) {
            this.entityId = entityId;
            return this;
        }

        public AbstractBuilder of(AbstractEntity entity) {
            this.entityId = entity.entityId;
            return this;
        }

        public abstract AbstractEntity build();
    }
}
