package com.myreward.engine.event.error;

public class MetadataParsingException extends Exception {
	private ErrorCode errorCode;
	public MetadataParsingException(ErrorCode errorCode){
		this.errorCode = errorCode;
	}

}
