package com.example.Booking.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class ConfirmationEmailListener {

    private static final Logger logger = LoggerFactory.getLogger(ConfirmationEmailListener.class);

    @KafkaListener(topics = "${booking.kafka.topic}", groupId = "email-group")
    public void consumeEmail(String message) {
        logger.info("📧 Simulated email received: {}", message);
    }
}