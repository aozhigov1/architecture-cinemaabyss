package com.cinemaabyss.events.controller.request;

import java.time.LocalDateTime;

public class UserEventRequest
{
    private Integer userId;
    private String username;
    private String email;
    private String action;
    private LocalDateTime timestamp;

    public Integer getUserId()
    {
        return userId;
    }

    public void setUserId(Integer userId)
    {
        this.userId = userId;
    }

    public String getUsername()
    {
        return username;
    }

    public void setUsername(String username)
    {
        this.username = username;
    }

    public String getEmail()
    {
        return email;
    }

    public void setEmail(String email)
    {
        this.email = email;
    }

    public String getAction()
    {
        return action;
    }

    public void setAction(String action)
    {
        this.action = action;
    }

    public LocalDateTime getTimestamp()
    {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp)
    {
        this.timestamp = timestamp;
    }

    @Override
    public String toString()
    {
        return "UserEventRequest{userId=" + userId + ", username='" + username + "', action='" + action + "'}";
    }
}