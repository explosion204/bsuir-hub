package com.karnyshov.bsuirhub.model.entity;

import java.util.Date;

public class Message extends AbstractEntity {
    private String value;
    private long senderId;
    private long receiverId;
    private Date date;

    public Message(long entityId, String value, long senderId, long receiverId, Date date) {
        super(entityId);
        this.value = value;
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.date = date;
    }

    public Message(String value, long senderId, long receiverId, Date date) {
        this(DEFAULT_ID, value, senderId, receiverId, date);
    }

    public String getValue() {
        return value;
    }

    public long getSenderId() {
        return senderId;
    }

    public long getReceiverId() {
        return receiverId;
    }

    public Date getDate() {
        return date;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public void setSenderId(long senderId) {
        this.senderId = senderId;
    }

    public void setReceiverId(long receiverId) {
        this.receiverId = receiverId;
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
