package com.behsa.usdp.interfaces;

import com.behsa.usdp.util.SmppException;
import com.cloudhopper.commons.util.windowing.WindowFuture;
import com.cloudhopper.smpp.SmppSession;
import com.cloudhopper.smpp.pdu.PduRequest;
import com.cloudhopper.smpp.pdu.PduResponse;
import com.cloudhopper.smpp.pdu.QuerySm;
import com.cloudhopper.smpp.pdu.QuerySmResp;
import com.cloudhopper.smpp.type.RecoverablePduException;
import com.cloudhopper.smpp.type.SmppChannelException;
import com.cloudhopper.smpp.type.SmppTimeoutException;
import com.cloudhopper.smpp.type.UnrecoverablePduException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.channels.ClosedChannelException;

@Component
public class QueryIml implements Query {
    private static final Logger logger = LoggerFactory.getLogger(QueryIml.class);
    @Value("${send.message.response.time.out.mills}")
    private Integer timeoutMillis;

    @Override
    public QuerySmResp query(SmppSession smppSession, QuerySm querySm) {
        try {

            WindowFuture<Integer, PduRequest, PduResponse> future = smppSession.sendRequestPdu(querySm, 10000, true);
            while (!future.isDone()) {
            }
            QuerySmResp queryResp = (QuerySmResp) future.getResponse();
            logger.debug("QuerySmResp: {}", queryResp);

            boolean completedWithinTimeout = future.await();
            if (!completedWithinTimeout) {
                future.cancel();
                throw new SmppTimeoutException("Unable to get response within [" + timeoutMillis + " ms]");
            }
            if (future.isSuccess()) {
                logger.debug("Response from gateway: {}", future.getResponse());
                return queryResp;
            } else if (future.getCause() != null) {
                Throwable cause = future.getCause();
                if (cause instanceof ClosedChannelException) {
                    throw new SmppChannelException("Channel was closed after sending request, but before receiving response", cause);
                } else {
                    throw new UnrecoverablePduException(cause.getMessage(), cause);
                }
            } else if (future.isCancelled()) {
                throw new RecoverablePduException("Request was cancelled");
            } else {
                throw new UnrecoverablePduException("Unable to sendRequestAndGetResponse successfully (future was in strange state)");
            }
        } catch (Exception e) {
            logger.error("error in getQuerySmResp");
            throw new SmppException("error in getQuerySmResp", e);
        }
    }
}
