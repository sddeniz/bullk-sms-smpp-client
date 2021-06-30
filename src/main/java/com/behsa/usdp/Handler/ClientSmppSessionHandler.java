package com.behsa.usdp.Handler;

import com.behsa.usdp.config.RabbitProducer;
import com.behsa.usdp.model.MessageResponse;
import com.behsa.usdp.model.RequestMessage;
import com.behsa.usdp.repository.DbLog;
import com.behsa.usdp.util.MessageRequestCache;
import com.cloudhopper.smpp.PduAsyncResponse;
import com.cloudhopper.smpp.impl.DefaultSmppSessionHandler;
import com.cloudhopper.smpp.pdu.PduRequest;
import com.cloudhopper.smpp.pdu.PduResponse;
import com.cloudhopper.smpp.pdu.SubmitSmResp;
import com.google.gson.Gson;
import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ClientSmppSessionHandler extends DefaultSmppSessionHandler {
    private static final Logger logger = LoggerFactory.getLogger(ClientSmppSessionHandler.class);

    @Autowired
    private MessageRequestCache messageRequestCache;
    @Autowired
    private RabbitProducer rabbitProducer;
    @Autowired
    private Gson gson;

    @Value("${exchange.name}")
    private String xname;
    @Value("${routingKey}")
    private String routineKey;
    @Value("${routingKeyLog}")
    private String routineKeyLog;
    @Autowired
    private DbLog dbLog;

    private RequestMessage requestMessage;


    public ClientSmppSessionHandler() {
        super(logger);
    }

    @Override
    public void firePduRequestExpired(PduRequest pduRequest) {
        logger.warn("PDU request expired: {}", pduRequest);
    }

    @Override
    public PduResponse firePduRequestReceived(PduRequest pduRequest) {
        PduResponse response = pduRequest.createResponse();
        return response;
    }

    @Override
    public void fireExpectedPduResponseReceived(PduAsyncResponse pduAsyncResponse) {
        super.fireExpectedPduResponseReceived(pduAsyncResponse);
        String requestId = this.messageRequestCache.get(pduAsyncResponse.getRequest().getSequenceNumber());
        if (requestId != null) {
            SubmitSmResp response = (SubmitSmResp) pduAsyncResponse.getResponse();
            Integer commandStatus = response.getCommandStatus() == 0 ? null : response.getCommandStatus();
            MessageResponse messageResponse = new MessageResponse(response.getMessageId(), requestId, response.getSequenceNumber());

            rabbitProducer.sendToRabbit(gson.fromJson(messageResponse.toString(), JSONObject.class), xname, routineKey);

            rabbitProducer.sendToRabbit(
                    this.dbLog.getDbLogModel(
                            Long.parseLong(this.requestMessage.getMSISDN())
                            , this.requestMessage.getMessage()
                            , this.requestMessage.getSmsNo()
                            , requestId
                            , response.getSequenceNumber()
                            , "Info"
                            , commandStatus
                            , commandStatus == null ? null : response.getResultMessage()
                            , response.getMessageId())
                    , xname
                    , routineKeyLog);
        }
    }

    public void set(String MSISDN, String smsSender, String message) {
        this.requestMessage = new RequestMessage();
        this.requestMessage.setMessage(message);
        this.requestMessage.setMSISDN(MSISDN);
        this.requestMessage.setSmsNo(smsSender);

    }
}
