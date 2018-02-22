package com.myreward.engine.event.opcode;

import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;

public class IfEventModel extends IfBaseModel {
	public enum IfCompletionType {
		FLAG,
		AMOUNT,
		EVENT
	}
	public enum IfCompletionAmtType {
		LE,
		LT,
		EQ,
		GT,
		GE
	}
	public enum IfCompletionFlgType {
		SET,
		NOT_SET
	}

	private static String OPCODE_LABEL_FLAG = "if_cmp_flg";
	private static String OPCODE_LABEL_AMOUNT = "if_cmp_amt";
	private static String OPCODE_LABEL_EVENT = "if_evt_nm";
	private static String OPCODE_OPERAND_START = "(";
	private static String OPCODE_OPERAND_END = ")";
	private static String OPERAND_FORMAT_PATTERN = ",";
	private IfCompletionType type;
	private IfCompletionAmtType amountType;
	private IfCompletionFlgType flagType;
	private String name;
	private String amount;
	private String eventName;
	private int gotoLine;
	public static String[] OPCODE_HANDLER = {OPCODE_LABEL_FLAG, OPCODE_LABEL_AMOUNT, OPCODE_LABEL_EVENT};

	public IfEventModel(String statement) {
		IfCompletionType ifCompletionType = getType(statement);
		if(ifCompletionType!=null) {
			type = ifCompletionType;
			if(type==IfCompletionType.AMOUNT) {
				String[] amountOperand = this.parse(OPCODE_LABEL_AMOUNT, OPERAND_FORMAT_PATTERN, statement);
				name = amountOperand[0];
				amount = amountOperand[1];
				if(StringUtils.startsWith(statement, OPCODE_LABEL_AMOUNT+"_le")) {
					amountType = IfCompletionAmtType.LE;
				} else if(StringUtils.startsWith(statement, OPCODE_LABEL_AMOUNT+"_lt")) {
					amountType = IfCompletionAmtType.LT;
				} else if(StringUtils.startsWith(statement, OPCODE_LABEL_AMOUNT+"_eq")) {
					amountType = IfCompletionAmtType.EQ;
				} else if(StringUtils.startsWith(statement, OPCODE_LABEL_AMOUNT+"_gt")) {
					amountType = IfCompletionAmtType.GT;
				} else if(StringUtils.startsWith(statement, OPCODE_LABEL_AMOUNT+"_ge")) {
					amountType = IfCompletionAmtType.GE;
				}
			} else if(type==IfCompletionType.FLAG) {
				String[] flagOperand = this.parse(OPCODE_LABEL_FLAG, null, statement);
				name = flagOperand[0];
				if(StringUtils.startsWith(statement, OPCODE_LABEL_AMOUNT+"_set")) {
					flagType = IfCompletionFlgType.SET;
				} else if(StringUtils.startsWith(statement, OPCODE_LABEL_AMOUNT+"_not_set")) {
					flagType = IfCompletionFlgType.NOT_SET;
				}
			} else if(type==IfCompletionType.EVENT) {
				String[] eventOperand = this.parse(OPCODE_LABEL_EVENT, OPERAND_FORMAT_PATTERN, statement);
				eventName = eventOperand[0];
				gotoLine = Integer.valueOf(eventOperand[1]).intValue();
			}
		}
	}
	public IfCompletionType getType(String opcode) {
		if(StringUtils.startsWith(opcode, OPCODE_LABEL_FLAG))
			return IfCompletionType.FLAG;
		if(StringUtils.startsWith(opcode, OPCODE_LABEL_AMOUNT))
			return IfCompletionType.AMOUNT;
		if(StringUtils.startsWith(opcode, OPCODE_LABEL_EVENT))
			return IfCompletionType.EVENT;
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
}
