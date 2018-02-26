package com.myreward.engine.event.opcode;

import java.io.Serializable;
import java.util.List;

import com.myreward.engine.model.event.EventDO;
import com.myreward.engine.model.event.OperationResultDO;
import com.myreward.parser.generator.MyRewardDataSegment;

public abstract class OpCodeBaseModel implements Serializable {
	
	public abstract String[] getOpcodes();

	public abstract OperationResultDO process(List<OpCodeBaseModel> instructionOpCodes, MyRewardDataSegment myRewardDataSegment, EventDO event);
	
}
