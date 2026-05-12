package com.cinemaabyss.events.model;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "eventType"
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = MovieEvent.class, name = "movie"),
        @JsonSubTypes.Type(value = UserEvent.class, name = "user"),
        @JsonSubTypes.Type(value = PaymentEvent.class, name = "payment")
})
public abstract class Event
{
    protected String id;
    protected String eventType;
    protected LocalDateTime timestamp;
    protected Map<String, Object> payload;

    public Event(String id, Map<String, Object> payload, LocalDateTime timestamp, String eventType)
    {
        this.id = id;
        this.payload = payload;
        this.timestamp = timestamp;
        this.eventType = eventType;
    }

    public Event()
    {
    }

    protected static String generateId(String type, Integer id)
    {
        return type + "-" + id + "-" + System.currentTimeMillis();
    }

    public String getId()
    {
        return id;
    }

    public Event setId(String id)
    {
        this.id = id;
        return this;
    }

    public String getEventType()
    {
        return eventType;
    }

    public Event setEventType(String eventType)
    {
        this.eventType = eventType;
        return this;
    }

    public LocalDateTime getTimestamp()
    {
        return timestamp;
    }

    public Event setTimestamp(LocalDateTime timestamp)
    {
        this.timestamp = timestamp;
        return this;
    }

    public Map<String, Object> getPayload()
    {
        return payload;
    }

    public Event setPayload(Map<String, Object> payload)
    {
        this.payload = payload;
        return this;
    }

    @Override
    public boolean equals(Object o)
    {
        if (!(o instanceof Event event))
        {
            return false;
        }
        return Objects.equals(id, event.id) && Objects.equals(eventType, event.eventType)
                && Objects.equals(timestamp, event.timestamp) && Objects.equals(payload, event.payload);
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(id, eventType, timestamp, payload);
    }
}
