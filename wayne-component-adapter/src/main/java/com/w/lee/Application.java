package com.w.lee;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 启动项
 *
 * @author w.lee
 * @since 1.0.0
 */
@SpringBootApplication
@MapperScan("com.w.lee.infrastructure.mapper")
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}