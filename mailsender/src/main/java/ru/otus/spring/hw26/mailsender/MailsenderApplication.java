package ru.otus.spring.hw26.mailsender;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
public class MailsenderApplication {

    public static void main(String[] args) {
        SpringApplication.run(MailsenderApplication.class, args);
    }

}
