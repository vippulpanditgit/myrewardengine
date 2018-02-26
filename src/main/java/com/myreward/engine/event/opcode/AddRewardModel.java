package com.myreward.engine.event.opcode;

import java.util.List;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;

import com.myreward.engine.model.event.EventDO;
import com.myreward.engine.model.event.OperationResultDO;
import com.myreward.engine.model.event.StatementOperationResult;
import com.myreward.parser.generator.MyRewardDataSegment;
import com.myreward.parser.generator.MyRewardDataSegment.EventDataObject;

public class AddRewardModel extends OpCodeBaseModel {
	private static final String ARGUMENT_SEPERATOR = ",";
	private static String OPCODE_LABEL_AMOUNT = "add_rwd_amt";
	private static String OPCODE_OPERAND_START = "(";
	private static String OPCODE_OPERAND_END = ")";
	private static String OPERAND_FORMAT_PATTERN = ",";
	private String name;
	private String amount;
	public static String[] OPCODE_HANDLER = {OPCODE_LABEL_AMOUNT};

	public AddRewardModel() {
	}

	public AddRewardModel(String statement) {
		String[] amountOperand = this.parse(OPCODE_LABEL_AMOUNT, OPERAND_FORMAT_PATTERN, statement);
		if(amountOperand.length==2) {
			name = amountOperand[0];
			amount = amountOperand[1];
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

	@Override
	public OperationResultDO process(List<OpCodeBaseModel> instructionOpCodes, MyRewardDataSegment myRewardDataSegment,
			EventDO event) {
		OperationResultDO operationResultDO = new StatementOperationResult();;
		EventDataObject eventDataObject = myRewardDataSegment.search(name);
		if(eventDataObject!=null) {
			eventDataObject.amount += Integer.valueOf(amount);
			operationResultDO.setResult(true);
			return operationResultDO;
		}
		operationResultDO.setResult(false);
		return operationResultDO;
	}

}
