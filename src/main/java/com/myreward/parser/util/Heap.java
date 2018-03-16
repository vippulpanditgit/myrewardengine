package com.myreward.parser.util;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.myreward.engine.event.error.HeapException;
import com.myreward.engine.event.error.HeapException.HeapExceptionType;

public class Heap<T> implements Serializable {
	private List<T> heapObjectList;
	private int maxSize;
	private int currentSize;
	
	public Heap(int maxSize) throws HeapException {
		if(maxSize <= 0)
			throw new HeapException(HeapExceptionType.HEAP_SET_MAX_REQUIRES_MORE_THAN_ZERO, "Max size can't be 0 or less");
		this.maxSize = maxSize;
		this.currentSize = 0;
		heapObjectList = new ArrayList<T>();
	}
	
	public boolean isEmpty() {
		return this.currentSize == 0;
	}
	public boolean insert(T element) throws HeapException {
		if(currentSize+1 == maxSize)
			throw new HeapException(HeapExceptionType.HEAP_FULL, "Current size is: "+currentSize+" Max Size is: "+maxSize);
		heapObjectList.add(element);
		return true;
	}
	public boolean remove() throws HeapException {
		if(isEmpty())
			throw new HeapException(HeapExceptionType.REMOVING_FROM_EMPTY_HEAP,"Removing last elelment from heap but heap is empty");
		
		heapObjectList.remove(heapObjectList.size()-1);
		return true;
	}
}
