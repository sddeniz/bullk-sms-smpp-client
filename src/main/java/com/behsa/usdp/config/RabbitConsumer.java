package com.behsa.usdp.config;

import com.behsa.usdp.model.RequestMessage;
import com.behsa.usdp.smpp_client.Client;
import com.google.gson.Gson;
import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RabbitConsumer {
    private static final Logger logger = LoggerFactory.getLogger(RabbitConsumer.class);

    @Autowired
    private Client client;
    @Autowired
    private Gson gson;

    @RabbitListener(queues = "smsGateway/request")
    public void listen(String input) {
        logger.debug("input received from rabbit: {}", input);
        if (null != input) {
            RequestMessage requestMessage = gson.fromJson(input, RequestMessage.class);
            client.sendObject(requestMessage);
        }
    }
}
