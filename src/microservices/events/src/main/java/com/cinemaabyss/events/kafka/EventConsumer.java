package com.cinemaabyss.events.kafka;

import java.util.concurrent.atomic.AtomicInteger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import com.cinemaabyss.events.model.MovieEvent;
import com.cinemaabyss.events.model.PaymentEvent;
import com.cinemaabyss.events.model.UserEvent;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class EventConsumer
{
    private static final Logger LOG = LoggerFactory.getLogger(EventConsumer.class);

    private final ObjectMapper objectMapper;
    private final AtomicInteger movieEventCount = new AtomicInteger(0);
    private final AtomicInteger userEventCount = new AtomicInteger(0);
    private final AtomicInteger paymentEventCount = new AtomicInteger(0);

    public EventConsumer(ObjectMapper objectMapper)
    {
        this.objectMapper = objectMapper;
    }

    @KafkaListener(topics = "movie-events", groupId = "events-service-group")
    public void consumeMovieEvent(@Payload String message,
            @Header(KafkaHeaders.RECEIVED_TOPIC) String topic,
            @Header(KafkaHeaders.RECEIVED_PARTITION) int partition,
            @Header(KafkaHeaders.OFFSET) long offset)
    {
        try
        {
            MovieEvent event = objectMapper.readValue(message, MovieEvent.class);
            int count = movieEventCount.incrementAndGet();
            LOG.info("[MOVIE EVENT #{}] Consumed from topic={}, partition={}, offset={}, event={}",
                    count, topic, partition, offset, event);

            processMovieEvent(event);
        }
        catch (Exception e)
        {
            LOG.error("Error consuming movie event: {}", e.getMessage(), e);
        }
    }

    @KafkaListener(topics = "user-events", groupId = "events-service-group")
    public void consumeUserEvent(@Payload String message,
            @Header(KafkaHeaders.RECEIVED_TOPIC) String topic,
            @Header(KafkaHeaders.RECEIVED_PARTITION) int partition,
            @Header(KafkaHeaders.OFFSET) long offset)
    {
        try
        {
            UserEvent event = objectMapper.readValue(message, UserEvent.class);
            int count = userEventCount.incrementAndGet();
            LOG.info("[USER EVENT #{}] Consumed from topic={}, partition={}, offset={}, event={}",
                    count, topic, partition, offset, event);

            processUserEvent(event);
        }
        catch (Exception e)
        {
            LOG.error("Error consuming user event: {}", e.getMessage(), e);
        }
    }

    @KafkaListener(topics = "payment-events", groupId = "events-service-group")
    public void consumePaymentEvent(@Payload String message,
            @Header(KafkaHeaders.RECEIVED_TOPIC) String topic,
            @Header(KafkaHeaders.RECEIVED_PARTITION) int partition,
            @Header(KafkaHeaders.OFFSET) long offset)
    {
        try
        {
            PaymentEvent event = objectMapper.readValue(message, PaymentEvent.class);
            int count = paymentEventCount.incrementAndGet();
            LOG.info("[PAYMENT EVENT #{}] Consumed from topic={}, partition={}, offset={}, event={}",
                    count, topic, partition, offset, event);

            processPaymentEvent(event);
        }
        catch (Exception e)
        {
            LOG.error("Error consuming payment event: {}", e.getMessage(), e);
        }
    }

    private static void processMovieEvent(MovieEvent event)
    {
        LOG.info("   → Processing movie event: action={}, movie_id={}, title='{}'",
                event.getAction(), event.getMovieId(), event.getTitle());

        switch (event.getAction())
        {
        case "viewed":
            LOG.info("   → Movie '{}' was viewed by user {}", event.getTitle(), event.getUserId());
            break;
        case "rated":
            LOG.info("   → Movie '{}' was rated with {} by user {}",
                    event.getTitle(), event.getRating(), event.getUserId());
            break;
        case "created":
            LOG.info("   → New movie '{}' was created with ID {}", event.getTitle(), event.getMovieId());
            break;
        case "updated":
            LOG.info("   → Movie '{}' (ID: {}) was updated", event.getTitle(), event.getMovieId());
            break;
        case "deleted":
            LOG.info("   → Movie '{}' (ID: {}) was deleted", event.getTitle(), event.getMovieId());
            break;
        default:
            LOG.info("   → Unknown movie action: {}", event.getAction());
        }
    }

    private static void processUserEvent(UserEvent event)
    {
        LOG.info("   → Processing user event: action={}, user_id={}, username='{}'",
                event.getAction(), event.getUserId(), event.getUsername());

        switch (event.getAction())
        {
        case "registered":
            LOG.info("   → New user '{}' (ID: {}) registered with email {}",
                    event.getUsername(), event.getUserId(), event.getEmail());
            break;
        case "logged_in":
            LOG.info("   → User '{}' (ID: {}) logged in", event.getUsername(), event.getUserId());
            break;
        case "logged_out":
            LOG.info("   → User '{}' (ID: {}) logged out", event.getUsername(), event.getUserId());
            break;
        case "profile_updated":
            LOG.info("   → User '{}' (ID: {}) updated their profile", event.getUsername(), event.getUserId());
            break;
        case "subscribed":
            LOG.info("   → User '{}' (ID: {}) subscribed to a plan", event.getUsername(), event.getUserId());
            break;
        default:
            LOG.info("   → Unknown user action: {}", event.getAction());
        }
    }

    private static void processPaymentEvent(PaymentEvent event)
    {
        LOG.info("   → Processing payment event: status={}, payment_id={}, user_id={}, amount={}",
                event.getStatus(), event.getPaymentId(), event.getUserId(), event.getAmount());

        switch (event.getStatus())
        {
        case "completed":
            LOG.info("   → Payment {} from user {} for ${} completed successfully via {}",
                    event.getPaymentId(), event.getUserId(), event.getAmount(), event.getMethodType());
            break;
        case "pending":
            LOG.info("   → Payment {} from user {} for ${} is pending",
                    event.getPaymentId(), event.getUserId(), event.getAmount());
            break;
        case "failed":
            LOG.info("   → Payment {} from user {} for ${} failed",
                    event.getPaymentId(), event.getUserId(), event.getAmount());
            break;
        case "refunded":
            LOG.info("   → Payment {} from user {} for ${} was refunded",
                    event.getPaymentId(), event.getUserId(), event.getAmount());
            break;
        default:
            LOG.info("   → Unknown payment status: {}", event.getStatus());
        }
    }

    public int getMovieEventCount()
    {
        return movieEventCount.get();
    }

    public int getUserEventCount()
    {
        return userEventCount.get();
    }

    public int getPaymentEventCount()
    {
        return paymentEventCount.get();
    }
}
