package com.example.microservicecloudeureka7003;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@EnableEurekaServer
@SpringBootApplication
public class MicroservicecloudEureka7003Application {

    public static void main(String[] args) {
        SpringApplication.run(MicroservicecloudEureka7003Application.class, args);
    }

}
