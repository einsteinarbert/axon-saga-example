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
import org.springframework.util.StringUtils;

import javax.transaction.Transactional;
import java.util.UUID;

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
    @Transactional
    public void on(OrderCreatedEvent event) {
        var order = Orders.builder()
                .orderId(event.orderId)
                .status(event.orderStatus)
                .itemName(event.itemType)
                .price(event.price)
                .build();
        var update = ordersRepo.findByOrderId(event.orderId);
        if (update.isPresent()) return;
        var result = ordersRepo.save(order);
        System.out.println("005: " + event.orderId);
        log.info("Inserting/Update orders: {} {}", event.orderId, event.itemType);
        // TODO commandGateway send event with data taken from 'result' above
        assert commandGateway != null;
        var nextEvent = new OrderCreatedCommand(UUID.randomUUID().toString(), event.itemType, event.price, event.currency, event.orderStatus, result.getId());
        BeanUtils.copyProperties(event, nextEvent);
        nextEvent.setId(result.getId());
        commandGateway.send(nextEvent);
    }

    @EventHandler
    public void on(OrderUpdatedEvent event) {
        System.out.println("016");
        log.info("Update orders: {} {}", event.orderId, event.orderStatus);
        ordersRepo.findByOrderId(event.orderId).ifPresent(orders -> {
            orders.setStatus(event.orderStatus);
            ordersRepo.save(orders);
        });
    }
}
