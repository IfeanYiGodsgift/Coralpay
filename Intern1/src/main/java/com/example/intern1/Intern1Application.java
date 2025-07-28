package com.example.intern1;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration; // <--- ADD THIS IMPORT
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class}) // <--- MODIFY THIS LINE
@RestController
public class Intern1Application {

    public static void main(String[] args) {
        SpringApplication.run(Intern1Application.class, args);
    }

    @GetMapping("/hello")
    public String hello() {
        return "Hello from Zeus's launchpad!";
    }
}