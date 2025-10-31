package com.rehber;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.rehber")
public class RehberServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(RehberServerApplication.class, args);
    }
}
