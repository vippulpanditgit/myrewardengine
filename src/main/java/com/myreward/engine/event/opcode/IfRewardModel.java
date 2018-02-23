package com.myreward.engine.event.opcode;

import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;

import com.myreward.engine.event.opcode.IfDurationModel.IfDurationType;

public class IfRewardModel extends IfBaseModel {
	public enum IfRewardType {
		FLAG,
		AMOUNT
	}
	public enum IfRewardAmtType {
		LE("_LE"),
		LT("_LT"),
		EQ("_EQ"),
		GT("_GT"),
		GE("_GE");
		
		private final String value;
		IfRewardAmtType(String value) {
			this.value = value;
		}
		public String value(){
			return this.value;
		}
	}
	public enum IfRewardFlgType {
		SET("_set"),
		NOT_SET("_not_set");
		
		private final String value;
		IfRewardFlgType(String value) {
			this.value = value;
		}
		public String value(){
			return this.value;
		}
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

	public IfRewardModel() {
		
	}

	public IfRewardModel(String statement) {
		IfRewardType ifRewardType = getType(statement);
		if(ifRewardType!=null) {
			type = ifRewardType;
			if(type==IfRewardType.AMOUNT) {
				String[] amountOperand = this.parse(OPCODE_LABEL_AMOUNT, OPERAND_FORMAT_PATTERN, statement);
				name = amountOperand[0];
				amount = amountOperand[1];
				if(StringUtils.startsWith(statement, OPCODE_LABEL_AMOUNT+IfRewardAmtType.LE)) {
					amountType = IfRewardAmtType.LE;
				} else if(StringUtils.startsWith(statement, OPCODE_LABEL_AMOUNT+IfRewardAmtType.LT)) {
					amountType = IfRewardAmtType.LT;
				} else if(StringUtils.startsWith(statement, OPCODE_LABEL_AMOUNT+IfRewardAmtType.EQ)) {
					amountType = IfRewardAmtType.EQ;
				} else if(StringUtils.startsWith(statement, OPCODE_LABEL_AMOUNT+IfRewardAmtType.GT)) {
					amountType = IfRewardAmtType.GT;
				} else if(StringUtils.startsWith(statement, OPCODE_LABEL_AMOUNT+IfRewardAmtType.GE)) {
					amountType = IfRewardAmtType.GE;
				}
			} else if(type==IfRewardType.FLAG) {
				String[] flagOperand = this.parse(OPCODE_LABEL_FLAG, null, statement);
				name = flagOperand[0];
				if(StringUtils.startsWith(statement, OPCODE_LABEL_FLAG+IfRewardFlgType.SET.value)) {
					flagType = IfRewardFlgType.SET;
				} else if(StringUtils.startsWith(statement, OPCODE_LABEL_FLAG+IfRewardFlgType.NOT_SET.value)) {
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
	@Override
	public String[] getOpcodes() {
		return OPCODE_HANDLER;
	}
	public String toString() {
		if(type==IfRewardType.FLAG) {
			return OPCODE_LABEL_FLAG+flagType.value+OPCODE_OPERAND_START+name+OPCODE_OPERAND_END;
		}
		if(type==IfRewardType.AMOUNT) {
			return OPCODE_LABEL_AMOUNT+amountType.value+OPCODE_OPERAND_START+name+OPERAND_FORMAT_PATTERN+amount+OPCODE_OPERAND_END;
		}
		return null;
	}
}
