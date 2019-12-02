package com.rabbit.producer.rabbitproducerdemo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication
@EntityScan(basePackages = {"com.rabbit.producer.rabbitproducerdemo"})
public class RabbitProducerDemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(RabbitProducerDemoApplication.class, args);
	}

}
