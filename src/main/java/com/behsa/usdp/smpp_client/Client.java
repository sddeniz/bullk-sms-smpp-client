package com.behsa.usdp.smpp_client;

import com.behsa.usdp.interfaces.MessageSender;
import com.behsa.usdp.model.RequestMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.concurrent.atomic.AtomicInteger;

@Service
public class Client {
    private static final Logger logger = LoggerFactory.getLogger(Client.class);
    private static AtomicInteger seqNumber = new AtomicInteger(1);
    private static final int maxAllowedSequenceNumber = 2147483647;

    @Autowired
    private MessageSender messageSender;

    @Value("${send.message.response.time.out.mills}")
    private Integer timeoutMillis;
    @Value("${maximum.multi.part.message.segment.size}")
    private Integer maxSegmentSize;
    @Value("${is.smpp.syncronous}")
    private boolean isSmppSyncronous;
    @Value("${exchange.name}")
    private String xname;
    @Value("${routingKeyLog}")
    private String routineKeyLog;


    public void sendObject(RequestMessage requestMessage) {
        if (null != requestMessage) {
            String msisdn = requestMessage.getMSISDN();
            String message = requestMessage.getMessage()==null?"":requestMessage.getMessage();
            String smsNo = requestMessage.getSmsNo();
            String id = requestMessage.getId();
            try {
                /*
                The allowed sequence_number range is from 0x00000001 to 0x7FFFFFFF.
                   In case seqNumber reaches its maximum value, it should be reset to 0x00000001.
                */
                if (seqNumber.get() == maxAllowedSequenceNumber) {
                    seqNumber.set(1);
                }
                logger.debug("inputs to be sent to smpp server: {}", requestMessage);

                this.messageSender.sendMessage(
                        msisdn
                        , message
                        , smsNo
                        , id
                        , seqNumber
                );

            } catch (Exception e) {
                logger.error("error in sendObject: ", e);
            }
        }
    }
}
