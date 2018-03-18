package com.myreward.engine.event.opcode.processing;

import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.myreward.engine.model.event.EventDO;
import com.myreward.engine.model.event.OperationResultDO;
import com.myreward.parser.generator.MyRewardDataSegment;

public class LabelMainModel extends LabelBaseModel {
	private static String OPCODE_LABEL = "lbl_main";
	private static int OPCODE_MAX_LENGTH = 1;
	public static String[] OPCODE_HANDLER = {OPCODE_LABEL};

	public LabelMainModel() {
	}

	public LabelMainModel(String statement) {
		if(!StringUtils.equalsIgnoreCase(statement, OPCODE_LABEL))
				return;
	}
	@Override
	public String[] getOpcodes() {
		return OPCODE_HANDLER;
	}
	public String toString() {
		return OPCODE_LABEL;
	}

	@Override
	public OperationResultDO process(List<OpCodeBaseModel> instructionOpCodes, MyRewardDataSegment myRewardDataSegment,
			EventDO event) {
		// TODO Auto-generated method stub
		return null;
	}

}
