package com.myreward.engine.event.error;

import com.myreward.engine.audit.AuditEvent;
import com.myreward.engine.audit.AuditEventType;
import com.myreward.engine.audit.AuditManager;

public class MetaDataParsingException extends Exception {
	private ErrorCode errorCode;
	public MetaDataParsingException(ErrorCode errorCode){
		this.errorCode = errorCode;
		AuditManager.getInstance().audit(new AuditEvent(null, AuditEventType.META_DATA_PARSING_EXCEPTION, null));
	}

}
