package com.solace.tester;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;

import com.solace.asyncapi.Account;
import com.solace.asyncapi.Address;
import com.solace.asyncapi.Item;
import com.solace.asyncapi.Order;
import com.solace.asyncapi.Order.Shipping;
import com.solace.asyncapi.OrderChannel;
import com.solace.asyncapi.OrderMessage;
import com.solace.asyncapi.OrderQueueChannel;

@SpringBootApplication
@ComponentScan("com.solace")
public class SolaceGeneratorTesterApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(SolaceGeneratorTesterApplication.class, args);
	}

	@Autowired
	ApplicationContext ctx;

	@Autowired
	OrderChannel orderChannel;
	@Autowired
	OrderQueueChannel orderQueueChannel;

	@Override
	public void run(String... args) throws Exception {
		Runtime.getRuntime().addShutdownHook(new ShutdownHook(orderChannel, orderQueueChannel));
		testSubscribe();
		testPublish();
		Thread.sleep(1000);
	}

	public void testSubscribe() throws Exception {
		orderChannel.subscribe(new OrderSubscribeListener());
		orderQueueChannel.subscribe(new OrderQueueSubscribeListener());
	}

	public void testPublish() throws Exception {
		orderChannel.initPublisher(new OrderPublishListener());
		
		Address address = new Address();
		address
		.setStreetAddress("123 Fake St")
		.setCity("Carp")
		.setPostalCode("K0A 1L0")
		.setProvince("ONT");
		
		Account account = new Account();
		account
		.setAccountId(12345)
		.setFirstName("Angela")
		.setLastName("Aarons");
		
		Shipping shipping = new Shipping();
		shipping
		.setCost(22.50)
		.setMethod(Shipping.Method.courier)
		.setShipTo(address);
		
		Order order = new Order();
		order
		.setCustomer(account)
		.setShipping(shipping);
		
		Item[] items = new Item[2];
		Item i1 = new Item();
		i1.setCatalogId(123);
		i1.setDescription("Dish rack");
		i1.setPrice(20.00);
		items[0] = i1;
		
		Item i2 = new Item();
		i2.setCatalogId(123);
		i2.setDescription("Ferrari");
		i2.setPrice(200000.00);
		items[1] = i2;
		
		order.setItems(items);
		
		OrderMessage orderMessage = new OrderMessage();
		orderMessage.setPayload(order);

		int span = 1;
		for (int i = 0; i < 10; i++) {
			order.setOrderId(i);
			order.setOrderDescription("I'm order # " + i);
			int price = 200 + i * 320;
			order.setPrice(price / 100.0);
			orderChannel.sendOrderMessage(orderMessage, OrderChannel.Action.buyItem, "trace", span++);
			orderChannel.sendOrderMessage(orderMessage, OrderChannel.Action.returnItem, "trace", span++);
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
		private OrderQueueChannel orderQueueChannel;

		public ShutdownHook(OrderChannel orderChannel, OrderQueueChannel orderQueueChannel) {
			this.orderChannel = orderChannel;
			this.orderQueueChannel = orderQueueChannel;
		}

		@Override
		public void run() {
			System.out.println("Shutdown hook called.");
			orderChannel.close();
			orderQueueChannel.close();
		}
	}

}
