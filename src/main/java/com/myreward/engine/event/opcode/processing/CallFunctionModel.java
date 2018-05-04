package com.myreward.engine.event.opcode.processing;

import java.util.Iterator;
import java.util.List;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;

import com.myreward.engine.event.error.ErrorCode;
import com.myreward.engine.model.event.EventDO;
import com.myreward.engine.model.event.IfOperationResult;
import com.myreward.engine.model.event.OperationResultDO;
import com.myreward.parser.generator.MyRewardDataSegment;

public class CallFunctionModel extends CallBaseModel {
	public static String OPCODE_LABEL = "call_fn";
	private static String OPCODE_OPERAND_START = "(";
	private static String OPCODE_OPERAND_END = ")";
	private static String OPERAND_FORMAT_PATTERN = ":";
	private String name;
	private String version;
	public static String[] OPCODE_HANDLER = {OPCODE_LABEL};
	
	public CallFunctionModel() {
		super();
	}

	public CallFunctionModel(String statement) {
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
		Pattern pattern = Pattern.compile(OPERAND_FORMAT_PATTERN);
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
    private int findOpCode(List<OpCodeBaseModel> instructionOpCodes, LabelFunctionModel labelFunctionModel) {
    	Iterator<OpCodeBaseModel> instructionOpCodeIterator = instructionOpCodes.iterator();
    	int index = 0;
    	while(instructionOpCodeIterator.hasNext()) {
    		OpCodeBaseModel opCodeBaseModel = instructionOpCodeIterator.next();
    		if(opCodeBaseModel instanceof LabelFunctionModel)
    			if(((LabelFunctionModel)opCodeBaseModel).equals(labelFunctionModel))
    				return index;
     		index++;
    	}
    	return index;
    }
	public OperationResultDO process(List<OpCodeBaseModel> instructionOpCodes, MyRewardDataSegment myRewardDataSegment, EventDO event)  throws Exception{
		OperationResultDO operationResultDO = null;
		LabelFunctionModel labelFunctionModel = new LabelFunctionModel(name, version);
		int callbackFunctionModelIndex = this.findOpCode(instructionOpCodes, labelFunctionModel);
//		if(callbackFunctionModelIndex==0)
//			return new ErrorOperationResultDO(ErrorCode.FUNCTION_NOT_FOUND);
		while(true) {
			OpCodeBaseModel opCodeBaseModel = instructionOpCodes.get(++callbackFunctionModelIndex);
//System.out.println(opCodeBaseModel);
			if(opCodeBaseModel instanceof ReturnModel) {
				operationResultDO = new ReturnModel().process(instructionOpCodes, myRewardDataSegment, null);
				break;
			}

			operationResultDO = opCodeBaseModel.process(instructionOpCodes, myRewardDataSegment, event);
			if(operationResultDO instanceof IfOperationResult) {
				int index = ((IfOperationResult)operationResultDO).getNextOperationNumber();
				if(index>0)
					callbackFunctionModelIndex += (index-1);
				else {
					while(callbackFunctionModelIndex<instructionOpCodes.size()) {
						callbackFunctionModelIndex++;
						if(instructionOpCodes.get(callbackFunctionModelIndex) instanceof ReturnModel) {
							operationResultDO = new ReturnModel().process(instructionOpCodes, myRewardDataSegment, null);
							break;
						}
					}
				}
			}
			
		}
		return operationResultDO;
	}
}
