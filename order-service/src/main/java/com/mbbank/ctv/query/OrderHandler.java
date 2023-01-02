package com.mbbank.ctv.query;

import com.mbbank.ctv.messages.events.OrderCreatedEvent;
import com.mbbank.ctv.messages.events.OrderSaveEvent;
import com.mbbank.ctv.messages.events.OrderUpdatedEvent;
import com.mbbank.ctv.orderservice.dto.entity.Orders;
import com.mbbank.ctv.orderservice.repo.OrdersRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.eventhandling.EventHandler;
import org.axonframework.modelling.saga.SagaLifecycle;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
@Slf4j
public class OrderHandler {
    private final OrdersRepo ordersRepo;

    @EventHandler
    public void on(OrderCreatedEvent orderCreatedEvent) {
        log.info("Inserting/Update orders: {} {}", orderCreatedEvent.orderId, orderCreatedEvent.itemType);
        var order = Orders.builder()
                .orderId(orderCreatedEvent.orderId)
                .status(orderCreatedEvent.orderStatus)
                .itemName(orderCreatedEvent.itemType)
                .price(orderCreatedEvent.price)
                .build();
        var update = ordersRepo.findByOrderId(orderCreatedEvent.orderId).orElse(order);
        ordersRepo.save(update);
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
