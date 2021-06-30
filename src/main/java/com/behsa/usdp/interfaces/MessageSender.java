package com.behsa.usdp.interfaces;

import java.util.concurrent.atomic.AtomicInteger;

public interface MessageSender {
    void sendMessage(String msisdn, String message, String senderAddr, String requestId, AtomicInteger seqNumber);
}
