package com.mbbank.ctv.shippingservice.config;

import org.axonframework.common.jpa.EntityManagerProvider;
import org.axonframework.common.transaction.TransactionManager;
import org.axonframework.eventsourcing.eventstore.EventStorageEngine;
import org.axonframework.eventsourcing.eventstore.jpa.JpaEventStorageEngine;
import org.axonframework.modelling.saga.repository.SagaStore;
import org.axonframework.modelling.saga.repository.jpa.JpaSagaStore;
import org.axonframework.springboot.autoconfig.InfraConfiguration;
import org.axonframework.springboot.util.jpa.ContainerManagedEntityManagerProvider;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

import javax.persistence.EntityManagerFactory;

@Configuration
@AutoConfigureAfter({InfraConfiguration.class}) // ignore circle dependency error
@ConditionalOnBean({InfraConfiguration.class}) // ignore circle dependency error
public class SagaConfiguration {

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
