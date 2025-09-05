package com.mutecsoft.healthvision.api;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@MapperScan("com.mutecsoft.healthvision.common.mapper")
@ComponentScan(basePackages = {"com.mutecsoft.healthvision.api", "com.mutecsoft.healthvision.common"})
public class ApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(ApiApplication.class, args);
	}
	
}
