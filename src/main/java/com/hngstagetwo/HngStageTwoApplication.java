package com.hngstagetwo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationProperties;

@SpringBootApplication
@ConfigurationProperties(prefix = "jwt")
public class HngStageTwoApplication {

    public static void main(String[] args) {
        SpringApplication.run(HngStageTwoApplication.class, args);
    }

}
