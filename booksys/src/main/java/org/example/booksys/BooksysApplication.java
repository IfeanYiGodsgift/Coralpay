package org.example.booksys;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class BooksysApplication {

    public static void main(String[] args) {
        SpringApplication.run(BooksysApplication.class, args);
    }

}
