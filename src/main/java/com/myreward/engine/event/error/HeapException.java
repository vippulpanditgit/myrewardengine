package com.myreward.engine.event.error;

public class HeapException extends Exception {
	public enum HeapExceptionType {
		HEAP_FULL,
		HEAP_SET_MAX_REQUIRES_MORE_THAN_ZERO, REMOVING_FROM_EMPTY_HEAP
	};
	private HeapExceptionType heapExceptionType;
	private String message;
	
	public HeapException(HeapExceptionType heapExceptionType, String message) {
		this.heapExceptionType = heapExceptionType;
		this.message = message;
		System.out.println("HeapException constructor TODO");
	}
}
