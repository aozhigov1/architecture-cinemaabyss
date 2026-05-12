package com.cinemaabyss.events.controller.request;

import java.time.LocalDateTime;


public class PaymentEventRequest
{
    private Integer paymentId;
    private Integer userId;
    private Double amount;
    private String status;
    private LocalDateTime timestamp;
    private String methodType;

    public Integer getPaymentId()
    {
        return paymentId;
    }

    public void setPaymentId(Integer paymentId)
    {
        this.paymentId = paymentId;
    }

    public Integer getUserId()
    {
        return userId;
    }

    public void setUserId(Integer userId)
    {
        this.userId = userId;
    }

    public Double getAmount()
    {
        return amount;
    }

    public void setAmount(Double amount)
    {
        this.amount = amount;
    }

    public String getStatus()
    {
        return status;
    }

    public void setStatus(String status)
    {
        this.status = status;
    }

    public LocalDateTime getTimestamp()
    {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp)
    {
        this.timestamp = timestamp;
    }

    public String getMethodType()
    {
        return methodType;
    }

    public void setMethodType(String methodType)
    {
        this.methodType = methodType;
    }

    @Override
    public String toString()
    {
        return "PaymentEventRequest{paymentId=" + paymentId + ", userId=" + userId + ", amount=" + amount
                + ", status='" + status + "'}";
    }
}