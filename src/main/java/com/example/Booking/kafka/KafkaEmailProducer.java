package com.example.Booking.kafka;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import org.slf4j.Logger;

@Service
public class KafkaEmailProducer {

    private static final Logger logger = LoggerFactory.getLogger(KafkaEmailProducer.class);
    private final KafkaTemplate<String, String> kafkaTemplate;

    @Value("${booking.kafka.topic}")
    private String topic;

    public KafkaEmailProducer(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendEmail(String message) {
        logger.info("Sending message to Kafka topic '{}': {}", topic, message);
        kafkaTemplate.send(topic, message);
    }
}