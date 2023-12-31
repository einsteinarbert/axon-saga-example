package com.mbbank.ctv.orderservice.services.commands;

import com.mbbank.ctv.orderservice.dto.commands.OrderCreateDTO;
import com.mbbank.ctv.orderservice.aggregates.OrderStatus;
import com.mbbank.ctv.messages.commands.CreateOrderCommand;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Service
public class OrderCommandServiceImpl implements OrderCommandService {

    private final CommandGateway commandGateway;

    public OrderCommandServiceImpl(CommandGateway commandGateway) {
        this.commandGateway = commandGateway;
    }

    @Override
    public CompletableFuture<String> createOrder(OrderCreateDTO orderCreateDTO) {
        System.out.println("002");
        return commandGateway.send(new CreateOrderCommand(UUID.randomUUID().toString(), orderCreateDTO.getItemType(),
                orderCreateDTO.getPrice(), orderCreateDTO.getCurrency(), String.valueOf(OrderStatus.CREATED)));
    }

    @Override
    public void sayHello() {
        System.out.println("HELLOOOO");
    }
}
