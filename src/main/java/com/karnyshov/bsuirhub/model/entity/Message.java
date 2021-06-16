package com.karnyshov.bsuirhub.model.entity;

import java.util.Date;

public class Message extends AbstractEntity {
    private String value;
    private User sender;
    private User receiver;
    private Date date;

    public Message(long entityId, String value, User sender, User receiver, Date date) {
        super(entityId);
        this.value = value;
        this.sender = sender;
        this.receiver = receiver;
        this.date = date;
    }

    public Message(String value, User sender, User receiver, Date date) {
        this(DEFAULT_ID, value, sender, receiver, date);
    }

    public String getValue() {
        return value;
    }

    public User getSender() {
        return sender;
    }

    public User getReceiver() {
        return receiver;
    }

    public Date getDate() {
        return date;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public void setSender(User sender) {
        this.sender = sender;
    }

    public void setReceiver(User receiver) {
        this.receiver = receiver;
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
