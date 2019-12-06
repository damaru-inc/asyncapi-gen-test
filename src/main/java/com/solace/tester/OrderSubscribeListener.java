package com.solace.tester;

import com.solace.asyncapi.OrderChannel;
import com.solace.asyncapi.OrderMessage;

public class OrderSubscribeListener implements OrderChannel.SubscribeListener {

	@Override
	public void onReceive(OrderMessage orderMessage) {
		System.out.printf("Received:  %-40s %s\n",  orderMessage.getTopic(), orderMessage.getPayload());
	}

	@Override
	public void handleException(Exception exception) {
		exception.printStackTrace();
	}


}
