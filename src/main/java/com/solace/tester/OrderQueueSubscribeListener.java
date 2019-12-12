package com.solace.tester;

import com.solace.asyncapi.OrderMessage;
import com.solace.asyncapi.OrderQueueChannel;

public class OrderQueueSubscribeListener implements OrderQueueChannel.SubscribeListener {

	@Override
	public void onReceive(OrderMessage orderMessage) {
		System.out.println("Received from queue: " + orderMessage.getTopic() +
		" order id: " + orderMessage.getPayload().getOrderId() +
		" message id: " + orderMessage.getMessageId());
	}

	@Override
	public void handleException(Exception exception) {
		exception.printStackTrace();
	}

}
