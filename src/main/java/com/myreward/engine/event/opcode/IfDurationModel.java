package com.myreward.engine.event.opcode;

import java.util.List;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;

import com.myreward.engine.event.opcode.IfEventModel.IfCompletionType;
import com.myreward.engine.event.opcode.StorePriorityModel.StorePriorityType;
import com.myreward.engine.model.event.EventDO;
import com.myreward.engine.model.event.IfOperationResult;
import com.myreward.engine.model.event.OperationResultDO;
import com.myreward.parser.generator.MyRewardDataSegment;
import com.myreward.parser.generator.MyRewardDataSegment.EventDataObject;

public class IfDurationModel extends IfBaseModel {
	public enum IfDurationType {
		FLAG,
		AMOUNT
	}
	public enum IfDurationAmtType {
		LE("_LE"),
		LT("_LT"),
		EQ("_EQ"),
		GT("_GT"),
		GE("_GE");
		
		private final String value;
		IfDurationAmtType(String value) {
			this.value = value;
		}
		public String value(){
			return this.value;
		}
	}
	public enum IfDurationFlgType {
		SET("_set"),
		NOT_SET("_not_set");
		
		private final String value;
		IfDurationFlgType(String value) {
			this.value = value;
		}
		public String value(){
			return this.value;
		}
	}

	private static String OPCODE_LABEL_FLAG = "if_dur_flg";
	private static String OPCODE_LABEL_AMOUNT = "if_dur_amt";
	private static String OPCODE_OPERAND_START = "(";
	private static String OPCODE_OPERAND_END = ")";
	private static String OPERAND_FORMAT_PATTERN = ",";
	private IfDurationType type;
	private IfDurationAmtType amountType;
	private IfDurationFlgType flagType;
	private String name;
	private String amount;
	private int gotoLine = -1;
	public static String[] OPCODE_HANDLER = {OPCODE_LABEL_FLAG, OPCODE_LABEL_AMOUNT};

	public IfDurationModel() {
		
	}
	public IfDurationModel(String statement) {
		IfDurationType ifDurationType = getType(statement);
		if(ifDurationType!=null) {
			type = ifDurationType;
			if(type==IfDurationType.AMOUNT) {
				String[] amountOperand = this.parse(OPCODE_LABEL_AMOUNT, OPERAND_FORMAT_PATTERN, statement);
				name = amountOperand[0];
				amount = amountOperand[1];
				if(StringUtils.startsWith(statement, OPCODE_LABEL_AMOUNT+IfDurationAmtType.LE.value())) {
					amountType = IfDurationAmtType.LE;
				} else if(StringUtils.startsWith(statement, OPCODE_LABEL_AMOUNT+IfDurationAmtType.LT.value())) {
					amountType = IfDurationAmtType.LT;
				} else if(StringUtils.startsWith(statement, OPCODE_LABEL_AMOUNT+IfDurationAmtType.EQ.value())) {
					amountType = IfDurationAmtType.EQ;
				} else if(StringUtils.startsWith(statement, OPCODE_LABEL_AMOUNT+IfDurationAmtType.GT.value())) {
					amountType = IfDurationAmtType.GT;
				} else if(StringUtils.startsWith(statement, OPCODE_LABEL_AMOUNT+IfDurationAmtType.GE.value())) {
					amountType = IfDurationAmtType.GE;
				}
			} else if(type==IfDurationType.FLAG) {
				String[] flagOperand = this.parse(OPCODE_LABEL_FLAG, null, statement);
				if(flagOperand.length>0)
					if(StringUtils.contains(flagOperand[0], OPERAND_FORMAT_PATTERN)) {
						flagOperand = StringUtils.split(flagOperand[0], OPERAND_FORMAT_PATTERN);
						if(flagOperand.length==1)
							name = flagOperand[0];
						if(flagOperand.length==2) {
							name = flagOperand[0];
							gotoLine = Integer.valueOf(flagOperand[1]);
						}
						if(flagOperand.length==3) {
							name = flagOperand[0];
							gotoLine = Integer.valueOf(flagOperand[2]);
						}
						
					} else 
						name = flagOperand[0];
				if(StringUtils.startsWith(statement, OPCODE_LABEL_FLAG+IfDurationFlgType.SET.value())) {
					flagType = IfDurationFlgType.SET;
				} else if(StringUtils.startsWith(statement, OPCODE_LABEL_FLAG+IfDurationFlgType.NOT_SET.value())) {
					flagType = IfDurationFlgType.NOT_SET;
				}
			}
		}
	}
	public IfDurationType getType(String opcode) {
		if(StringUtils.startsWith(opcode, OPCODE_LABEL_FLAG))
			return IfDurationType.FLAG;
		if(StringUtils.startsWith(opcode, OPCODE_LABEL_AMOUNT))
			return IfDurationType.AMOUNT;
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
		if(type==IfDurationType.FLAG) {
			return OPCODE_LABEL_FLAG+flagType.value+OPCODE_OPERAND_START+name+OPCODE_OPERAND_END;
		}
		if(type==IfDurationType.AMOUNT) {
			return OPCODE_LABEL_AMOUNT+amountType.value+OPCODE_OPERAND_START+name+OPERAND_FORMAT_PATTERN+amount+OPCODE_OPERAND_END;
		}
		return null;
	}
	@Override
	public OperationResultDO process(List<OpCodeBaseModel> instructionOpCodes, MyRewardDataSegment myRewardDataSegment,
			EventDO event) {
		OperationResultDO operationResultDO = null;
		if(type==IfDurationType.FLAG) {
			operationResultDO = new IfOperationResult();;
			EventDataObject eventDataObject = myRewardDataSegment.search(name);
			if(eventDataObject!=null) {
				if(flagType == IfDurationFlgType.SET) {
					if(eventDataObject.isDurationFlagSet()) {
						((IfOperationResult)operationResultDO).setResult(true);
						((IfOperationResult)operationResultDO).setNextOperationNumber(1);
					} else {
						((IfOperationResult)operationResultDO).setResult(false);
						((IfOperationResult)operationResultDO).setNextOperationNumber(gotoLine);
					}
				} else if(flagType == IfDurationFlgType.NOT_SET) {
					if(eventDataObject.isDurationFlagSet()) {
						((IfOperationResult)operationResultDO).setResult(false);
						((IfOperationResult)operationResultDO).setNextOperationNumber(gotoLine);
					} else {
						((IfOperationResult)operationResultDO).setResult(true);
						((IfOperationResult)operationResultDO).setNextOperationNumber(1);
					}
				}
				return operationResultDO;
			}
			operationResultDO.setResult(false);
			return operationResultDO;
		}
		if(type==IfDurationType.AMOUNT) {
		}
		return operationResultDO;
	}

}
