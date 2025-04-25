package com.example.Booking.service.basic;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import org.slf4j.Logger;

// Service responsible for publishing confirmation email messages to Kafka
@Service
public class ConfirmationEmailService {

    // Logger for tracking events in this service
    private static final Logger logger = LoggerFactory.getLogger(ConfirmationEmailService.class);

    // KafkaTemplate is used to send messages to Kafka topics
    private final KafkaTemplate<String, String> kafkaTemplate;

    @Value("${booking.kafka.topic}")
    private String topic;

    public ConfirmationEmailService(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    // Sends a simulated email message to the configured Kafka topic
    public void sendEmail(String message) {
        logger.info("Sending message to Kafka topic '{}': {}", topic, message);
        kafkaTemplate.send(topic, message);
    }
}