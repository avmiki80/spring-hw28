package ru.otus.spring.hw26.moderator;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.integration.annotation.IntegrationComponentScan;

@SpringBootApplication
@IntegrationComponentScan({
        "ru.otus.spring.hw26.moderator.event.moderate.publish"
})
@EnableEurekaClient
public class ModeratorApplication {
    public static void main(String[] args) {
        SpringApplication.run(ModeratorApplication.class, args);
        System.out.println("KAFKA_URL from env: " + System.getenv("KAFKA_URL"));
    }

}
