package com.myreward.engine.app;

import java.util.Map;
import java.util.Stack;
import java.util.function.Consumer;

import com.myreward.engine.event.processor.EventProcessor;
import com.myreward.engine.event.processor.MetaOpCodeProcessor;
import com.myreward.parser.generator.MyRewardDataSegment;
import com.myreward.parser.util.Heap;

public class AppInstanceContext {
	public String actor;
	public String uuid;
	public boolean isDebug = false;
	public MyRewardDataSegment dataSegment;
	public MetaOpCodeProcessor metaOpCodeProcessor;
	public EventProcessor eventProcessor;
	public Stack<Object> stackSegment;
	public Heap<Object> heapSegment;
	public Map<String, String> nextMetaDataToProcess;
	
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
