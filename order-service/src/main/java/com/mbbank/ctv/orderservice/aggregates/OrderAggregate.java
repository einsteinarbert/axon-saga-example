package com.mbbank.ctv.orderservice.aggregates;

import com.mbbank.ctv.messages.commands.CreateOrderCommand;
import com.mbbank.ctv.messages.commands.UpdateOrderStatusCommand;
import com.mbbank.ctv.messages.events.OrderCreatedEvent;
import com.mbbank.ctv.messages.commands.OrderCreatedCommand;
import com.mbbank.ctv.messages.events.OrderRecordSavedEvent;
import com.mbbank.ctv.messages.events.OrderUpdatedEvent;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.spring.stereotype.Aggregate;

import java.math.BigDecimal;

import static org.axonframework.modelling.command.AggregateLifecycle.apply;

@Aggregate
public class OrderAggregate {

    @AggregateIdentifier
    private String orderId;

    private ItemType itemType;

    private BigDecimal price;

    private String currency;

    private OrderStatus orderStatus;

    private Integer orderIdDatabase;

    public OrderAggregate() {
    }

    @CommandHandler
    public OrderAggregate(CreateOrderCommand createOrderCommand){
        System.out.println("003");
        AggregateLifecycle.apply(new OrderCreatedEvent(createOrderCommand.orderId, createOrderCommand.itemType,
                createOrderCommand.price, createOrderCommand.currency, createOrderCommand.orderStatus));
    }

    @EventSourcingHandler
    protected void on(OrderCreatedEvent orderCreatedEvent){
        System.out.println("004");
        this.orderId = orderCreatedEvent.orderId;
        this.itemType = ItemType.valueOf(orderCreatedEvent.itemType);
        this.price = orderCreatedEvent.price;
        this.currency = orderCreatedEvent.currency;
        this.orderStatus = OrderStatus.valueOf(orderCreatedEvent.orderStatus);
    }

    @CommandHandler
    public OrderAggregate(OrderCreatedCommand event){
        System.out.println("006");
        AggregateLifecycle.apply(new OrderRecordSavedEvent(event.orderId, event.itemType,
                event.price, event.currency, event.orderStatus, event.id));
    }

    @EventSourcingHandler
    protected void on(OrderRecordSavedEvent event){
        System.out.println("006.1");
        this.orderId = event.orderId;
        this.itemType = ItemType.valueOf(event.itemType);
        this.price = event.price;
        this.currency = event.currency;
        this.orderStatus = OrderStatus.valueOf(event.orderStatus);
        this.orderIdDatabase = event.id;
    }

    @CommandHandler
    protected void on(UpdateOrderStatusCommand updateOrderStatusCommand){
        apply(new OrderUpdatedEvent(updateOrderStatusCommand.orderId, updateOrderStatusCommand.orderStatus));
    }

    @EventSourcingHandler
    protected void on(OrderUpdatedEvent orderUpdatedEvent){
        this.orderId = orderId;
        this.orderStatus = OrderStatus.valueOf(orderUpdatedEvent.orderStatus);
    }
}
