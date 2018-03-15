package com.myreward.engine.event.error;

import com.myreward.engine.audit.AuditEvent;
import com.myreward.engine.audit.AuditEventType;
import com.myreward.engine.audit.AuditManager;
import com.myreward.parser.util.InternalLogger;

public class MetaDataCreationException extends Exception {
    public MetaDataCreationException(String message, Throwable cause) {
        super(message, cause);
		AuditManager.getInstance().audit(new AuditEvent(null, AuditEventType.META_DATA_CREATION_EXCEPTION, null));
       InternalLogger.error("Problem while running MyReward: " + message, cause);
    }

}
