package com.myreward.engine.event.opcode.processing;

import java.util.List;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;

import com.myreward.engine.event.processing.helper.OperationResultDO;
import com.myreward.engine.event.processing.helper.StatementOperationResult;
import com.myreward.engine.model.event.EventDO;
import com.myreward.parser.generator.MyRewardDataSegment;
import com.myreward.parser.generator.MyRewardDataSegment.EventDataObject;

public class SetRewardModel extends SetBaseModel {
	public enum StoreRewardType {
		FLAG,
		AMOUNT
	}
	private static String OPCODE_LABEL_FLAG = "set_rwd_flg";
	private static String OPCODE_LABEL_AMOUNT = "set_rwd_amt";
	private static String OPCODE_OPERAND_START = "(";
	private static String OPCODE_OPERAND_END = ")";
	private static String OPERAND_FORMAT_PATTERN = ",";
	private StoreRewardType type;
	private String name;
	private String amount;
	public static String[] OPCODE_HANDLER = {OPCODE_LABEL_FLAG, OPCODE_LABEL_AMOUNT};

	public SetRewardModel() {
	}

	public SetRewardModel(String statement) {
		StoreRewardType storePriorityType = getType(statement);
		if(storePriorityType!=null) {
			type = storePriorityType;
			if(type==StoreRewardType.AMOUNT) {
				String[] amountOperand = this.parse(OPCODE_LABEL_AMOUNT, OPERAND_FORMAT_PATTERN, statement);
				name = amountOperand[0];
				amount = amountOperand[1];
			} else if(type==StoreRewardType.FLAG) {
				String[] amountOperand = this.parse(OPCODE_LABEL_FLAG, null, statement);
				name = amountOperand[0];
			}
		}
	}
	public StoreRewardType getType(String opcode) {
		if(StringUtils.startsWith(opcode, OPCODE_LABEL_FLAG))
			return StoreRewardType.FLAG;
		if(StringUtils.startsWith(opcode, OPCODE_LABEL_AMOUNT))
			return StoreRewardType.AMOUNT;
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
		return type==StoreRewardType.FLAG?(OPCODE_LABEL_FLAG+OPCODE_OPERAND_START+name+OPCODE_OPERAND_END)
						:(OPCODE_LABEL_AMOUNT+OPCODE_OPERAND_START+name+OPERAND_FORMAT_PATTERN+amount+OPCODE_OPERAND_END);
	}

	@Override
	public OperationResultDO process(List<OpCodeBaseModel> instructionOpCodes, MyRewardDataSegment myRewardDataSegment,
			EventDO event) {
		OperationResultDO operationResultDO = new StatementOperationResult();;
		EventDataObject eventDataObject = myRewardDataSegment.search(name);
		if(eventDataObject!=null) {
			eventDataObject.setRewardStatus();
			operationResultDO.setResult(true);
			return operationResultDO;
		}
		operationResultDO.setResult(false);
		return operationResultDO;
	}

}
