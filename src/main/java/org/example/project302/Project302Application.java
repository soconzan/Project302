package org.example.project302;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@EnableCaching
@SpringBootApplication
public class Project302Application {

    public static void main(String[] args) {
        SpringApplication.run(Project302Application.class, args);
    }

}
