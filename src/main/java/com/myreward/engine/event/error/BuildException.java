package com.myreward.engine.event.error;

import com.myreward.engine.audit.AuditEvent;
import com.myreward.engine.audit.AuditEventType;
import com.myreward.engine.audit.AuditManager;

public class BuildException extends Exception {
	private ErrorCode errorCode;
	public BuildException(ErrorCode errorCode){
		this.errorCode = errorCode;
		AuditManager.getInstance().audit(new AuditEvent(null, AuditEventType.SYMBOL_SEARCH_EXCEPTION, null));
	}
}
