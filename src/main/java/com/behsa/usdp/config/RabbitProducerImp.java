package com.behsa.usdp.config;

import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RabbitProducerImp implements RabbitProducer {
    private static final Logger logger = LoggerFactory.getLogger(RabbitProducerImp.class);
    @Autowired
    private RabbitTemplate template;
    @Autowired
    private Gson gson;

    @Override
    public <T> void sendToRabbit(T t, String exchangeName, String routineKey) {
        try {
            logger.debug("data sent to rabbit: {}", gson.toJson(t));
            template.convertAndSend(exchangeName, routineKey, t.toString());
        } catch (Exception e) {
            logger.error("error in sending message in rabbit");
        }
    }
}
