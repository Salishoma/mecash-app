package com.oma.mecash.migrations;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DbMigration {
    public static void main(String[] args) {
        SpringApplication.run(DbMigration.class, args);
    }
}