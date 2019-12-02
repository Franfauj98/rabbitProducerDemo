package com.rabbit.producer.rabbitproducerdemo.config;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

@Configuration
public class RabbitConfig {
	private final String queuePrefix = "rabbitExample";
	private final String synchronousQueue = "synchronous";
	private final String asynchronousQueue = "asynchronous";

	private final AmqpAdmin amqpAdmin;

	public RabbitConfig(AmqpAdmin amqpAdmin) {
		this.amqpAdmin = amqpAdmin;
	}

	@PostConstruct
	public void afterInit() {
		amqpAdmin.declareExchange(getDirectExchange());
		amqpAdmin.declareQueue(getQueueSynchronous());
		amqpAdmin.declareQueue(getQueueAsynchronous());
	}

	@Bean
	public DirectExchange getDirectExchange() {
		return new DirectExchange("rabbit.example.direct");
	}

	@Bean
	public Queue getQueueSynchronous() {
		return new Queue(queuePrefix + "." + synchronousQueue);
	}

	@Bean
	public Queue getQueueAsynchronous() {
		return new Queue(queuePrefix + "." + asynchronousQueue);
	}

	@Bean
	public Binding bindingSynchronous() {
		return BindingBuilder.bind(getQueueSynchronous())
				.to(getDirectExchange())
				.with(synchronousQueue);
	}

	@Bean
	public Binding BindingAsynchronous() {
		return BindingBuilder.bind(getQueueAsynchronous())
				.to(getDirectExchange())
				.with(asynchronousQueue);
	}


}
