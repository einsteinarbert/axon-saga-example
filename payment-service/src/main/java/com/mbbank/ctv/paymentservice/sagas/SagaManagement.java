package com.mbbank.ctv.paymentservice.sagas;

import com.mbbank.ctv.messages.commands.CreateShippingCommand;
import com.mbbank.ctv.messages.events.InvoiceCreatedEvent;
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


    @SagaEventHandler(associationProperty = "paymentId")
    @StartSaga
    @Transactional
    public void handle(InvoiceCreatedEvent invoiceCreatedEvent){
        String shippingId = UUID.randomUUID().toString();

        System.out.println("Saga continued");

        //associate Saga with shipping
        SagaLifecycle.associateWith("shipping", shippingId);

        //send the create shipping command
        commandGateway.send(new CreateShippingCommand(shippingId, invoiceCreatedEvent.orderId, invoiceCreatedEvent.paymentId, invoiceCreatedEvent.itemType));
    }
}
