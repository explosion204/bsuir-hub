package com.karnyshov.bsuirhub.model.entity;

public class Subject extends AbstractEntity {
    private String name;
    private String shortName;

    public Subject(long entityId, String name, String shortName) {
        super(entityId);
        this.name = name;
        this.shortName = shortName;
    }

    public Subject(String name, String shortName) {
        this(DEFAULT_ID, name, shortName);
    }

    public String getName() {
        return name;
    }

    public String getShortName() {
        return shortName;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
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
