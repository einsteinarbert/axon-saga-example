package com.mbbank.ctv.orderservice.sagas;

import com.mbbank.ctv.messages.commands.CreateInvoiceCommand;
import com.mbbank.ctv.messages.commands.UpdateOrderStatusCommand;
import com.mbbank.ctv.messages.events.*;
import com.mbbank.ctv.orderservice.aggregates.OrderStatus;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.modelling.saga.SagaEventHandler;
import org.axonframework.modelling.saga.SagaLifecycle;
import org.axonframework.modelling.saga.StartSaga;
import org.axonframework.spring.stereotype.Saga;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.util.UUID;

@Saga
public class SagaManagement {
    @Inject
    private transient CommandGateway commandGateway;

    @StartSaga
    @SagaEventHandler(associationProperty = "orderId")
    @Transactional
    public void handle(OrderRecordSavedEvent event) {
        System.out.println("007");
        String paymentId = UUID.randomUUID().toString();
        System.out.println("Saga invoked");

        //associate Saga
        SagaLifecycle.associateWith("paymentId", paymentId);

        System.out.println("order id" + event.orderId);
        commandGateway.send(new CreateInvoiceCommand(paymentId, event.orderId, event.itemType));
    }

    @SagaEventHandler(associationProperty = "orderId")
    public void handle(OrderShippedEvent orderShippedEvent){
        System.out.println("013");
        commandGateway.send(new UpdateOrderStatusCommand(orderShippedEvent.orderId, String.valueOf(OrderStatus.SHIPPED)));
    }

    @SagaEventHandler(associationProperty = "orderId")
    public void handle(OrderUpdatedEvent orderUpdatedEvent){
        System.out.println("END---------------017---------------END");
        SagaLifecycle.end();
    }
}
