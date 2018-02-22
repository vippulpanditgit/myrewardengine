package com.myreward.engine.event.opcode;

import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;

public class IfRewardModel extends IfBaseModel {
	public enum IfRewardType {
		FLAG,
		AMOUNT
	}
	public enum IfRewardAmtType {
		LE,
		LT,
		EQ,
		GT,
		GE
	}
	public enum IfRewardFlgType {
		SET,
		NOT_SET
	}

	private static String OPCODE_LABEL_FLAG = "if_rwd_flg";
	private static String OPCODE_LABEL_AMOUNT = "if_rwd_amt";
	private static String OPCODE_OPERAND_START = "(";
	private static String OPCODE_OPERAND_END = ")";
	private static String OPERAND_FORMAT_PATTERN = ",";
	private IfRewardType type;
	private IfRewardAmtType amountType;
	private IfRewardFlgType flagType;
	private String name;
	private String amount;
	public static String[] OPCODE_HANDLER = {OPCODE_LABEL_FLAG, OPCODE_LABEL_AMOUNT};

	public IfRewardModel(String statement) {
		IfRewardType ifRewardType = getType(statement);
		if(ifRewardType!=null) {
			type = ifRewardType;
			if(type==IfRewardType.AMOUNT) {
				String[] amountOperand = this.parse(OPCODE_LABEL_AMOUNT, OPERAND_FORMAT_PATTERN, statement);
				name = amountOperand[0];
				amount = amountOperand[1];
				if(StringUtils.startsWith(statement, OPCODE_LABEL_AMOUNT+"_le")) {
					amountType = IfRewardAmtType.LE;
				} else if(StringUtils.startsWith(statement, OPCODE_LABEL_AMOUNT+"_lt")) {
					amountType = IfRewardAmtType.LT;
				} else if(StringUtils.startsWith(statement, OPCODE_LABEL_AMOUNT+"_eq")) {
					amountType = IfRewardAmtType.EQ;
				} else if(StringUtils.startsWith(statement, OPCODE_LABEL_AMOUNT+"_gt")) {
					amountType = IfRewardAmtType.GT;
				} else if(StringUtils.startsWith(statement, OPCODE_LABEL_AMOUNT+"_ge")) {
					amountType = IfRewardAmtType.GE;
				}
			} else if(type==IfRewardType.FLAG) {
				String[] flagOperand = this.parse(OPCODE_LABEL_FLAG, null, statement);
				name = flagOperand[0];
				if(StringUtils.startsWith(statement, OPCODE_LABEL_AMOUNT+"_set")) {
					flagType = IfRewardFlgType.SET;
				} else if(StringUtils.startsWith(statement, OPCODE_LABEL_AMOUNT+"_not_set")) {
					flagType = IfRewardFlgType.NOT_SET;
				}
			}
		}
	}
	public IfRewardType getType(String opcode) {
		if(StringUtils.startsWith(opcode, OPCODE_LABEL_FLAG))
			return IfRewardType.FLAG;
		if(StringUtils.startsWith(opcode, OPCODE_LABEL_AMOUNT))
			return IfRewardType.AMOUNT;
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
