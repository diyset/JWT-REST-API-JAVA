package com.jwtrestapi.beta;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.convert.Jsr310Converters;

import javax.annotation.PostConstruct;
import javax.persistence.Entity;
import java.util.TimeZone;

@SpringBootApplication
@EntityScan(basePackageClasses = {
        BetaApplication.class,
        Jsr310Converters.class
})
public class BetaApplication {

    @PostConstruct
    void init() {
        TimeZone.setDefault(TimeZone.getTimeZone("Asia/Jakarta"));
    }

    public static void main(String[] args) {
        SpringApplication.run(BetaApplication.class, args);
    }
}
