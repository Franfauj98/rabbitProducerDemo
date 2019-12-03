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

	/**
	 * Création des exchanges et des queues if not present
	 */
	@PostConstruct
	public void afterInit() {
		amqpAdmin.declareExchange(getDirectExchange());
		amqpAdmin.declareQueue(getQueueSynchronous());
		amqpAdmin.declareQueue(getQueueAsynchronous());
	}

	/**
	 * Récupération du direct exchange rabbit.example.direct
	 *
	 * @return le direct exchange rabbit.example.direct
	 */
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
