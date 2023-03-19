package com.mbbank.ctv.messages.commands;

import org.axonframework.modelling.command.TargetAggregateIdentifier;

public class CreateShippingCommand {

    @TargetAggregateIdentifier
    public final String shippingId;

    public final String orderId;

    public final String paymentId;
    public final String itemType;

    public CreateShippingCommand(String shippingId, String orderId, String paymentId, String itemType) {
        this.shippingId = shippingId;
        this.orderId = orderId;
        this.paymentId = paymentId;
        this.itemType = itemType;
    }
}
