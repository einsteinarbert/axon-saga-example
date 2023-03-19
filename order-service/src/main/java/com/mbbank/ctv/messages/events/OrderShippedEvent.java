package com.mbbank.ctv.messages.events;

public class OrderShippedEvent {

    public final String shippingId;

    public final String orderId;

    public final String paymentId;
    public final String itemType;

    public OrderShippedEvent(String shippingId, String orderId, String paymentId, String itemType) {
        this.shippingId = shippingId;
        this.orderId = orderId;
        this.paymentId = paymentId;
        this.itemType = itemType;
    }
}
