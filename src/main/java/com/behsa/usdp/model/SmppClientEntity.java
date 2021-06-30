package com.behsa.usdp.model;

public class SmppClientEntity {

    private Long id;

    private Long msisdn;

    private String requestId;

    private String messageId;

    private String smsSender;

    private Integer sequenceNumber;

    private String message;

    private String trackCode;

    private String ip;

    private Long dateTime;

    private String logLevel;

    private String execLog;

    private Integer errorCode;


    public Long getMsisdn() {
        return msisdn;
    }

    public void setMsisdn(Long msisdn) {
        this.msisdn = msisdn;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public String getSmsSender() {
        return smsSender;
    }

    public void setSmsSender(String smsSender) {
        this.smsSender = smsSender;
    }

    public Integer getSequenceNumber() {
        return sequenceNumber;
    }

    public void setSequenceNumber(Integer sequenceNumber) {
        this.sequenceNumber = sequenceNumber;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTrackCode() {
        return trackCode;
    }

    public void setTrackCode(String trackCode) {
        this.trackCode = trackCode;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public Long getDateTime() {
        return dateTime;
    }

    public void setDateTime(Long dateTime) {
        this.dateTime = dateTime;
    }

    public String getLogLevel() {
        return logLevel;
    }

    public void setLogLevel(String logLevel) {
        this.logLevel = logLevel;
    }

    public String getExecLog() {
        return execLog;
    }

    public void setExecLog(String execLog) {
        this.execLog = execLog;
    }

    public Integer getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(Integer errorCode) {
        this.errorCode = errorCode;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    @Override
    public String toString() {
        return "{" +
                "id=" + id +
                ", msisdn=" + msisdn +
                ", requestId='" + requestId + '\'' +
                ", smsSender='" + smsSender + '\'' +
                ", sequenceNumber=" + sequenceNumber +
                ", message='" + message + '\'' +
                ", trackCode='" + trackCode + '\'' +
                ", ip='" + ip + '\'' +
                ", dateTime=" + dateTime +
                ", logLevel='" + logLevel + '\'' +
                ", execLog='" + execLog + '\'' +
                ", errorCode=" + errorCode +
                ", messageId='" + messageId + '\'' +
                '}';
    }
}
