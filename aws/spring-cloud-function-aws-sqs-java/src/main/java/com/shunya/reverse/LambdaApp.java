package com.shunya.reverse;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class LambdaApp {

    public static void main(String[] args) throws Exception {
        System.out.println("intit spring");
        SpringApplication.run(LambdaApp.class, args);
        System.out.println("sprint intialized");
    }
}
