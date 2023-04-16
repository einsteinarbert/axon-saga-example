package com.mbbank.ctv.shippingservice.aggregates;

import com.mbbank.ctv.error.GiftCardBusinessErrorCode;
import com.mbbank.ctv.exception.GiftCardException;
import com.mbbank.ctv.messages.commands.CreateShippingCommand;
import com.mbbank.ctv.messages.events.OrderShippedEvent;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.spring.stereotype.Aggregate;

@Aggregate
public class ShippingAggregate {

    @AggregateIdentifier
    private String shippingId;

    private String orderId;

    private String paymentId;

    public ShippingAggregate() {
    }

    @CommandHandler
    public ShippingAggregate(CreateShippingCommand createShippingCommand){
        System.out.println("011");
        AggregateLifecycle.apply(new OrderShippedEvent(createShippingCommand.shippingId, createShippingCommand.orderId,
                createShippingCommand.paymentId, createShippingCommand.itemType));
    }

    @EventSourcingHandler
    protected void on(OrderShippedEvent orderShippedEvent){
        System.out.println("012");
        this.shippingId = orderShippedEvent.shippingId;
        this.orderId = orderShippedEvent.orderId;
        // throw random exception
        if ("SEXTOY".equalsIgnoreCase(orderShippedEvent.itemType)) {
            System.out.println("sextoy exception throw situation");
            throw new GiftCardException("The order item: " + orderShippedEvent.itemType
                    + " in Tokuda inventory is not supported :))", GiftCardBusinessErrorCode.UNKNOWN);
        }
    }
}
