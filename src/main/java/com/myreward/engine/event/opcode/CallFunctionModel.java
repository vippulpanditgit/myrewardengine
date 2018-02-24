package com.myreward.engine.event.opcode;

import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;

import com.myreward.engine.model.event.EventDO;
import com.myreward.engine.model.event.OperationResultDO;

public class CallFunctionModel extends CallBaseModel {
	public static String OPCODE_LABEL = "call_fn";
	private static String OPCODE_OPERAND_START = "(";
	private static String OPCODE_OPERAND_END = ")";
	private static String OPERAND_FORMAT_PATTERN = ":";
	private String name;
	private String version;
	public static String[] OPCODE_HANDLER = {OPCODE_LABEL};
	
	public CallFunctionModel() {
		super();
	}

	public CallFunctionModel(String statement) {
		String[] operand = parse(statement);
		if(operand!=null) {
			name = operand[0];
			version = operand[1];
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
		return OPCODE_LABEL+OPCODE_OPERAND_START+name+OPERAND_FORMAT_PATTERN+version+OPCODE_OPERAND_END;
	}
	public OperationResultDO process(EventDO event) {
		OperationResultDO operationResultDO = null;

		return operationResultDO;
	}
}
