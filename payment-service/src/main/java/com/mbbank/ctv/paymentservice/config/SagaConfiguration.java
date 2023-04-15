package com.mbbank.ctv.paymentservice.config;

import com.mbbank.ctv.paymentservice.service.handler.PaymentHandler;
import org.axonframework.common.jpa.EntityManagerProvider;
import org.axonframework.common.transaction.TransactionManager;
import org.axonframework.config.Configurer;
import org.axonframework.eventsourcing.eventstore.EventStorageEngine;
import org.axonframework.eventsourcing.eventstore.jpa.JpaEventStorageEngine;
import org.axonframework.modelling.saga.repository.SagaStore;
import org.axonframework.modelling.saga.repository.jpa.JpaSagaStore;
import org.axonframework.springboot.autoconfig.InfraConfiguration;
import org.axonframework.springboot.util.jpa.ContainerManagedEntityManagerProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

import javax.persistence.EntityManagerFactory;

@Configuration
@ComponentScan("com.mbbank.ctv.paymentservice.service.handler")
@EnableJpaRepositories(basePackages = "com.mbbank.ctv.paymentservice.repo")
@AutoConfigureAfter({InfraConfiguration.class}) // ignore circle dependency error
@ConditionalOnBean({InfraConfiguration.class}) // ignore circle dependency error
public class SagaConfiguration {

    @Bean
    @Autowired
    @Primary
    public Configurer configure(Configurer config, PaymentHandler paymentHandler) {
        config.registerEventHandler(configuration -> paymentHandler);
//        config.configureAggregate(PartyAggregate.class);
        return config;
    }

    @Bean
    public PlatformTransactionManager transactionManager(EntityManagerFactory emf) {
        return new JpaTransactionManager(emf);
    }

    @Bean
    public EventStorageEngine eventStorageEngine(EntityManagerProvider entityManagerProvider) {
        return JpaEventStorageEngine.builder()
                .entityManagerProvider(entityManagerProvider)
                .transactionManager((TransactionManager) transactionManager(entityManagerProvider.getEntityManager().getEntityManagerFactory()))
                .build();
    }

    @Bean
    public SagaStore sagaStore(EntityManagerProvider entityManagerProvider) {
        return JpaSagaStore.builder()
                .entityManagerProvider(entityManagerProvider)
                .build();
    }

    @Bean
    public EntityManagerProvider entityManagerProvider() {
        return new ContainerManagedEntityManagerProvider();
    }
}
