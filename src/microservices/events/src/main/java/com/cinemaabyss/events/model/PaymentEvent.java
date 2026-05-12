package com.cinemaabyss.events.model;

import java.time.LocalDateTime;
import java.util.Objects;

public class PaymentEvent extends Event
{
    private Integer paymentId;
    private Integer userId;
    private Double amount;
    private String status;
    private LocalDateTime paymentTimestamp;
    private String methodType;

    public PaymentEvent()
    {
        super();
    }

    public PaymentEvent(Integer paymentId, Integer userId, Double amount, String status, LocalDateTime paymentTimestamp,
            String methodType)
    {
        super();
        this.paymentId = paymentId;
        this.userId = userId;
        this.amount = amount;
        this.status = status;
        this.paymentTimestamp = paymentTimestamp;
        this.methodType = methodType;
        this.id = generateId("payment", paymentId);
        this.eventType = "payment";
        this.timestamp = java.time.LocalDateTime.now();
    }

    public Integer getPaymentId()
    {
        return paymentId;
    }

    public PaymentEvent setPaymentId(Integer paymentId)
    {
        this.paymentId = paymentId;
        return this;
    }

    public Integer getUserId()
    {
        return userId;
    }

    public PaymentEvent setUserId(Integer userId)
    {
        this.userId = userId;
        return this;
    }

    public Double getAmount()
    {
        return amount;
    }

    public PaymentEvent setAmount(Double amount)
    {
        this.amount = amount;
        return this;
    }

    public String getStatus()
    {
        return status;
    }

    public PaymentEvent setStatus(String status)
    {
        this.status = status;
        return this;
    }

    public LocalDateTime getPaymentTimestamp()
    {
        return paymentTimestamp;
    }

    public PaymentEvent setPaymentTimestamp(LocalDateTime paymentTimestamp)
    {
        this.paymentTimestamp = paymentTimestamp;
        return this;
    }

    public String getMethodType()
    {
        return methodType;
    }

    public PaymentEvent setMethodType(String methodType)
    {
        this.methodType = methodType;
        return this;
    }

    @Override
    public boolean equals(Object o)
    {
        if (!(o instanceof PaymentEvent that))
        {
            return false;
        }
        if (!super.equals(o))
        {
            return false;
        }
        return Objects.equals(paymentId, that.paymentId) && Objects.equals(userId, that.userId)
                && Objects.equals(amount, that.amount) && Objects.equals(status, that.status)
                && Objects.equals(paymentTimestamp, that.paymentTimestamp) && Objects.equals(methodType,
                that.methodType);
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), paymentId, userId, amount, status, paymentTimestamp, methodType);
    }
}
