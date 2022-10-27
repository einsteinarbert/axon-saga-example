package com.mbbank.ctv.orderservice.services.commands;

import com.mbbank.ctv.orderservice.dto.commands.OrderCreateDTO;

import java.util.concurrent.CompletableFuture;

public interface OrderCommandService {

    public CompletableFuture<String> createOrder(OrderCreateDTO orderCreateDTO);

}
