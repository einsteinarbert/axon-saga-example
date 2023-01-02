package com.mbbank.ctv.orderservice.sagas;

import com.mbbank.ctv.messages.commands.CreateInvoiceCommand;
import com.mbbank.ctv.messages.commands.UpdateOrderStatusCommand;
import com.mbbank.ctv.messages.events.OrderCreatedEvent;
import com.mbbank.ctv.messages.events.OrderShippedEvent;
import com.mbbank.ctv.messages.events.OrderUpdatedEvent;
import com.mbbank.ctv.orderservice.aggregates.OrderStatus;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.modelling.saga.SagaEventHandler;
import org.axonframework.modelling.saga.SagaLifecycle;
import org.axonframework.modelling.saga.StartSaga;
import org.axonframework.spring.stereotype.Saga;

import javax.inject.Inject;
import java.util.UUID;

@Saga
public class SagaManagement {

    @Inject
    private transient CommandGateway commandGateway;

    @StartSaga
    @SagaEventHandler(associationProperty = "orderId")
    public void handle(OrderCreatedEvent orderCreatedEvent){
        String paymentId = UUID.randomUUID().toString();
        System.out.println("Saga invoked");

        //associate Saga
        SagaLifecycle.associateWith("paymentId", paymentId);

        System.out.println("order id" + orderCreatedEvent.orderId);
        commandGateway.send(new CreateInvoiceCommand(paymentId, orderCreatedEvent.orderId));
    }

    @SagaEventHandler(associationProperty = "orderId")
    public void handle(OrderShippedEvent orderShippedEvent){
        if (orderShippedEvent.orderId.length() > 5) {
            // throw new IllegalArgumentException("simulator test rollback");
        }
        commandGateway.send(new UpdateOrderStatusCommand(orderShippedEvent.orderId, String.valueOf(OrderStatus.SHIPPED)));
    }

    @SagaEventHandler(associationProperty = "orderId")
    public void handle(OrderUpdatedEvent orderUpdatedEvent){
        SagaLifecycle.end();
    }
}
