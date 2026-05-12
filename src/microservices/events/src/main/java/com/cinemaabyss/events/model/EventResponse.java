package com.cinemaabyss.events.model;

import java.util.Objects;

public class EventResponse
{
    private String status;
    private Integer partition;
    private Long offset;
    private Event event;

    public EventResponse(String status, Integer partition, Long offset, Event event)
    {
        this.status = status;
        this.partition = partition;
        this.offset = offset;
        this.event = event;
    }

    public EventResponse()
    {
    }

    public String getStatus()
    {
        return status;
    }

    public EventResponse setStatus(String status)
    {
        this.status = status;
        return this;
    }

    public Integer getPartition()
    {
        return partition;
    }

    public EventResponse setPartition(Integer partition)
    {
        this.partition = partition;
        return this;
    }

    public Long getOffset()
    {
        return offset;
    }

    public EventResponse setOffset(Long offset)
    {
        this.offset = offset;
        return this;
    }

    public Event getEvent()
    {
        return event;
    }

    public EventResponse setEvent(Event event)
    {
        this.event = event;
        return this;
    }

    @Override
    public boolean equals(Object o)
    {
        if (!(o instanceof EventResponse that))
        {
            return false;
        }
        return Objects.equals(status, that.status) && Objects.equals(partition, that.partition)
                && Objects.equals(offset, that.offset) && Objects.equals(event, that.event);
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(status, partition, offset, event);
    }
}
