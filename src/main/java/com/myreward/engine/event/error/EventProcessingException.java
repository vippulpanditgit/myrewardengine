package com.myreward.engine.event.error;

import com.myreward.engine.audit.AuditEvent;
import com.myreward.engine.audit.AuditEventType;
import com.myreward.engine.audit.AuditManager;

public class EventProcessingException extends Exception {
	private ErrorCode errorCode;
	public EventProcessingException(ErrorCode errorCode){
		this.errorCode = errorCode;
		AuditManager.getInstance().audit(new AuditEvent(null, AuditEventType.EVENT_PROCESSING_EXCEPTION, null));
	}

}
