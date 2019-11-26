package com.rabbit.producer.rabbitproducerdemo.controllers;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
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

	private DirectExchange directExchange = new DirectExchange("rabbit.example.direct");

	private Queue queue = new Queue("rabbitExample.first");
	private Queue queue2 = new Queue("rabbitExample.second");
	// Temps d'attente avant de supprimer un message de la file d'attente ou de considérer l'envoi de message en erreur
	private int rabbitTimeout = 60000;

	@Bean
	public Binding bindingFirst() {
		return BindingBuilder.bind(queue)
				.to(directExchange)
				.with("first");
	}

	@Bean
	public Binding BindingSecond() {
		return BindingBuilder.bind(queue2)
				.to(directExchange)
				.with("second");
	}

	@GetMapping(value = "/queue1/{toSend}")
	public ResponseEntity sendMessageQueue1(@PathVariable String toSend) {
		sendAsynchronous(toSend, "first");
		return ResponseEntity.ok(toSend);
	}

	@GetMapping(value = "/queue2/{toSend}")
	public ResponseEntity sendMessageQueue2(@PathVariable String toSend) {
		sendSynchronous(toSend, "second");
		return ResponseEntity.ok(toSend);
	}

	private void sendSynchronous(String message, String routingKey) {
		this.template.setReplyTimeout(rabbitTimeout);
		Integer response = (Integer) this.template.convertSendAndReceive(directExchange.getName(), routingKey, message);
		System.out.println(directExchange.getName() + " Sent synchronously '" + message + "', routingKey: " + routingKey + ", date : " + LocalDateTime.now() + ", response : " + response);
	}

	private void sendAsynchronous(String message, String routingKey) {
		this.template.convertAndSend(directExchange.getName(), routingKey, message);
		System.out.println(directExchange.getName() + " Sent asynchronously '" + message + "', routingKey: " + routingKey + ", date : " + LocalDateTime.now());
	}
}
