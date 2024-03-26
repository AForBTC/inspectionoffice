package com.ws.inspectionoffice;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.ws.inspectionoffice.mapper")
public class InspectionofficeApplication {

    public static void main(String[] args) {
        SpringApplication.run(InspectionofficeApplication.class, args);
    }

}
