package com.myreward.engine.event.error;

public class EventProcessingException extends Exception {
	private ErrorCode errorCode;
	public EventProcessingException(ErrorCode errorCode){
		this.errorCode = errorCode;
	}

}
