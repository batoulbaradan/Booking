package com.example.Booking.kafka;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class KafkaEmailConsumer {

    private static final Logger logger = LoggerFactory.getLogger(KafkaEmailConsumer.class);

    @KafkaListener(topics = "${booking.kafka.topic}", groupId = "email-group")
    public void consumeEmail(String message) {
        logger.info("📧 Simulated email received: {}", message);
    }
}