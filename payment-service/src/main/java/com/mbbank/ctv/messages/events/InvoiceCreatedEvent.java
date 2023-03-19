package com.mbbank.ctv.messages.events;

public class InvoiceCreatedEvent  {

    public final String paymentId;

    public final String orderId;
    public final String itemType;

    public InvoiceCreatedEvent(String paymentId, String orderId, String itemType) {
        this.paymentId = paymentId;
        this.orderId = orderId;
        this.itemType = itemType;
    }
}
