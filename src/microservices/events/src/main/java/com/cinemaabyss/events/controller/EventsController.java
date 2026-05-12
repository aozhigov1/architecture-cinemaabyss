package com.cinemaabyss.events.controller;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cinemaabyss.events.controller.request.MovieEventRequest;
import com.cinemaabyss.events.controller.request.PaymentEventRequest;
import com.cinemaabyss.events.controller.request.UserEventRequest;
import com.cinemaabyss.events.kafka.EventConsumer;
import com.cinemaabyss.events.kafka.EventProducer;
import com.cinemaabyss.events.model.EventResponse;
import com.cinemaabyss.events.model.MovieEvent;
import com.cinemaabyss.events.model.PaymentEvent;
import com.cinemaabyss.events.model.UserEvent;

@RestController
@RequestMapping("/api/events")
public class EventsController
{
    private static final Logger LOG = LoggerFactory.getLogger(EventsController.class);
    private final EventProducer eventProducer;
    private final EventConsumer eventConsumer;

    public EventsController(EventProducer eventProducer, EventConsumer eventConsumer)
    {
        this.eventProducer = eventProducer;
        this.eventConsumer = eventConsumer;
    }

    @GetMapping("/health")
    public ResponseEntity<Map<String, Object>> health()
    {
        Map<String, Object> response = new HashMap<>();
        response.put("status", true);
        response.put("service", "events-service");
        response.put("kafka", "connected");
        response.put("statistics", Map.of(
                "movieEvents", eventConsumer.getMovieEventCount(),
                "userEvents", eventConsumer.getUserEventCount(),
                "paymentEvents", eventConsumer.getPaymentEventCount()
        ));
        return ResponseEntity.ok(response);
    }

    @PostMapping("/movie")
    public ResponseEntity<EventResponse> createMovieEvent(@RequestBody MovieEventRequest request)
    {
        try
        {
            LOG.info("Received movie event request: {}", request);

            MovieEvent event = new MovieEvent(
                    request.getMovieId(),
                    request.getTitle(),
                    request.getAction(),
                    request.getUserId(),
                    request.getRating(),
                    request.getGenres(),
                    request.getDescription()
            );

            eventProducer.sendMovieEvent(event);

            EventResponse response = new EventResponse()
                    .setStatus("success")
                    .setPartition(0)
                    .setOffset(0L)
                    .setEvent(event);

            LOG.info("Movie event created and sent to Kafka: {}", event.getId());
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        }
        catch (Exception e)
        {
            LOG.error("Error creating movie event: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new EventResponse()
                            .setStatus("error")
                            .setEvent(null));
        }
    }

    @PostMapping("/user")
    public ResponseEntity<EventResponse> createUserEvent(@RequestBody UserEventRequest request)
    {
        try
        {
            LOG.info("Received user event request: {}", request);

            UserEvent event = new UserEvent(
                    request.getUserId(),
                    request.getUsername(),
                    request.getEmail(),
                    request.getAction(),
                    request.getTimestamp() != null ? request.getTimestamp() : LocalDateTime.now()
            );

            eventProducer.sendUserEvent(event);

            EventResponse response = new EventResponse()
                    .setStatus("success")
                    .setPartition(0)
                    .setOffset(0L)
                    .setEvent(event);

            LOG.info("User event created and sent to Kafka: {}", event.getId());
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        }
        catch (Exception e)
        {
            LOG.error("Error creating user event: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new EventResponse()
                            .setStatus("error")
                            .setEvent(null));
        }
    }

    @PostMapping("/payment")
    public ResponseEntity<EventResponse> createPaymentEvent(@RequestBody PaymentEventRequest request)
    {
        try
        {
            LOG.info("Received payment event request: {}", request);

            PaymentEvent event = new PaymentEvent(
                    request.getPaymentId(),
                    request.getUserId(),
                    request.getAmount(),
                    request.getStatus(),
                    request.getTimestamp() != null ? request.getTimestamp() : LocalDateTime.now(),
                    request.getMethodType()
            );

            eventProducer.sendPaymentEvent(event);

            EventResponse response = new EventResponse()
                    .setStatus("success")
                    .setPartition(0)
                    .setOffset(0L)
                    .setEvent(event);

            LOG.info("Payment event created and sent to Kafka: {}", event.getId());
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        }
        catch (Exception e)
        {
            LOG.error("Error creating payment event: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new EventResponse()
                            .setStatus("error")
                            .setEvent(null));
        }
    }
}
