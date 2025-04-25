package com.example.Booking.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

// Listens to email confirmation messages from a Kafka topic
@Component
public class ConfirmationEmailListener {

    // Logger for logging incoming messages
    private static final Logger logger = LoggerFactory.getLogger(ConfirmationEmailListener.class);

    // Listens to the configured Kafka topic for email confirmation messages
    @KafkaListener(topics = "${booking.kafka.topic}", groupId = "email-group")
    public void consumeEmail(String message) {
        logger.info("📧 Simulated email received: {}", message);
    }
}