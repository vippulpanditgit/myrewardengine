package com.myreward.engine.event.opcode.processing;

import java.util.Iterator;
import java.util.List;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;

import com.myreward.engine.event.error.ErrorCode;
import com.myreward.engine.event.processing.helper.IfOperationResult;
import com.myreward.engine.event.processing.helper.OperationResultDO;
import com.myreward.engine.model.event.EventDO;
import com.myreward.parser.generator.MyRewardDataSegment;

public class CallShowModel extends CallBaseModel {
	private static String OPCODE_LABEL = "call_shw";
	private static String OPCODE_OPERAND_START = "(";
	private static String OPCODE_OPERAND_END = ")";
	private static String OPERAND_FORMAT_PATTERN = ":";
	private String name;
	private String version;
	public static String[] OPCODE_HANDLER = {OPCODE_LABEL};

	public CallShowModel() {
	}

	public CallShowModel(String statement) {
		String[] operand = parse(statement);
		if(operand!=null) {
			name = operand[0];
			version = operand[1];
		}
	}
	public String[] parse(String value) {
		if(!StringUtils.startsWith(value, OPCODE_LABEL))
			return null;
		String operandValue = StringUtils.substringBetween(value, OPCODE_OPERAND_START, OPCODE_OPERAND_END);
		Pattern pattern = Pattern.compile(CallShowModel.OPERAND_FORMAT_PATTERN);
		String[] functionCallParameter = pattern.split(operandValue);
		return functionCallParameter;
	}
	@Override
	public String[] getOpcodes() {
		return OPCODE_HANDLER;
	}
	public String toString() {
		return OPCODE_LABEL+OPCODE_OPERAND_START+name+OPERAND_FORMAT_PATTERN+version+OPCODE_OPERAND_END;
	}
	   private int findOpCode(List<OpCodeBaseModel> instructionOpCodes, LabelShowModel labelShowModel) {
	    	Iterator<OpCodeBaseModel> instructionOpCodeIterator = instructionOpCodes.iterator();
	    	int index = 0;
	    	while(instructionOpCodeIterator.hasNext()) {
	    		OpCodeBaseModel opCodeBaseModel = instructionOpCodeIterator.next();
	    		if(opCodeBaseModel instanceof LabelShowModel)
	    			if(((LabelShowModel)opCodeBaseModel).equals(labelShowModel))
	    				return index;
	     		index++;
	    	}
	    	return index;
	    }
		@Override
		public OperationResultDO process(List<OpCodeBaseModel> instructionOpCodes, MyRewardDataSegment myRewardDataSegment,
				EventDO event)  throws Exception {
			OperationResultDO operationResultDO = null;
			LabelShowModel labelShowModel = new LabelShowModel(name, version);
			int callbackFunctionModelIndex = this.findOpCode(instructionOpCodes, labelShowModel);
			if(callbackFunctionModelIndex<0)
				return new ErrorOperationResultDO(ErrorCode.FUNCTION_NOT_FOUND);
			while(true) {
				OpCodeBaseModel opCodeBaseModel = instructionOpCodes.get(++callbackFunctionModelIndex);
				this.notifyProcessingListeners(opCodeBaseModel.toString());
				if(opCodeBaseModel instanceof ReturnModel)
					break;

				operationResultDO = opCodeBaseModel.process(instructionOpCodes, myRewardDataSegment, event);
				if(operationResultDO instanceof IfOperationResult) {
					int index = ((IfOperationResult)operationResultDO).getNextOperationNumber();
					if(index>0)
						callbackFunctionModelIndex += (index-1);
					else {
						while(callbackFunctionModelIndex<instructionOpCodes.size()) {
							callbackFunctionModelIndex++;
							if(instructionOpCodes.get(callbackFunctionModelIndex) instanceof ReturnModel)
								break;
						}
					}
				}
				
			}
			return operationResultDO;
		}
}
