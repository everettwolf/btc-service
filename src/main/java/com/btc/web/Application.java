package com.btc.web;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * Created by Eric on 9/12/14.
 */
@Configuration
@EnableAutoConfiguration
@EnableJpaRepositories
@ComponentScan(value = "com.btc.web")
public class Application {

    @Value("${db.auto}")
    private boolean dbAuto;

    public static void main(String[] args) throws Exception {
        SpringApplication application = new SpringApplication(Application.class);
        application.run();
    }

}
