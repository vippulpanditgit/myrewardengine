package com.myreward.engine.event.error;

import com.myreward.parser.util.InternalLogger;

public class AuditRuntimeException extends RuntimeException {
	/**
     * Instantiates a new audit4j runtime exception.
     *
     * @param message the message
     * @param cause the cause
     */
    public AuditRuntimeException(String message, Throwable cause) {
        super(message, cause);
        InternalLogger.error("Problem while running MyReward: " + message, cause);
    }

}
