package com.progressivecoder.paymentmanagement.paymentservice.messages.events;

public class OrderUpdatedEvent {

    public final String orderId;

    public final String orderStatus;

    public OrderUpdatedEvent(String orderId, String orderStatus) {
        this.orderId = orderId;
        this.orderStatus = orderStatus;
    }
}
