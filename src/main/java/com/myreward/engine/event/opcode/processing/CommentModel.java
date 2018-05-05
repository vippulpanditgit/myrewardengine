package com.myreward.engine.event.opcode.processing;

import java.util.List;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;

import com.myreward.engine.event.processing.helper.OperationResultDO;
import com.myreward.engine.model.event.EventDO;
import com.myreward.parser.generator.MyRewardDataSegment;

public class CommentModel extends OpCodeBaseModel {
	public static String OPCODE_LABEL = "//";
	public static String[] OPCODE_HANDLER = {OPCODE_LABEL};
	public String comment;
	public CommentModel() {
	}

	public CommentModel(String statement) {
		String[] operand = parse(statement);
		if(operand!=null) {
			comment = operand[0];
		}
	}

	public String[] parse(String value) {
		if(!StringUtils.startsWith(value, OPCODE_LABEL))
			return null;
		return new String[] {StringUtils.substringAfter(value, OPCODE_LABEL)};
	}
	@Override
	public String[] getOpcodes() {
		return OPCODE_HANDLER;
	}
	public String toString() {
		return OPCODE_LABEL+" "+comment;
	}
	@Override
	public OperationResultDO process(List<OpCodeBaseModel> instructionOpCodes, MyRewardDataSegment myRewardDataSegment,
			EventDO event)  throws Exception {
		return null;
	}

}
