package com.myreward.engine.audit;

public class AuditProcessOuptutStream implements IAuditOutputStream<AuditEvent> {

	private final AuditEventProcessor processor;
	
	public AuditProcessOuptutStream() {
		processor = new AuditEventProcessor();
	}
	@Override
	public IAuditOutputStream<AuditEvent> write(AuditEvent event) {
		processor.process(event);
		return this;
	}

	@Override
	public IAuditOutputStream<AuditEvent> write(EventBatch<AuditEvent> batch) {
		processor.process(batch);
		return this;
	}

}
