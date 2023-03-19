package com.mbbank.ctv.shippingservice.aggregates;

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
        AggregateLifecycle.apply(new OrderShippedEvent(createShippingCommand.shippingId, createShippingCommand.orderId,
                createShippingCommand.paymentId, createShippingCommand.itemType));
    }

    @EventSourcingHandler
    protected void on(OrderShippedEvent orderShippedEvent){
        this.shippingId = orderShippedEvent.shippingId;
        this.orderId = orderShippedEvent.orderId;
        // throw random exception
        if ("sextoy".equalsIgnoreCase(orderShippedEvent.itemType)) {
            throw new UnsupportedOperationException("The order item: " + orderShippedEvent.itemType + " in Tokuda inventory is not supported :))");
        }
    }
}
