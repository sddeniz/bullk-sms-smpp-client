package com.behsa.usdp.config;

public interface RabbitProducer {
    <T> void sendToRabbit(T t, String exchangeName, String routineKey);
}
