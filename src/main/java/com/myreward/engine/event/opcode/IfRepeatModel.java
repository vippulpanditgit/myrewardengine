package com.myreward.engine.event.opcode;

import java.util.List;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;

import com.myreward.engine.event.opcode.IfDurationModel.IfDurationType;
import com.myreward.engine.model.event.EventDO;
import com.myreward.engine.model.event.OperationResultDO;
import com.myreward.parser.generator.MyRewardDataSegment;

public class IfRepeatModel extends IfBaseModel {
	public enum IfRepeatType {
		FLAG,
		AMOUNT
	}
	public enum IfRepeatAmtType {
		LE("_LE"),
		LT("_LT"),
		EQ("_EQ"),
		GT("_GT"),
		GE("_GE");
		
		private final String value;
		IfRepeatAmtType(String value) {
			this.value = value;
		}
		public String value(){
			return this.value;
		}
	}
	public enum IfRepeatFlgType {
		SET("_set"),
		NOT_SET("_not_set");
		
		private final String value;
		IfRepeatFlgType(String value) {
			this.value = value;
		}
		public String value(){
			return this.value;
		}
	}

	private static String OPCODE_LABEL_FLAG = "if_rpt_flg";
	private static String OPCODE_LABEL_AMOUNT = "if_rpt_amt";
	private static String OPCODE_OPERAND_START = "(";
	private static String OPCODE_OPERAND_END = ")";
	private static String OPERAND_FORMAT_PATTERN = ",";
	private IfRepeatType type;
	private IfRepeatAmtType amountType;
	private IfRepeatFlgType flagType;
	private String name;
	private String amount;
	public static String[] OPCODE_HANDLER = {OPCODE_LABEL_FLAG, OPCODE_LABEL_AMOUNT};

	public IfRepeatModel() {
		
	}

	public IfRepeatModel(String statement) {
		IfRepeatType ifRewardType = getType(statement);
		if(ifRewardType!=null) {
			type = ifRewardType;
			if(type==IfRepeatType.AMOUNT) {
				String[] amountOperand = this.parse(OPCODE_LABEL_AMOUNT, OPERAND_FORMAT_PATTERN, statement);
				name = amountOperand[0];
				amount = amountOperand[1];
				if(StringUtils.startsWith(statement, OPCODE_LABEL_AMOUNT+IfRepeatAmtType.LE)) {
					amountType = IfRepeatAmtType.LE;
				} else if(StringUtils.startsWith(statement, OPCODE_LABEL_AMOUNT+IfRepeatAmtType.LT)) {
					amountType = IfRepeatAmtType.LT;
				} else if(StringUtils.startsWith(statement, OPCODE_LABEL_AMOUNT+IfRepeatAmtType.EQ)) {
					amountType = IfRepeatAmtType.EQ;
				} else if(StringUtils.startsWith(statement, OPCODE_LABEL_AMOUNT+IfRepeatAmtType.GT)) {
					amountType = IfRepeatAmtType.GT;
				} else if(StringUtils.startsWith(statement, OPCODE_LABEL_AMOUNT+IfRepeatAmtType.GE)) {
					amountType = IfRepeatAmtType.GE;
				}
			} else if(type==IfRepeatType.FLAG) {
				String[] flagOperand = this.parse(OPCODE_LABEL_FLAG, null, statement);
				name = flagOperand[0];
				if(StringUtils.startsWith(statement, OPCODE_LABEL_FLAG+IfRepeatFlgType.SET.value)) {
					flagType = IfRepeatFlgType.SET;
				} else if(StringUtils.startsWith(statement, OPCODE_LABEL_FLAG+IfRepeatFlgType.NOT_SET.value)) {
					flagType = IfRepeatFlgType.NOT_SET;
				}
			}
		}
	}
	public IfRepeatType getType(String opcode) {
		if(StringUtils.startsWith(opcode, OPCODE_LABEL_FLAG))
			return IfRepeatType.FLAG;
		if(StringUtils.startsWith(opcode, OPCODE_LABEL_AMOUNT))
			return IfRepeatType.AMOUNT;
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
		if(type==IfRepeatType.FLAG) {
			return OPCODE_LABEL_FLAG+flagType.value()+OPCODE_OPERAND_START+name+OPCODE_OPERAND_END;
		}
		if(type==IfRepeatType.AMOUNT) {
			return OPCODE_LABEL_AMOUNT+amountType.value()+OPCODE_OPERAND_START+name+OPERAND_FORMAT_PATTERN+amount+OPCODE_OPERAND_END;
		}
		return null;
	}

	@Override
	public OperationResultDO process(List<OpCodeBaseModel> instructionOpCodes, MyRewardDataSegment myRewardDataSegment,
			EventDO event) {
		System.out.println("Not Implmented - IfRepeatModel.process");
		return null;
	}
}
