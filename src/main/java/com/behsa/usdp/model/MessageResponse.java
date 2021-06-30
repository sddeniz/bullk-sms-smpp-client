package com.behsa.usdp.model;

public class MessageResponse {
    private String messageId;
    private String id;
    private Integer sequenceNumber;

    public MessageResponse() {
    }

    public MessageResponse(String messageId, String id, Integer sequenceNumber) {
        this.messageId = messageId;
        this.id = id;
        this.sequenceNumber = sequenceNumber;
    }

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getSequenceNumber() {
        return sequenceNumber;
    }

    public void setSequenceNumber(Integer sequenceNumber) {
        this.sequenceNumber = sequenceNumber;
    }

    @Override
    public String toString() {
        return "{" +
                "messageId='" + messageId + '\'' +
                ", id='" + id + '\'' +
                ", sequenceNumber='" + sequenceNumber + '\'' +
                '}';
    }
}
