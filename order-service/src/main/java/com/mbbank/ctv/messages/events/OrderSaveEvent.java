package com.mbbank.ctv.messages.events;

import com.mbbank.ctv.orderservice.dto.entity.Orders;
import lombok.Data;

@Data
public class OrderSaveEvent {
    Orders order;
    public OrderSaveEvent(Orders o) {
        this.order = o;
    }
}
