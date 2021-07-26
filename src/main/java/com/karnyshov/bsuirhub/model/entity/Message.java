package com.karnyshov.bsuirhub.model.entity;

import java.util.Date;

public class Message extends AbstractEntity {
    private String value;
    private long senderId;
    private long receiverId;
    private Date date;

    private Message(MessageBuilder builder) {
        super(builder);
        this.value = builder.value;
        this.senderId = builder.senderId;
        this.receiverId = builder.receiverId;
        this.date = builder.date;
    }

    public static MessageBuilder builder() {
        return new MessageBuilder();
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

    public static class MessageBuilder extends AbstractEntity.AbstractBuilder {
        private String value;
        private long senderId;
        private long receiverId;
        private Date date;

        private MessageBuilder() {

        }

        public MessageBuilder setValue(String value) {
            this.value = value;
            return this;
        }

        public MessageBuilder setSenderId(long senderId) {
            this.senderId = senderId;
            return this;
        }

        public MessageBuilder setReceiverId(long receiverId) {
            this.receiverId = receiverId;
            return this;
        }

        public MessageBuilder setDate(Date date) {
            this.date = date;
            return this;
        }

        public MessageBuilder of(Message message) {
            super.of(message);
            this.value = message.value;
            this.senderId = message.senderId;
            this.receiverId = message.receiverId;
            this.date = message.date;
            return this;
        }

        @Override
        public Message build() {
            return new Message(this);
        }
    }
}
