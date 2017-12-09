package com.fltck.aws.agent.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.beans.factory.annotation.Value;

import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

@Configuration
public class RabbitMQConfig {
	@Value("${spring.rabbitmq.host:localhost}") String host;

	@Value("${spring.rabbitmq.exchange:exchange}") String exchange;
	@Value("${spring.rabbitmq.queue:spots}") String queue;
	@Value("${spring.rabbitmq.routingKey.heartbeat:heartbeat}") String routingKeyHeartbeat;

	@Bean
	public ConnectionFactory connectionFactory() {
		return new CachingConnectionFactory(host);
	}

//	@Bean
//	public Queue requests() {
//		return new AnonymousQueue();
//	}
//
//	@Bean
//	public Queue replies() {
//		return new AnonymousQueue();
//	}

	@Bean
	public RabbitAdmin admin(ConnectionFactory connectionFactory) {
		return new RabbitAdmin(connectionFactory);
	}

//	@Bean
//	public Jackson2JsonMessageConverter producerJackson2MessageConverter(ObjectMapper jsonObjectMapper) {
//		Jackson2JsonMessageConverter converter = new Jackson2JsonMessageConverter(jsonObjectMapper);
//		return converter;
//	}
//	
//	@Bean
//	public ObjectMapper jsonObjectMapper() {
//		ObjectMapper mapper = new ObjectMapper();
//		mapper.registerModule(new JavaTimeModule());
//		mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
//		return mapper;
//	}
	
	@Bean
	public RabbitTemplate template(ConnectionFactory connectionFactory) {
		RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
//	    rabbitTemplate.setMessageConverter(producerJackson2MessageConverter(jsonObjectMapper()));
		return rabbitTemplate;
	}

//	@Bean
//	@Primary
//	public SimpleMessageListenerContainer replyContainer(ConnectionFactory connectionFactory) {
//		SimpleMessageListenerContainer container = new SimpleMessageListenerContainer(connectionFactory);
//		container.setQueueNames(replies().getName());
//		return container;
//	}
//
//	@Bean
//	public AsyncRabbitTemplate asyncTemplate(RabbitTemplate template,
//			SimpleMessageListenerContainer container) {
//		return new AsyncRabbitTemplate(template, container);
//	}
//
//	@Bean
//	public SimpleMessageListenerContainer remoteContainer(ConnectionFactory connectionFactory) {
//		SimpleMessageListenerContainer container = new SimpleMessageListenerContainer(connectionFactory);
//		container.setQueueNames(requests().getName());
//		container.setMessageListener(new MessageListenerAdapter(new Object() {
//
//			@SuppressWarnings("unused")
//			public String handleMessage(String message) {
//				return message.toUpperCase();
//			}
//
//		}));
//		return container;
//	}

} 