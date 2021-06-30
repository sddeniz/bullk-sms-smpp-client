package com.behsa.usdp.interfaces;

import com.cloudhopper.smpp.SmppSession;
import com.cloudhopper.smpp.pdu.QuerySm;
import com.cloudhopper.smpp.pdu.QuerySmResp;

public interface Query {
    QuerySmResp query(SmppSession smppSession, QuerySm querySm);
}
