package com.behsa.usdp.util;

public class SmppException extends RuntimeException {
    public SmppException(String message) {
        super(message);
    }

    public SmppException(String message, Throwable cause) {
        super(message, cause);
    }
}
