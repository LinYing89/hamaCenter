package com.bairock.iot.hamaCenter;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@MapperScan(basePackages="com.bairock.iot.hamaCenter.mapper")
@EnableCaching
@EnableScheduling
public class HamaServerApplication {

	public static void main(String[] args) {
		SpringApplication springApplication = new SpringApplication(HamaServerApplication.class);
        springApplication.run(args);
	}
}
