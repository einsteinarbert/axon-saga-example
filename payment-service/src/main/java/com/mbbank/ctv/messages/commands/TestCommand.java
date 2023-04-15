package com.mbbank.ctv.messages.commands;

import org.axonframework.modelling.command.TargetAggregateIdentifier;

public class TestCommand {
    @TargetAggregateIdentifier
    public final String commandId;

    public TestCommand(String commandId) {
        this.commandId = commandId;
    }
}
