package com.solace.tester;

import com.solace.asyncapi.OrderMessage;
import com.solace.asyncapi.OrderQueueChannel;

public class OrderSubscribeListener implements OrderQueueChannel.SubscribeListener {

	@Override
	public void onReceive(OrderMessage orderMessage) {

		System.out.println("Received: " + orderMessage.getTopic() + "\n" + orderMessage.getPayload());

	}

	@Override
	public void handleException(Exception exception) {
		exception.printStackTrace();
	}

}
