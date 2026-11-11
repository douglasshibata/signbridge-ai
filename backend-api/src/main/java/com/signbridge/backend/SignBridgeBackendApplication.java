package com.signbridge.backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class SignBridgeBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(SignBridgeBackendApplication.class, args);
    }
}
