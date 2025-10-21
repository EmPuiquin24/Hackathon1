package com.qhapaq.oreo;

import org.springframework.boot.SpringApplication;

public class TestOreoApplication {

    public static void main(String[] args) {
        SpringApplication.from(OreoApplication::main).with(TestcontainersConfiguration.class).run(args);
    }

}
