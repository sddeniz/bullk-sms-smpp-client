package com.behsa.usdp.interfaces;

import com.behsa.usdp.Handler.ClientSmppSessionHandler;
import com.behsa.usdp.config.RabbitProducer;
import com.behsa.usdp.model.MessageResponse;
import com.behsa.usdp.repository.DbLog;
import com.behsa.usdp.util.MessageRequestCache;
import com.behsa.usdp.util.SmppException;
import com.behsa.usdp.util.SplitMessage;
import com.cloudhopper.commons.charset.CharsetUtil;
import com.cloudhopper.smpp.SmppConstants;
import com.cloudhopper.smpp.SmppSession;
import com.cloudhopper.smpp.pdu.SubmitSm;
import com.cloudhopper.smpp.pdu.SubmitSmResp;
import com.google.gson.Gson;
import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicInteger;

@Component
public class MessageSenderImp implements MessageSender {
    private static final Logger logger = LoggerFactory.getLogger(MessageSenderImp.class);

    @Value("${maximum.multi.part.message.segment.size}")
    private Integer maxSegmentSize;
    @Value("${is.smpp.syncronous}")
    private boolean isSmppSyncronous;
    @Value("${send.message.response.time.out.mills}")
    private Integer timeoutMillis;
    @Autowired
    private SplitMessage splitMessage;
    @Autowired
    private SmppSession smppSession;
    @Autowired
    private SmppPduConfig smppPduConfig;
    @Autowired
    private MessageRequestCache messageRequestCache;
    @Autowired
    private RabbitProducer rabbitProducer;
    @Autowired
    private Gson gson;
    @Autowired
    private DbLog dbLog;
    @Autowired
    private ClientSmppSessionHandler handler;

    @Value("${exchange.name}")
    private String xname;
    @Value("${routingKey}")
    private String routineKey;
    @Value("${routingKeyLog}")
    private String routineKeyLog;


    @Override
    public void sendMessage(String msisdn, String message, String senderAddr, String requestId, AtomicInteger seqNumber) {
        try {
            byte[] messageByte = CharsetUtil.encode(message, CharsetUtil.NAME_UCS_2);

            byte[][] multiMessageByte;
            if (message.length() > maxSegmentSize) {
                multiMessageByte = splitMessage.splitUnicodeMessage(messageByte, maxSegmentSize);
            } else {
                multiMessageByte = new byte[][]{messageByte};
            }
            for (int i = 0; i < multiMessageByte.length; i++) {
                int seq = seqNumber.getAndIncrement();
                SubmitSm submitSm = smppPduConfig.getSubmitSm(msisdn, multiMessageByte[i], senderAddr, seq);
                if (multiMessageByte.length > 1) {
                    submitSm.setEsmClass(SmppConstants.ESM_CLASS_UDHI_MASK);
                }
                logger.debug("SubmitSm: {}", submitSm);
                if (isSmppSyncronous) {
                    SubmitSmResp submitSmResp = smppSession.submit(submitSm, timeoutMillis);
                    logger.debug("SubmitSmResp: {}", submitSmResp);
                    if (null != submitSmResp) {
                        sendToRabbitQueue(submitSmResp.getMessageId(), requestId, submitSmResp.getSequenceNumber());

                        Integer commandStatus = submitSmResp.getCommandStatus() == 0 ? null : submitSmResp.getCommandStatus();

                        rabbitProducer.sendToRabbit(
                                this.dbLog.getDbLogModel(
                                        Long.parseLong(msisdn)
                                        , message
                                        , senderAddr
                                        , requestId
                                        , seq
                                        , "Info"
                                        , commandStatus
                                        , commandStatus == null ? null : submitSmResp.getResultMessage()
                                        , submitSmResp.getMessageId())
                                , xname
                                , routineKeyLog);
                    }
                } else {
                    smppSession.sendRequestPdu(submitSm, timeoutMillis, false);
                    this.messageRequestCache.put(seq, requestId);
                    handler.set(msisdn, senderAddr, message);
                }
            }
        } catch (Exception e) {
            logger.error("error in sendMessage: ", e);
            rabbitProducer.sendToRabbit(
                    this.dbLog.getDbLogModel(
                            Long.parseLong(msisdn)
                            , message
                            , senderAddr
                            , requestId
                            , -1
                            , "Error"
                            , 3001
                            , null
                            , "-1")
                    , xname
                    , routineKeyLog);
            throw new SmppException("error in sendMessage", e);
        }
    }

    private void sendToRabbitQueue(String messageId, String id, Integer seqNumber) {
        MessageResponse messageResponse = new MessageResponse(messageId, id, seqNumber);
        logger.debug("data sent to rabbit: {}", messageResponse);
        rabbitProducer.sendToRabbit(gson.fromJson(messageResponse.toString(), JSONObject.class), xname, routineKey);
    }
}
