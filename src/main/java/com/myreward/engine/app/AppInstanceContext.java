package com.myreward.engine.app;

import java.util.Map;
import java.util.Stack;
import java.util.function.Consumer;

import com.myreward.engine.event.error.HeapException;
import com.myreward.engine.event.processor.EventProcessor;
import com.myreward.engine.event.processor.MetaOpCodeProcessor;
import com.myreward.parser.generator.MyRewardDataSegment;
import com.myreward.parser.symbol.MyRewardSymbolTable;
import com.myreward.parser.symbol.SymbolTable;
import com.myreward.parser.util.Heap;

public class AppInstanceContext implements java.io.Serializable  {
	public String actor;
	public String uuid;
	public boolean isDebug = false;
	public MyRewardDataSegment dataSegment;
	public MetaOpCodeProcessor metaOpCodeProcessor;
	public EventProcessor eventProcessor;
	public Stack<Object> stackSegment;
	public Heap<Object> heapSegment;
	public Map<String, String> nextMetaDataToProcess;
	public SymbolTable symbolTable;
	
	public AppInstanceContext() {
		symbolTable = new MyRewardSymbolTable();
		stackSegment = new Stack<>();
		try {
			heapSegment = new Heap<Object>(1000);
		} catch (HeapException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public boolean isInstanceReady() {
		if(dataSegment!=null
				&& eventProcessor!=null)
			return true;
		return false;
	}
	public void print_data_segment() {
		dataSegment.printString();
	}
}
