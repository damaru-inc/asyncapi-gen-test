package com.solace.tester;

public class PublishListener implements com.solace.asyncapi.OrderChannel.PublishListener {

	@Override
	public void onResponse(String messageId) {
		System.out.println("Received " + messageId);

	}

	@Override
	public void handleException(String messageId, Exception exception, long timestamp) {
		exception.printStackTrace();
	}

}
