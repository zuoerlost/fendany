package com.fendany;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ImportResource;

/**
 * Created by zuoer on 16-9-27.
 *
 */
@SpringBootApplication
@ImportResource("classpath:context-main.xml")
public class MainApplication {

    public static void main(String[] args) {
        SpringApplication.run(MainApplication.class ,args);
    }

}
