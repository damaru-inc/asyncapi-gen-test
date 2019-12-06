package com.solace.tester;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;

import com.solace.asyncapi.Order;
import com.solace.asyncapi.OrderChannel;
import com.solace.asyncapi.OrderMessage;

@SpringBootApplication
@ComponentScan("com.solace")
public class SolaceBeanTesterApplication implements CommandLineRunner  {

	public static void main(String[] args) {
		SpringApplication.run(SolaceBeanTesterApplication.class, args);
	}

	@Autowired
	ApplicationContext ctx;
	
	@Autowired OrderChannel orderChannel;
	
	@Override
	public void run(String... args) throws Exception {
		Runtime.getRuntime().addShutdownHook(new ShutdownHook(orderChannel));
		testSubscribe();
		testPublish();
		Thread.sleep(5000);
	}
	
	public void testSubscribe() throws Exception {
		orderChannel.subscribe(new OrderSubscribeListener());
	}
	
	public void testPublish() throws Exception {
		orderChannel.initPublisher(new OrderPublishListener());
		Order order = new Order();
		OrderMessage orderMessage = new OrderMessage();
		orderMessage.setPayload(order);
		
		for (int i = 0; i < 10; i++) {
			order.setOrderId(i);
			order.setOrderDescription("I'm order # " + i);
			orderChannel.sendOrderMessage(orderMessage, OrderChannel.Action.buyItem, "trace", i);
			orderChannel.sendOrder(order, OrderChannel.Action.buyItem, "trace", i);
		}
	}

	public void dumpBeans() {
		System.out.println("Let's inspect the beans provided by Spring Boot:");

		String[] beanNames = ctx.getBeanDefinitionNames();
		Arrays.sort(beanNames);
		for (String beanName : beanNames) {
			System.out.println(beanName);
		}		
	}
	
	static class ShutdownHook extends Thread {
		
		private OrderChannel orderChannel;
		
		public ShutdownHook(OrderChannel orderChannel) {
			this.orderChannel = orderChannel;
		}
		
		@Override
		public void run() {
			System.out.println("Shutdown hook called.");
			orderChannel.close();			
		}
	}

}
