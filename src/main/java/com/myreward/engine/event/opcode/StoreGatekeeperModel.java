package com.myreward.engine.event.opcode;

import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;

public class StoreGatekeeperModel extends StoreBaseModel {
	public enum StoreGatekeeperType {
		FLAG,
		AMOUNT
	}
	private static String OPCODE_LABEL_FLAG = "store_gtk_flg";
	private static String OPCODE_LABEL_AMOUNT = "store_gtk_amt";
	private static String OPCODE_OPERAND_START = "(";
	private static String OPCODE_OPERAND_END = ")";
	private static String OPERAND_FORMAT_PATTERN = ",";
	private StoreGatekeeperType type;
	private String name;
	private String amount;

	public StoreGatekeeperModel(String statement) {
		StoreGatekeeperType storePriorityType = getType(statement);
		if(storePriorityType!=null) {
			type = storePriorityType;
			if(type==StoreGatekeeperType.AMOUNT) {
				String[] amountOperand = this.parse(OPCODE_LABEL_AMOUNT, OPERAND_FORMAT_PATTERN, statement);
				name = amountOperand[0];
				amount = amountOperand[1];
			} else if(type==StoreGatekeeperType.FLAG) {
				String[] amountOperand = this.parse(OPCODE_LABEL_AMOUNT, null, statement);
				name = amountOperand[0];
			}
		}
	}
	public StoreGatekeeperType getType(String opcode) {
		if(StringUtils.startsWith(opcode, OPCODE_LABEL_FLAG))
			return StoreGatekeeperType.FLAG;
		if(StringUtils.startsWith(opcode, OPCODE_LABEL_AMOUNT))
			return StoreGatekeeperType.AMOUNT;
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
