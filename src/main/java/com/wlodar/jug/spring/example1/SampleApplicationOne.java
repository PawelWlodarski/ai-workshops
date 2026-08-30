package com.wlodar.jug.spring.example1;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SampleApplicationOne {

    static void main(String[] args) {
        System.out.println("Starting Spring Boot Application One");
        SpringApplication.run(SampleApplicationOne.class);
        System.out.println("Spring Boot Application One started");
    }
}
