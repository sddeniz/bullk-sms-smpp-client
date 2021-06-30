package com.behsa.usdp.model;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class SmppConfigurationModel {
    @Value("${smpp.window.size}")
    private Integer smppWindowSize;
    @Value("${smpp.name}")
    private String smppName;
    @Value("${smpp.host}")
    private String smppHost;
    @Value("${smpp.port}")
    private Integer smppPort;
    @Value("${smpp.connection.time.out}")
    private Long smppConnectionTimeOut;
    @Value("${smpp.system.id}")
    private String smppSystemId;
    @Value("${smpp.password}")
    private String smppPassword;
    @Value("${smpp.log.bytes}")
    private boolean isSmppLogBytes;
    @Value("${smpp.expiry.time.out}")
    private Long smppExpiryTimeOut;
    @Value("${smpp.window.monitor.interval}")
    private Long smppWindowMonitorInterval;
    @Value("${smpp.counter.enabled}")
    private boolean isSmppCounterEnabled;
    @Value("${smpp.bind.type}")
    private String smppBindType;

    public String getSmppBindType() {
        return smppBindType;
    }

    public void setSmppBindType(String smppBindType) {
        this.smppBindType = smppBindType;
    }

    public Integer getSmppWindowSize() {
        return smppWindowSize;
    }

    public void setSmppWindowSize(Integer smppWindowSize) {
        this.smppWindowSize = smppWindowSize;
    }

    public String getSmppName() {
        return smppName;
    }

    public void setSmppName(String smppName) {
        this.smppName = smppName;
    }

    public String getSmppHost() {
        return smppHost;
    }

    public void setSmppHost(String smppHost) {
        this.smppHost = smppHost;
    }

    public Integer getSmppPort() {
        return smppPort;
    }

    public void setSmppPort(Integer smppPort) {
        this.smppPort = smppPort;
    }

    public Long getSmppConnectionTimeOut() {
        return smppConnectionTimeOut;
    }

    public void setSmppConnectionTimeOut(Long smppConnectionTimeOut) {
        this.smppConnectionTimeOut = smppConnectionTimeOut;
    }

    public String getSmppSystemId() {
        return smppSystemId;
    }

    public void setSmppSystemId(String smppSystemId) {
        this.smppSystemId = smppSystemId;
    }

    public String getSmppPassword() {
        return smppPassword;
    }

    public void setSmppPassword(String smppPassword) {
        this.smppPassword = smppPassword;
    }

    public boolean isSmppLogBytes() {
        return isSmppLogBytes;
    }

    public void setSmppLogBytes(boolean smppLogBytes) {
        isSmppLogBytes = smppLogBytes;
    }

    public Long getSmppExpiryTimeOut() {
        return smppExpiryTimeOut;
    }

    public void setSmppExpiryTimeOut(Long smppExpiryTimeOut) {
        this.smppExpiryTimeOut = smppExpiryTimeOut;
    }

    public Long getSmppWindowMonitorInterval() {
        return smppWindowMonitorInterval;
    }

    public void setSmppWindowMonitorInterval(Long smppWindowMonitorInterval) {
        this.smppWindowMonitorInterval = smppWindowMonitorInterval;
    }

    public boolean isSmppCounterEnabled() {
        return isSmppCounterEnabled;
    }

    public void setSmppCounterEnabled(boolean smppCounterEnabled) {
        isSmppCounterEnabled = smppCounterEnabled;
    }

    @Override
    public String toString() {
        return "SmppConfigurationModel{" +
                "smppWindowSize=" + smppWindowSize +
                ", smppName='" + smppName + '\'' +
                ", smppHost='" + smppHost + '\'' +
                ", smppPort=" + smppPort +
                ", smppConnectionTimeOut=" + smppConnectionTimeOut +
                ", smppSystemId='" + smppSystemId + '\'' +
                ", smppPassword='" + smppPassword + '\'' +
                ", isSmppLogBytes=" + isSmppLogBytes +
                ", smppExpiryTimeOut=" + smppExpiryTimeOut +
                ", smppWindowMonitorInterval=" + smppWindowMonitorInterval +
                ", isSmppCounterEnabled=" + isSmppCounterEnabled +
                ", smppBindType='" + smppBindType + '\'' +
                '}';
    }
}
