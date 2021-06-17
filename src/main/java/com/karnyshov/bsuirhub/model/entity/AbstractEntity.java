package com.karnyshov.bsuirhub.model.entity;

public abstract class AbstractEntity {
    public final static long DEFAULT_ID = 0;
    private final long entityId;

    public AbstractEntity(long entityId) {
        this.entityId = entityId;
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
}
