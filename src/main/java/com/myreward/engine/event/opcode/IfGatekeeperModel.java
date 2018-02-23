package com.myreward.engine.event.opcode;

import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;

public class IfGatekeeperModel extends IfBaseModel {
	public enum IfGatekeeperType {
		FLAG,
		AMOUNT
	}
	public enum IfGatekeeperAmtType {
		LE,
		LT,
		EQ,
		GT,
		GE
	}
	public enum IfGatekeeperFlgType {
		SET,
		NOT_SET
	}

	private static String OPCODE_LABEL_FLAG = "if_gtk_flg";
	private static String OPCODE_LABEL_AMOUNT = "if_gtk_amt";
	private static String OPCODE_OPERAND_START = "(";
	private static String OPCODE_OPERAND_END = ")";
	private static String OPERAND_FORMAT_PATTERN = ",";
	private IfGatekeeperType type;
	private IfGatekeeperAmtType amountType;
	private IfGatekeeperFlgType flagType;
	private String name;
	private String amount;
	public static String[] OPCODE_HANDLER = {OPCODE_LABEL_FLAG, OPCODE_LABEL_AMOUNT};

	public IfGatekeeperModel() {
		
	}

	public IfGatekeeperModel(String statement) {
		IfGatekeeperType ifGatekeeperType = getType(statement);
		if(ifGatekeeperType!=null) {
			type = ifGatekeeperType;
			if(type==IfGatekeeperType.AMOUNT) {
				String[] amountOperand = this.parse(OPCODE_LABEL_AMOUNT, OPERAND_FORMAT_PATTERN, statement);
				name = amountOperand[0];
				amount = amountOperand[1];
				if(StringUtils.startsWith(statement, OPCODE_LABEL_AMOUNT+"_le")) {
					amountType = IfGatekeeperAmtType.LE;
				} else if(StringUtils.startsWith(statement, OPCODE_LABEL_AMOUNT+"_lt")) {
					amountType = IfGatekeeperAmtType.LT;
				} else if(StringUtils.startsWith(statement, OPCODE_LABEL_AMOUNT+"_eq")) {
					amountType = IfGatekeeperAmtType.EQ;
				} else if(StringUtils.startsWith(statement, OPCODE_LABEL_AMOUNT+"_gt")) {
					amountType = IfGatekeeperAmtType.GT;
				} else if(StringUtils.startsWith(statement, OPCODE_LABEL_AMOUNT+"_ge")) {
					amountType = IfGatekeeperAmtType.GE;
				}
			} else if(type==IfGatekeeperType.FLAG) {
				String[] flagOperand = this.parse(OPCODE_LABEL_FLAG, null, statement);
				name = flagOperand[0];
				if(StringUtils.startsWith(statement, OPCODE_LABEL_FLAG+"_set")) {
					flagType = IfGatekeeperFlgType.SET;
				} else if(StringUtils.startsWith(statement, OPCODE_LABEL_FLAG+"_not_set")) {
					flagType = IfGatekeeperFlgType.NOT_SET;
				}
			}
		}
	}
	public IfGatekeeperType getType(String opcode) {
		if(StringUtils.startsWith(opcode, OPCODE_LABEL_FLAG))
			return IfGatekeeperType.FLAG;
		if(StringUtils.startsWith(opcode, OPCODE_LABEL_AMOUNT))
			return IfGatekeeperType.AMOUNT;
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
