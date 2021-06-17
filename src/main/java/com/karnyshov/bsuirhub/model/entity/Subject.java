package com.karnyshov.bsuirhub.model.entity;

public class Subject extends AbstractEntity {
    private String name;
    private String shortName;

    private Subject(SubjectBuilder builder) {
        super(builder);
        this.name = builder.name;
        this.shortName = builder.shortName;
    }

    public static SubjectBuilder builder() {
        return new SubjectBuilder();
    }

    public String getName() {
        return name;
    }

    public String getShortName() {
        return shortName;
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

    public static class SubjectBuilder extends AbstractEntity.AbstractBuilder {
        private String name;
        private String shortName;

        private SubjectBuilder() {
        }

        public SubjectBuilder setName(String name) {
            this.name = name;
            return this;
        }

        public SubjectBuilder setShortName(String shortName) {
            this.shortName = shortName;
            return this;
        }

        public SubjectBuilder of(Subject subject) {
            this.name = subject.name;
            this.shortName = subject.shortName;
            return this;
        }

        @Override
        public Subject build() {
            return new Subject(this);
        }
    }
}
