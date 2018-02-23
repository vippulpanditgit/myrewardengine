package com.myreward.engine.event.opcode;

import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;

public class StoreRepeatModel extends StoreBaseModel {
	public enum StorePriorityType {
		FLAG,
		AMOUNT,
		TYPE
	}
	private static String OPCODE_LABEL_FLAG = "store_rpt_flg";
	private static String OPCODE_LABEL_AMOUNT = "store_rpt_aft";
	private static String OPCODE_LABEL_TYPE = "store_rpt_typ";
	private static String OPCODE_OPERAND_START = "(";
	private static String OPCODE_OPERAND_END = ")";
	private static String OPERAND_FORMAT_PATTERN = ",";
	private StorePriorityType type;
	private String name;
	private String amount;
	private String after;
	public static String[] OPCODE_HANDLER = {OPCODE_LABEL_FLAG, OPCODE_LABEL_AMOUNT, OPCODE_LABEL_TYPE};

	public StoreRepeatModel() {
	}

	public StoreRepeatModel(String statement) {
		StorePriorityType storePriorityType = getType(statement);
		if(storePriorityType!=null) {
			type = storePriorityType;
			if(type==StorePriorityType.AMOUNT) {
				String[] amountOperand = this.parse(OPCODE_LABEL_AMOUNT, OPERAND_FORMAT_PATTERN, statement);
				name = amountOperand[0];
				amount = amountOperand[1];
			} else if(type==StorePriorityType.FLAG) {
				String[] amountOperand = this.parse(OPCODE_LABEL_FLAG, null, statement);
				name = amountOperand[0];
			} else if(type==StorePriorityType.TYPE) {
				String[] amountOperand = this.parse(OPCODE_LABEL_TYPE, OPERAND_FORMAT_PATTERN, statement);
				name = amountOperand[0];
				after = amountOperand[1];
			}
		}
	}
	public StorePriorityType getType(String opcode) {
		if(StringUtils.startsWith(opcode, OPCODE_LABEL_FLAG))
			return StorePriorityType.FLAG;
		if(StringUtils.startsWith(opcode, OPCODE_LABEL_AMOUNT))
			return StorePriorityType.AMOUNT;
		if(StringUtils.startsWith(opcode, OPCODE_LABEL_TYPE))
			return StorePriorityType.TYPE;
		return null;
	}
	public String[] parse(String opcodeLabelFlag, String operandSeparator, String value) {
		if(!StringUtils.startsWith(value, opcodeLabelFlag))
			return null;
		String operandValue = StringUtils.substringBetween(value, OPCODE_OPERAND_START, OPCODE_OPERAND_END);
		if(operandSeparator!=null) {
			Pattern pattern = Pattern.compile(operandSeparator);
			String[] functionCallParameter = pattern.split(operandValue);
			return functionCallParameter;
		} else {
			return new String[]{operandValue};
		}
	}
	@Override
	public String[] getOpcodes() {
		return OPCODE_HANDLER;
	}

}
