package com.myreward.engine.event.opcode;

import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.myreward.engine.model.event.EventDO;
import com.myreward.engine.model.event.OperationResultDO;
import com.myreward.engine.model.event.StatementOperationResult;
import com.myreward.parser.generator.MyRewardDataSegment;

public class ReturnModel extends OpCodeBaseModel {
	private static String OPCODE_LABEL = "return";
	private static int OPCODE_MAX_LENGTH = 1;
	public static String[] OPCODE_HANDLER = {OPCODE_LABEL};

	public ReturnModel() {
	}

	public ReturnModel(String statement) {
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
		OperationResultDO operationResultDO = new StatementOperationResult();
		operationResultDO.setResult(true);

		return operationResultDO;
	}

}
