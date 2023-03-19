package com.mbbank.ctv.messages.commands;

import org.axonframework.modelling.command.TargetAggregateIdentifier;

public class CreateInvoiceCommand{

    @TargetAggregateIdentifier
    public final String paymentId;

    public final String orderId;
    public final String itemType;

    public CreateInvoiceCommand(String paymentId, String orderId, String itemType) {
        this.paymentId = paymentId;
        this.orderId = orderId;
        this.itemType = itemType;
    }
}
