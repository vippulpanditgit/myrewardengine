package com.myreward.engine.event.opcode;

import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.myreward.engine.event.error.DebugException;
import com.myreward.engine.model.event.EventDO;
import com.myreward.engine.model.event.OperationResultDO;
import com.myreward.parser.generator.MyRewardDataSegment;

public class DebugModel extends OpCodeBaseModel {
	private static String OPCODE_LABEL = "debug";
	public static String[] OPCODE_HANDLER = {OPCODE_LABEL};
	public boolean ignore = false;

	public DebugModel() {
	}

	public DebugModel(String statement) {
	}
	public String[] parse(String value) {
		if(!StringUtils.startsWith(value, OPCODE_LABEL))
			return null;
		return null;
	}	
	public String toString() {
		return OPCODE_LABEL;
	}
	
	@Override
	public String[] getOpcodes() {
		return OPCODE_HANDLER;
	}

	@Override
	public OperationResultDO process(List<OpCodeBaseModel> instructionOpCodes, MyRewardDataSegment myRewardDataSegment,
			EventDO event) throws Exception {
		if(!ignore)
			throw new DebugException();
		return null;
	}

}
