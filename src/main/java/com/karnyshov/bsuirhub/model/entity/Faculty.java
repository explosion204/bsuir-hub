package com.karnyshov.bsuirhub.model.entity;

import java.util.Objects;

public class Faculty extends AbstractEntity {
    private String name;
    private String shortName;
    private boolean archived;

    public Faculty(long entityId, String name, String shortName, boolean archived) {
        super(entityId);
        this.name = name;
        this.shortName = shortName;
        this.archived = archived;
    }

    public Faculty(String name, String shortName, boolean archived) {
        this(DEFAULT_ID, name, shortName, archived);
    }

    public String getName() {
        return name;
    }

    public String getShortName() {
        return shortName;
    }

    public boolean isArchived() {
        return archived;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public void setArchived(boolean archived) {
        this.archived = archived;
    }

    @Override
    public int hashCode() {
        int prime = 31;
        int result = 1;

        result = result * prime + super.hashCode();
        result = result * prime + name.hashCode();
        result = result * prime + shortName.hashCode();
        result = result * prime + Boolean.hashCode(archived);

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

        Faculty faculty = (Faculty) obj;
        return super.equals(obj) && Objects.equals(name, faculty.name) && Objects.equals(shortName, faculty.shortName)
                && archived == faculty.archived;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder("Faculty (");
        builder.append(super.toString()).append("): ");
        builder.append("name = ").append(name).append(", ");
        builder.append("short name = ").append(shortName).append(", ");
        builder.append("is archived = ").append(archived);

        return builder.toString();
    }
}
