package com.mbbank.ctv.messages.commands;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.axonframework.modelling.command.TargetAggregateIdentifier;

import java.math.BigDecimal;


@RequiredArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class OrderCreatedCommand {
    @TargetAggregateIdentifier
    public String orderId;

    public String itemType;

    public BigDecimal price;

    public String currency;

    public String orderStatus;
    public Integer id;
}
