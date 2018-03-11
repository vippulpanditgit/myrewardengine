package com.myreward.engine.event.opcode;

import java.util.List;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;

import com.myreward.engine.model.event.EventDO;
import com.myreward.engine.model.event.OperationResultDO;
import com.myreward.parser.generator.MyRewardDataSegment;

public class DebugModel extends OpCodeBaseModel {
	private static String OPCODE_LABEL = "debug";
	public static String[] OPCODE_HANDLER = {OPCODE_LABEL};

	public DebugModel() {
	}

	public DebugModel(String statement) {
	}
	public String[] parse(String value) {
		if(!StringUtils.startsWith(value, OPCODE_LABEL))
			return null;
		return null;
	}	
	
	@Override
	public String[] getOpcodes() {
		return OPCODE_HANDLER;
	}

	@Override
	public OperationResultDO process(List<OpCodeBaseModel> instructionOpCodes, MyRewardDataSegment myRewardDataSegment,
			EventDO event) {
		// TODO Auto-generated method stub
		return null;
	}

}
