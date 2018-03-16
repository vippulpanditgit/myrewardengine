package com.myreward.engine.app;

import java.util.List;
import java.util.Stack;

import com.myreward.engine.event.opcode.OpCodeBaseModel;
import com.myreward.engine.event.processor.EventProcessor;
import com.myreward.engine.event.processor.MetaOpCodeProcessor;
import com.myreward.parser.generator.MyRewardDataSegment;
import com.myreward.parser.generator.MyRewardPCodeGenerator;
import com.myreward.parser.util.Heap;

public class AppInstanceContext {
	public String username;
	public String uuid;
	public boolean isDebug = false;
	public MyRewardDataSegment dataSegment;
	public MetaOpCodeProcessor metaOpCodeProcessor;
	public List<OpCodeBaseModel> virtualInstructionOpCodes;
	public EventProcessor eventProcessor;
	public Stack<Object> stackSegment;
	public Heap<Object> headSegment;
	
	public boolean isInstanceReady() {
		if(dataSegment!=null
//				&& virtualInstructionOpCodes!=null
//				&& virtualInstructionOpCodes.size()>0
				&& eventProcessor!=null)
			return true;
		return false;
	}
	public void print_data_segment() {
		dataSegment.printString();
	}

}
