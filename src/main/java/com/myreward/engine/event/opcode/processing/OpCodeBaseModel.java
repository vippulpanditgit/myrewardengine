package com.myreward.engine.event.opcode.processing;

import java.io.Serializable;
import java.util.List;

import com.myreward.engine.event.processing.helper.OperationResultDO;
import com.myreward.engine.model.event.EventDO;
import com.myreward.parser.generator.MyRewardDataSegment;

public  class OpCodeBaseModel implements Serializable {
	
	public  String[] getOpcodes() {
		return null;};

	public OperationResultDO process(List<OpCodeBaseModel> instructionOpCodes, MyRewardDataSegment myRewardDataSegment, EventDO event)  throws Exception {
		return null;};
	
}
