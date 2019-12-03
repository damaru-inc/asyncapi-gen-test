package com.solace.tester;

import com.solace.asyncapi.OrderChannel;
import com.solace.asyncapi.OrderMessage;

public class SubscribeListener implements OrderChannel.SubscribeListener {

	@Override
	public void onReceive(OrderMessage orderMessage) {
		
		System.out.println("Received: " + orderMessage.getTopic() + "\n" + orderMessage.getPayload());
		
	}

	@Override
	public void handleException(Exception exception) {
		exception.printStackTrace();
	}


}
