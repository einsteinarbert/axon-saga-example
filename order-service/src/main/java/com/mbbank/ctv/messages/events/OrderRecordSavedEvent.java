package com.mbbank.ctv.messages.events;

import java.math.BigDecimal;

public class OrderRecordSavedEvent {

    public final String orderId;

    public final String itemType;

    public final BigDecimal price;

    public final String currency;

    public final String orderStatus;
    public final Integer id;

    public OrderRecordSavedEvent(String orderId, String itemType, BigDecimal price, String currency, String orderStatus, Integer id) {
        this.orderId = orderId;
        this.itemType = itemType;
        this.price = price;
        this.currency = currency;
        this.orderStatus = orderStatus;
        this.id = id;
    }
}
