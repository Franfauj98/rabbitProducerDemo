package com.rabbit.producer.rabbitproducerdemo.controllers;

import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
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
	private DirectExchange directExchange;

	private final String synchronousQueue = "synchronous";
	private final String asynchronousQueue = "asynchronous";

	@GetMapping(value = "/asynchronous/{toSend}")
	public ResponseEntity sendMessageQueue1(@PathVariable String toSend) {
		sendAsynchronous(toSend, synchronousQueue);
		return ResponseEntity.ok(toSend);
	}

	@GetMapping(value = "/synchronous/{toSend}")
	public ResponseEntity sendMessageQueue2(@PathVariable String toSend) {
		sendSynchronous(toSend, asynchronousQueue);
		return ResponseEntity.ok(toSend);
	}

	private void sendSynchronous(String message, String routingKey) {
		// Temps d'attente avant de supprimer un message de la file d'attente ou de considérer l'envoi de message en erreur
		int rabbitTimeout = 60000;
		this.template.setReplyTimeout(rabbitTimeout);
		Integer response = (Integer) this.template.convertSendAndReceive(directExchange.getName(), routingKey, message);
		System.out.println(directExchange.getName() + " Sent synchronously '" + message + "', routingKey: " + routingKey + ", date : " + LocalDateTime.now() + ", response : " + response);
	}

	private void sendAsynchronous(String message, String routingKey) {
		this.template.convertAndSend(directExchange.getName(), routingKey, message);
		System.out.println(directExchange.getName() + " Sent asynchronously '" + message + "', routingKey: " + routingKey + ", date : " + LocalDateTime.now());
	}
}
