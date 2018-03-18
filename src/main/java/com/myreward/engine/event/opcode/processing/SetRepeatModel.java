package com.myreward.engine.event.opcode.processing;

import java.util.List;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;

import com.myreward.engine.model.event.EventDO;
import com.myreward.engine.model.event.OperationResultDO;
import com.myreward.parser.generator.MyRewardDataSegment;


public class SetRepeatModel extends SetBaseModel {
	public enum StoreRepeatType {
		FLAG,
		AMOUNT,
		TYPE,
		DATE
	}
	private static String OPCODE_LABEL_FLAG = "set_rpt_flg";
	private static String OPCODE_LABEL_AMOUNT = "set_rpt_aft";
	private static String OPCODE_LABEL_TYPE = "set_rpt_typ";
	private static String OPCODE_LABEL_DATE = "set_rpt_dt";

	private static String OPCODE_OPERAND_START = "(";
	private static String OPCODE_OPERAND_END = ")";
	private static String OPERAND_FORMAT_PATTERN = ",";
	private StoreRepeatType type;
	private String name;
	private String amount;
	private String after;
	public static String[] OPCODE_HANDLER = {OPCODE_LABEL_FLAG, OPCODE_LABEL_AMOUNT, OPCODE_LABEL_TYPE, OPCODE_LABEL_DATE};

	public SetRepeatModel() {
	}

	public SetRepeatModel(String statement) {
		StoreRepeatType storePriorityType = getType(statement);
		if(storePriorityType!=null) {
			type = storePriorityType;
			if(type==StoreRepeatType.AMOUNT) {
				String[] amountOperand = this.parse(OPCODE_LABEL_AMOUNT, OPERAND_FORMAT_PATTERN, statement);
				name = amountOperand[0];
				amount = amountOperand[1];
			} else if(type==StoreRepeatType.FLAG) {
				String[] amountOperand = this.parse(OPCODE_LABEL_FLAG, null, statement);
				name = amountOperand[0];
			} else if(type==StoreRepeatType.TYPE) {
				String[] amountOperand = this.parse(OPCODE_LABEL_TYPE, OPERAND_FORMAT_PATTERN, statement);
				name = amountOperand[0];
				after = amountOperand[1];
			} else if(type==StoreRepeatType.DATE) {
				String[] amountOperand = this.parse(OPCODE_LABEL_DATE, null, statement);
				name = amountOperand[0];
//				after = amountOperand[1];
			}
		}
	}
	public StoreRepeatType getType(String opcode) {
		if(StringUtils.startsWith(opcode, OPCODE_LABEL_FLAG))
			return StoreRepeatType.FLAG;
		if(StringUtils.startsWith(opcode, OPCODE_LABEL_AMOUNT))
			return StoreRepeatType.AMOUNT;
		if(StringUtils.startsWith(opcode, OPCODE_LABEL_TYPE))
			return StoreRepeatType.TYPE;
		if(StringUtils.startsWith(opcode, OPCODE_LABEL_DATE))
			return StoreRepeatType.DATE;
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
		return type==StoreRepeatType.FLAG?(OPCODE_LABEL_FLAG+OPCODE_OPERAND_START+name+OPCODE_OPERAND_END)
						:(OPCODE_LABEL_AMOUNT+OPCODE_OPERAND_START+name+OPERAND_FORMAT_PATTERN+amount+OPCODE_OPERAND_END);
	}

	@Override
	public OperationResultDO process(List<OpCodeBaseModel> instructionOpCodes, MyRewardDataSegment myRewardDataSegment,
			EventDO event) {
		// TODO Auto-generated method stub
		return null;
	}

}
