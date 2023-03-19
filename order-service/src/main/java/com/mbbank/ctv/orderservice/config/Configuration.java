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

/*    @Bean
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
    }*/
}
