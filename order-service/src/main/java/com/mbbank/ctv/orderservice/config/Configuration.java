package com.mbbank.ctv.orderservice.config;

import com.mbbank.ctv.query.OrderHandler;
import lombok.RequiredArgsConstructor;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.config.Configurer;
import org.axonframework.springboot.autoconfig.InfraConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@org.springframework.context.annotation.Configuration
@ComponentScan("com.mbbank.ctv.query")
@EnableJpaRepositories(basePackages = "com.mbbank.ctv.orderservice.repo")
@RequiredArgsConstructor
@AutoConfigureAfter({InfraConfiguration.class})
@ConditionalOnBean({InfraConfiguration.class})
public class Configuration {
    private final CommandGateway commandGateway;

    @Bean
    @Autowired
    @Primary
    public Configurer configure(Configurer config, OrderHandler orderHandler) {
        orderHandler.setCommandGateway(commandGateway);
        config.registerEventHandler(configuration -> orderHandler);
        return config;
    }
}
