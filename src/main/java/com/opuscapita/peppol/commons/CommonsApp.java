package com.opuscapita.peppol.commons;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class CommonsApp implements CommandLineRunner {

    @Override
    public void run(String... strings) throws Exception {
    }

    public static void main(String[] args) {
        SpringApplication.run(CommonsApp.class, args);
    }
}
