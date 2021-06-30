package com.behsa.usdp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.behsa.usdp")
public class SmppClientApplication implements CommandLineRunner {
    private static final Logger logger = LoggerFactory.getLogger(SmppClientApplication.class);
    @Value("${smpp.version}")
    private String version;

    public static void main(String[] args) {
        SpringApplication.run(SmppClientApplication.class, args);
        logger.debug("smpp client starting ...");
    }

    @Override
    public void run(String... args) {
        logger.debug("*********************************** smpp version {} ***********************************", version);
    }
}
