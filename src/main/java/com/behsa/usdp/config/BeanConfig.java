package com.behsa.usdp.config;

import com.behsa.usdp.Handler.ClientSmppSessionHandler;
import com.behsa.usdp.model.SmppConfigurationModel;
import com.cloudhopper.smpp.SmppBindType;
import com.cloudhopper.smpp.SmppSession;
import com.cloudhopper.smpp.SmppSessionConfiguration;
import com.cloudhopper.smpp.impl.DefaultSmppClient;
import com.cloudhopper.smpp.impl.DefaultSmppSessionHandler;
import com.cloudhopper.smpp.type.SmppChannelException;
import com.cloudhopper.smpp.type.SmppTimeoutException;
import com.cloudhopper.smpp.type.UnrecoverablePduException;
import com.google.gson.Gson;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.atomic.AtomicInteger;

@Configuration
public class BeanConfig {
    @Value("${exchange.name}")
    private String exchangeName;
    @Value("${queue.name.producer}")
    private String queueNameProducer;
    @Value("${queue.name.consumer}")
    private String queueNameConsumer;
    @Value("${bind}")
    private String routineKeyBind;
    @Value("${smpp.bind.type}")
    private String smppBindType;
    @Autowired
    private SmppConfigurationModel smppConfiguration;
    @Autowired
    private ClientSmppSessionHandler clientSmppSessionHandler;

    @Bean("producer")
    Queue queueProducer() {
        return new Queue(queueNameProducer, false);
    }


    @Bean("request")
    Queue queueRequest() {
        return new Queue(queueNameConsumer, false);
    }

    @Bean
    DirectExchange getDirectExchange() {
        return new DirectExchange(exchangeName, true, false);
    }

    @Bean
    Binding bindingProducer(@Qualifier("producer") Queue queueProducer, DirectExchange exchange) {
        return BindingBuilder.bind(queueProducer).to(exchange).with(queueNameProducer);
    }

    @Bean
    Binding bindingRequest(@Qualifier("request") Queue queueRequest, DirectExchange exchange) {
        return BindingBuilder.bind(queueRequest).to(exchange).with(routineKeyBind);
    }

    @Bean
    MessageListenerAdapter getMessageListenerAdapter() {
        return new MessageListenerAdapter();
    }

    @Bean
    SmppSessionConfiguration getSmppSessionConfiguration() {
        SmppSessionConfiguration config = new SmppSessionConfiguration();
        config.setWindowSize(smppConfiguration.getSmppWindowSize());
        config.setName(smppConfiguration.getSmppName());
        config.setType(SmppBindType.valueOf(smppBindType.toUpperCase()));
        config.setHost(smppConfiguration.getSmppHost());
        config.setPort(smppConfiguration.getSmppPort());
        config.setConnectTimeout(smppConfiguration.getSmppConnectionTimeOut());
        config.setSystemId(smppConfiguration.getSmppSystemId());
        config.setPassword(smppConfiguration.getSmppPassword());
        config.getLoggingOptions().setLogBytes(smppConfiguration.isSmppLogBytes());
        // to enable monitoring (request expiration)
        config.setRequestExpiryTimeout(smppConfiguration.getSmppExpiryTimeOut());
        config.setWindowMonitorInterval(smppConfiguration.getSmppWindowMonitorInterval());
        config.setCountersEnabled(smppConfiguration.isSmppCounterEnabled());
        return config;
    }

    @Bean("ThreadPoolExecutor")
    ThreadPoolExecutor getThreadPoolExecutor() {
        return (ThreadPoolExecutor) Executors.newCachedThreadPool();
    }

    @Bean("ScheduledThreadPoolExecutor")
    ScheduledThreadPoolExecutor getScheduledThreadPoolExecutor() {
        return (ScheduledThreadPoolExecutor) Executors.newScheduledThreadPool(1, new ThreadFactory() {
            private AtomicInteger sequence = new AtomicInteger(0);

            @Override
            public Thread newThread(Runnable r) {
                Thread t = new Thread(r);
                t.setName("SmppClientSessionWindowMonitorPool-" + sequence.getAndIncrement());
                return t;
            }
        });
    }

    @Bean
    DefaultSmppClient getDefaultSmppClient(@Qualifier("ScheduledThreadPoolExecutor") ScheduledThreadPoolExecutor monitorExecutor) {
        return new DefaultSmppClient(Executors.newCachedThreadPool(), 1, monitorExecutor);
    }

    @Bean("DefaultSmppSessionHandler")
    DefaultSmppSessionHandler getDefaultSmppSessionHandler() {
        return clientSmppSessionHandler;
    }

    @Bean
    SmppSession getSmppSession(DefaultSmppClient clientBootstrap, SmppSessionConfiguration smppSessionConfiguration, @Qualifier("DefaultSmppSessionHandler") DefaultSmppSessionHandler sessionHandler) throws UnrecoverablePduException, SmppChannelException, InterruptedException, SmppTimeoutException {
        return clientBootstrap.bind(smppSessionConfiguration, sessionHandler);
    }

    @Bean
    Gson getGson() {
        return new Gson();
    }

}
