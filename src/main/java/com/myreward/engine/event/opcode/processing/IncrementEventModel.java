package com.myreward.engine.event.opcode.processing;

import java.util.List;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;

import com.myreward.engine.event.processing.helper.OperationResultDO;
import com.myreward.engine.event.processing.helper.StatementOperationResult;
import com.myreward.engine.model.event.EventDO;
import com.myreward.parser.generator.MyRewardDataSegment;
import com.myreward.parser.generator.MyRewardDataSegment.EventDataObject;

public class IncrementEventModel extends OpCodeBaseModel {
	private static final String ARGUMENT_SEPERATOR = ",";
	private static String OPCODE_LABEL = "inc_cmp_cnt";
	private static String OPCODE_OPERAND_START = "(";
	private static String OPCODE_OPERAND_END = ")";
	private static String OPERAND_FORMAT_PATTERN = ",";
	private String name;
	private String amount;
	public static String[] OPCODE_HANDLER = {OPCODE_LABEL};

	public IncrementEventModel() {
	}

	public IncrementEventModel(String statement) {
		String[] amountOperand = this.parse(OPCODE_LABEL, OPERAND_FORMAT_PATTERN, statement);
		if(amountOperand.length==2) {
			name = amountOperand[0];
			amount = amountOperand[1];
		} else if(amountOperand.length==1) {
			name = amountOperand[0];
			amount = "1";
		}
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
		return OPCODE_LABEL+this.OPCODE_OPERAND_START+name+this.OPCODE_OPERAND_END;
	}
	@Override
	public OperationResultDO process(List<OpCodeBaseModel> instructionOpCodes, MyRewardDataSegment myRewardDataSegment, EventDO event) {
		OperationResultDO operationResultDO = new StatementOperationResult();;
		EventDataObject eventDataObject = myRewardDataSegment.search(name);
		if(eventDataObject!=null) {
			eventDataObject.eventCount += Integer.valueOf(amount);
			operationResultDO.setResult(true);
			return operationResultDO;
		}
		operationResultDO.setResult(false);
		return operationResultDO;
	
	}

}
