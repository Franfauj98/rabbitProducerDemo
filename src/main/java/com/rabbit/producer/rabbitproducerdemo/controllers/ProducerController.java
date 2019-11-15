package com.rabbit.producer.rabbitproducerdemo.controllers;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.LocalDateTime;

@Controller
@RequestMapping("/api/producer")
public class ProducerController {
	@Autowired
	private RabbitTemplate template;

	@Autowired
	private Queue queue;

	@Bean
	public Queue hello() {
		return new Queue("rabbitExample");
	}

	@GetMapping(value = "{toSend}")
	public ResponseEntity sendMessage(@PathVariable String toSend) {
		send(toSend);
		return ResponseEntity.ok(toSend);
	}

	private void send(String message) {
		this.template.convertAndSend(queue.getName(), message);
		System.out.println("Sent '" + message + "', date : " + LocalDateTime.now());
	}
}
