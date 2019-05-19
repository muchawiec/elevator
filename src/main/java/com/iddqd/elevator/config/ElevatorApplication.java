package com.iddqd.elevator.config;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * Preconfigured Spring Application boot class.
 *
 */
@Configuration
@ComponentScan(basePackages = { "com.iddqd.elevator" })
@EnableAutoConfiguration
@PropertySources({ @PropertySource("classpath:application.properties") })
@EnableAsync
public class ElevatorApplication {

    @Value("${com.iddqd.elevator.numberofelevators}")
    private int numberOfElevators;
    

    /**
     * Start method that will be invoked when starting the Spring context.
     *
     * @param args
     *            Not in use
     */
    public static void main(final String[] args) {
        SpringApplication.run(ElevatorApplication.class, args);
    }

    /**
     * Create a default thread pool for your convenience.
     *
     * @return Executor thread pool
     */
    @Bean(name = "asyncExecutor", destroyMethod = "shutdown")
    public Executor taskExecutor() {
        return Executors.newScheduledThreadPool(numberOfElevators);
    }

}
