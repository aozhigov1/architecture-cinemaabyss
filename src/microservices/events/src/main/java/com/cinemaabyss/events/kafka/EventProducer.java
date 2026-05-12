package com.cinemaabyss.events.kafka;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.cinemaabyss.events.model.Event;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class EventProducer
{
    private static final Logger LOG = LoggerFactory.getLogger(EventProducer.class);
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    public EventProducer(KafkaTemplate<String, String> kafkaTemplate, ObjectMapper objectMapper)
    {
        this.kafkaTemplate = kafkaTemplate;
        this.objectMapper = objectMapper;
    }

    private void sendEvent(String topic, Event event)
    {
        try
        {
            String jsonEvent = objectMapper.writeValueAsString(event);
            LOG.info("Sending event to topic {}: {}", topic, jsonEvent);

            var sendResult = kafkaTemplate.send(topic, event.getId(), jsonEvent);

            sendResult.whenComplete((result, ex) ->
            {
                if (ex == null)
                {
                    LOG.info("Event sent successfully to topic {}, partition={}, offset={}",
                            topic, result.getRecordMetadata().partition(), result.getRecordMetadata().offset());
                }
                else
                {
                    LOG.error("Failed to send event to topic {}: {}", topic, ex.getMessage());
                }
            });
        }
        catch (Exception e)
        {
            LOG.error("Error serializing event: {}", e.getMessage());
            throw new RuntimeException("Failed to send event", e);
        }
    }

    public void sendMovieEvent(Event event)
    {
        sendEvent("movie-events", event);
    }

    public void sendUserEvent(Event event)
    {
        sendEvent("user-events", event);
    }

    public void sendPaymentEvent(Event event)
    {
        sendEvent("payment-events", event);
    }
}
