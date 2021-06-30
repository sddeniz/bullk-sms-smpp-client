package com.behsa.usdp.model;

import com.behsa.usdp.util.StringSanitizeDeserializer;
import com.google.gson.annotations.JsonAdapter;

public class RequestMessage {
    @JsonAdapter(StringSanitizeDeserializer.class)
    private String MSISDN;
    @JsonAdapter(StringSanitizeDeserializer.class)
    private String message;
    @JsonAdapter(StringSanitizeDeserializer.class)
    private String smsNo;
    @JsonAdapter(StringSanitizeDeserializer.class)
    private String id;

    public String getMSISDN() {
        return MSISDN;
    }

    public void setMSISDN(String MSISDN) {
        this.MSISDN = MSISDN;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSmsNo() {
        return smsNo;
    }

    public void setSmsNo(String smsNo) {
        this.smsNo = smsNo;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "{" +
                "MSISDN='" + MSISDN + '\'' +
                ", message='" + message + '\'' +
                ", smsNo='" + smsNo + '\'' +
                ", id='" + id + '\'' +
                '}';
    }
}
