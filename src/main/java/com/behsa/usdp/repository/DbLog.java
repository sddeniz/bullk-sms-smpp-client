package com.behsa.usdp.repository;

import com.behsa.usdp.model.SmppClientEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Date;
import java.util.UUID;

@Component
public class DbLog {
    private static final Logger logger = LoggerFactory.getLogger(DbLog.class);

    public SmppClientEntity getDbLogModel(
            Long msisdn
            , String message
            , String senderAddr
            , String requestId
            , Integer seqNumber
            , String logLevel
            , Integer errorCode
            , String execLog
            , String messageId) {

        SmppClientEntity entity = new SmppClientEntity();

        entity.setDateTime(new Date().getTime());
        entity.setErrorCode(errorCode);
        entity.setExecLog(execLog);
        entity.setIp(getServerIp());
        entity.setLogLevel(logLevel);
        entity.setMessage(message);
        entity.setMsisdn(msisdn);
        entity.setRequestId(requestId);
        entity.setSequenceNumber(seqNumber);
        entity.setSmsSender(senderAddr);
        entity.setTrackCode(UUID.randomUUID().toString());
        entity.setMessageId(messageId);
        logger.debug("SmppClientEntity sent to log queue in Rabbit: {}", entity);
        return entity;
    }

    private String getServerIp() {
        String hostIp = "";
        try {
            InetAddress inetAddress = InetAddress.getLocalHost();
            hostIp = inetAddress.getHostAddress();
        } catch (UnknownHostException e) {
            logger.error("error in get server ip", e);
        }
        return hostIp;
    }
}

