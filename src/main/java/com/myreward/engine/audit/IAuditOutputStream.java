package com.myreward.engine.audit;

public interface IAuditOutputStream<T extends Event> {
	IAuditOutputStream<T> write(T event);
	
	IAuditOutputStream<T> write(EventBatch<T> batch);

}
