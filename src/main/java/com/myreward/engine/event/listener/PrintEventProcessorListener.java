package com.myreward.engine.event.listener;

public class PrintEventProcessorListener implements IEventProcessorListener {

	@Override
	public void onExecute(String description) {
		System.out.println("Listerner: "+description);

	}

}
