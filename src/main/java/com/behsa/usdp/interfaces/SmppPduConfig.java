package com.behsa.usdp.interfaces;

import com.behsa.usdp.util.SmppException;
import com.cloudhopper.smpp.SmppConstants;
import com.cloudhopper.smpp.pdu.QuerySm;
import com.cloudhopper.smpp.pdu.SubmitSm;
import com.cloudhopper.smpp.type.Address;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public interface SmppPduConfig {
    Logger logger = LoggerFactory.getLogger(SmppPduConfig.class);

    default SubmitSm getSubmitSm(String msisdn, byte[] textBytes, String senderAddr, Integer sequenceNumber) {
        try {
            SubmitSm submitSm = new SubmitSm();
            submitSm.setRegisteredDelivery(SmppConstants.REGISTERED_DELIVERY_SMSC_RECEIPT_REQUESTED);
            submitSm.setDataCoding((byte) 0x08);
            submitSm.setSequenceNumber(sequenceNumber);
            submitSm.setSourceAddress(new Address((byte) 0x01, (byte) 0x01, senderAddr)); ////3,0
            submitSm.setDestAddress(new Address((byte) 0x01, (byte) 0x01, msisdn));
            submitSm.setShortMessage(textBytes);
            return submitSm;
        } catch (Exception e) {
            logger.debug("error in SmppPduConfig: ", e);
            throw new SmppException("error in SmppPduConfig", e);
        }
    }

    default QuerySm getQuerySm(String messageId, String sourceAddress, Integer sequenceNumber) {
        QuerySm querySm = new QuerySm();
        querySm.setMessageId(messageId);
        querySm.setSourceAddress(new Address((byte) 0x01, (byte) 0x01, sourceAddress));
        querySm.setSequenceNumber(sequenceNumber);
        return querySm;
    }
}
