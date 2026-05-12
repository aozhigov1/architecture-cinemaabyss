package com.cinemaabyss.events.model;

import java.time.LocalDateTime;
import java.util.Objects;


public class UserEvent extends Event
{
    private Integer userId;
    private String username;
    private String email;
    private String action;
    private LocalDateTime eventTimestamp;

    public UserEvent()
    {
        super();
    }

    public UserEvent(Integer userId, String username, String email, String action, LocalDateTime eventTimestamp)
    {
        super();
        this.userId = userId;
        this.username = username;
        this.email = email;
        this.action = action;
        this.eventTimestamp = eventTimestamp;
        this.id = generateId("user", userId);
        this.eventType = "user";
        this.timestamp = java.time.LocalDateTime.now();
    }

    public Integer getUserId()
    {
        return userId;
    }

    public UserEvent setUserId(Integer userId)
    {
        this.userId = userId;
        return this;
    }

    public String getUsername()
    {
        return username;
    }

    public UserEvent setUsername(String username)
    {
        this.username = username;
        return this;
    }

    public String getEmail()
    {
        return email;
    }

    public UserEvent setEmail(String email)
    {
        this.email = email;
        return this;
    }

    public String getAction()
    {
        return action;
    }

    public UserEvent setAction(String action)
    {
        this.action = action;
        return this;
    }

    public LocalDateTime getEventTimestamp()
    {
        return eventTimestamp;
    }

    public UserEvent setEventTimestamp(LocalDateTime eventTimestamp)
    {
        this.eventTimestamp = eventTimestamp;
        return this;
    }

    @Override
    public boolean equals(Object o)
    {
        if (!(o instanceof UserEvent userEvent))
        {
            return false;
        }
        if (!super.equals(o))
        {
            return false;
        }
        return Objects.equals(userId, userEvent.userId) && Objects.equals(username, userEvent.username)
                && Objects.equals(email, userEvent.email) && Objects.equals(action, userEvent.action)
                && Objects.equals(eventTimestamp, userEvent.eventTimestamp);
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), userId, username, email, action, eventTimestamp);
    }
}
