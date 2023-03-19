package com.mbbank.ctv.query;

import com.mbbank.ctv.messages.events.OrderCreatedEvent;
import com.mbbank.ctv.messages.commands.OrderCreatedCommand;
import com.mbbank.ctv.messages.events.OrderUpdatedEvent;
import com.mbbank.ctv.orderservice.dto.entity.Orders;
import com.mbbank.ctv.orderservice.repo.OrdersRepo;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.eventhandling.EventHandler;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
@Setter
public class OrderHandler {
    private final OrdersRepo ordersRepo;
    @Lazy
    @Autowired
    private CommandGateway commandGateway;

    @EventHandler
    public void on(OrderCreatedEvent event) {
        System.out.println("005");
        log.info("Inserting/Update orders: {} {}", event.orderId, event.itemType);
        var order = Orders.builder()
                .orderId(event.orderId)
                .status(event.orderStatus)
                .itemName(event.itemType)
                .price(event.price)
                .build();
        var update = ordersRepo.findByOrderId(event.orderId).orElse(order);
        var result = ordersRepo.save(update);
        // TODO commandGateway send event with data taking from 'result' above
        assert commandGateway != null;
        var nextEvent = new OrderCreatedCommand();
        BeanUtils.copyProperties(event, nextEvent);
        nextEvent.setId(result.getId());
        commandGateway.send(nextEvent);
    }

    @EventHandler
    public void on(OrderUpdatedEvent event) {
        log.info("Update orders: {} {}", event.orderId, event.orderStatus);
        ordersRepo.findByOrderId(event.orderId).ifPresent(orders -> {
            orders.setStatus(event.orderStatus);
            ordersRepo.save(orders);
        });
    }
}
