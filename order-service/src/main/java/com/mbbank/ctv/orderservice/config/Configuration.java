package com.mbbank.ctv.orderservice.config;

import com.mbbank.ctv.query.OrderHandler;
import lombok.RequiredArgsConstructor;
import org.axonframework.config.Configurer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@org.springframework.context.annotation.Configuration
@ComponentScan("com.mbbank.ctv.query")
@EnableJpaRepositories(basePackages = "com.mbbank.ctv.orderservice.repo")
@RequiredArgsConstructor
public class Configuration {

    @Bean
    @Autowired
    @Primary
    public Configurer configure(Configurer config, OrderHandler orderHandler) {
        config.registerEventHandler(configuration -> orderHandler);
        config.registerCommandHandler(configuration -> orderHandler); // for handling command
        return config;
    }
}
