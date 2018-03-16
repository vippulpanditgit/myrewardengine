package com.myreward.engine.audit;

import com.fasterxml.jackson.core.JsonProcessingException;

public class AuditEventProcessor {
	
	public void process(AuditEvent event) throws JsonProcessingException {
		System.out.println("TODO _ AuditEventProcessor.process "+new String(ObjectJsonSerializer.toJson(event, null)));
		
	}
	public void process(EventBatch batch) throws JsonProcessingException {
		System.out.println("TODO _ AuditEventProcessor.process "+new String(ObjectJsonSerializer.toJson(batch, null)));		
	}

}
