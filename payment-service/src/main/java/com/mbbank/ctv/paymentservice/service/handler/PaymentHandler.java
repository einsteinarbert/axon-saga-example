package com.mbbank.ctv.paymentservice.service.handler;


import com.mbbank.ctv.messages.commands.TestCommand;
import org.axonframework.commandhandling.CommandHandler;
import org.springframework.stereotype.Component;

@Component
public class PaymentHandler {
    @CommandHandler
    public void on(TestCommand testCommand) {
        System.out.println("chems");
    }
}
