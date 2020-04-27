package com.example.springcloud;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@EnableEurekaServer
@SpringBootApplication
public class MicroservicecloudEureka7001Application {

    public static void main(String[] args) {
        SpringApplication.run(MicroservicecloudEureka7001Application.class, args);
    }

}
