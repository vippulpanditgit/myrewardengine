package com.myreward.engine.audit;

import com.fasterxml.jackson.core.JsonProcessingException;

public class AuditProcessOuptutStream implements IAuditOutputStream<AuditEvent> {

	private final AuditEventProcessor processor;
	
	public AuditProcessOuptutStream() {
		processor = new AuditEventProcessor();
	}
	@Override
	public IAuditOutputStream<AuditEvent> write(AuditEvent event) {
		try {
			processor.process(event);
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return this;
	}

	@Override
	public IAuditOutputStream<AuditEvent> write(EventBatch<AuditEvent> batch) {
		try {
			processor.process(batch);
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return this;
	}

}
