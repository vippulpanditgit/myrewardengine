package com.myreward.engine.app;

import java.util.List;

import com.myreward.engine.event.opcode.OpCodeBaseModel;
import com.myreward.engine.event.processor.EventProcessor;
import com.myreward.parser.generator.MyRewardDataSegment;
import com.myreward.parser.generator.MyRewardPCodeGenerator;

public class AppInstanceContext {
	public String username;
	public String uuid;
	public boolean isDebug = false;
	public MyRewardDataSegment myRewardDataSegment;
	public List<OpCodeBaseModel> virtualInstructionOpCodes;
	public EventProcessor eventProcessor;

}
