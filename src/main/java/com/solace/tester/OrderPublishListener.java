package com.solace.tester;

import com.solace.asyncapi.OrderChannel;

public class OrderPublishListener implements OrderChannel.PublishListener {

	@Override
	public void onResponse(String messageId) {
		System.out.println("Broker received " + messageId);

	}

	@Override
	public void handleException(String messageId, Exception exception, long timestamp) {
		exception.printStackTrace();
	}

}
