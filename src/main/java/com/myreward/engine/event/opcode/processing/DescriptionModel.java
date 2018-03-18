package com.myreward.engine.event.opcode.processing;

import java.util.List;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;

import com.myreward.engine.model.event.EventDO;
import com.myreward.engine.model.event.OperationResultDO;
import com.myreward.parser.generator.MyRewardDataSegment;

public class DescriptionModel extends OpCodeBaseModel {
	private static String OPCODE_LABEL = "desc";
	private static String OPCODE_OPERAND_START = "\"";
	private static String OPCODE_OPERAND_END = "\"";
	private static String OPERAND_FORMAT_PATTERN = ":";
	private String description;
	private String version;
	public static String[] OPCODE_HANDLER = {OPCODE_LABEL};

	public DescriptionModel() {
	}

	public DescriptionModel(String statement) {
		String[] operand = parse(statement);
		if(operand!=null) {
			description = operand[0];
		}
	}
	public String[] parse(String value) {
		if(!StringUtils.startsWith(value, OPCODE_LABEL))
			return null;
		String operandValue = StringUtils.substringBetween(value, OPCODE_OPERAND_START, OPCODE_OPERAND_END);
		Pattern pattern = Pattern.compile(OPERAND_FORMAT_PATTERN);
		String[] functionCallParameter = pattern.split(operandValue);
		return functionCallParameter;
	}	
	@Override
	public String[] getOpcodes() {
		return OPCODE_HANDLER;
	}
	public String toString() {
		return OPCODE_LABEL+" "+OPCODE_OPERAND_START+description+OPCODE_OPERAND_END;
	}

	public OperationResultDO process(List<OpCodeBaseModel> instructionOpCodes, MyRewardDataSegment myRewardDataSegment, EventDO event) {
		OperationResultDO operationResultDO = null;
System.out.println(description);
		return operationResultDO;
	}
}
