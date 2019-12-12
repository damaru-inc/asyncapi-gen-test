package com.solace.tester;

import com.solace.asyncapi.OrderChannel;
import com.solace.asyncapi.OrderMessage;

public class OrderSubscribeListener implements OrderChannel.SubscribeListener {

	@Override
	public void onReceive(OrderMessage orderMessage) {
		System.out.println("Received from topic: " + orderMessage.getTopic() +
				" order id: " + orderMessage.getPayload().getOrderId() +
				" message id: " + orderMessage.getMessageId());
	}

	@Override
	public void handleException(Exception exception) {
		exception.printStackTrace();
	}

}
