package edu.ptit.da2020;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class Da2020Application implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(Da2020Application.class, args);
    }

    @Override
    public void run(String... args) throws Exception {

    }
}
