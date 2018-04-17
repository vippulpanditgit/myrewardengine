package com.myreward.engine.event.processor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.myreward.engine.app.AppInstanceContext;
import com.myreward.engine.event.error.DebugException;
import com.myreward.engine.event.error.ErrorCode;
import com.myreward.engine.event.error.EventProcessingException;
import com.myreward.engine.event.opcode.processing.LabelMainModel;
import com.myreward.engine.event.opcode.processing.OpCodeBaseModel;
import com.myreward.engine.event.opcode.processing.ReturnModel;
import com.myreward.engine.model.event.EventDO;
import com.myreward.engine.model.event.IfOperationResult;
import com.myreward.engine.model.event.OperationResultDO;
import com.myreward.parser.generator.MyRewardDataSegment;

public class EventProcessor  implements java.io.Serializable  {
	private AppInstanceContext parentContext;
	private MetaOpCodeProcessor metaOpCodeProcessor;
	private MyRewardDataSegment myRewardDataSegment;
	private Map<String, Integer> fnXref = new HashMap<String, Integer>();
	private List<OpCodeBaseModel> instructionOpCodes = new ArrayList<OpCodeBaseModel>();

	
	public EventProcessor(AppInstanceContext parentContext, MetaOpCodeProcessor metaOpCodeProcessor) {
		this.parentContext = parentContext;
		this.metaOpCodeProcessor = metaOpCodeProcessor;
		
	}
	public EventProcessor(AppInstanceContext parentContext, MetaOpCodeProcessor metaOpCodeProcessor, MyRewardDataSegment myRewardDataSegment) {
		this.parentContext = parentContext;
		this.metaOpCodeProcessor = metaOpCodeProcessor;
		this.myRewardDataSegment = myRewardDataSegment;
	}

		
    private int findMainOpCode() {
    	Iterator<OpCodeBaseModel> instructionOpCodeIterator = instructionOpCodes.iterator();
    	int index = 0;
    	while(instructionOpCodeIterator.hasNext()) {
    		OpCodeBaseModel opCodeBaseModel = instructionOpCodeIterator.next();
    		if(opCodeBaseModel instanceof LabelMainModel)
    			return index;
     		index++;
    	}
    	return index;
    }

 	public boolean process_event(EventDO eventDO) throws Exception {
 		
		if(metaOpCodeProcessor.getMyRewardPCodeGenerator()==null)
			throw new EventProcessingException(ErrorCode.NO_PCODE_GENERATED);
		if(this.myRewardDataSegment==null)
			throw new EventProcessingException(ErrorCode.DATASEGMENT_NOT_INITIALIZED);
		int lbl_main_index = findMainOpCode();
		if(lbl_main_index==0)
			throw new EventProcessingException(ErrorCode.LABEL_MAIN_NOT_FOUND);
		if(lbl_main_index==this.instructionOpCodes.size())
			throw new EventProcessingException(ErrorCode.LABEL_MAIN_NOT_FOUND);
		//Testing - VP
		try {
			step(lbl_main_index, eventDO);
		} catch (Exception e) {
			throw e;
		}
		return true;
	}
 	public int step(int currentOpCodeIndex, EventDO eventDO) throws Exception {
		while(true) {
			if(currentOpCodeIndex < instructionOpCodes.size()-1)
				currentOpCodeIndex++;
			OpCodeBaseModel opCodeBaseModel = instructionOpCodes.get(currentOpCodeIndex);
			if(opCodeBaseModel instanceof ReturnModel)
				break;
			OperationResultDO operationResultDO = null;
			try {
				operationResultDO = opCodeBaseModel.process(instructionOpCodes, myRewardDataSegment, eventDO);
			} catch (DebugException debugException) {
				debugException.opCodeIndex = currentOpCodeIndex;
				debugException.eventDO = eventDO;
				debugException.myRewardDataSegment = myRewardDataSegment;
				throw debugException;
			}
			if(operationResultDO instanceof IfOperationResult) {
				int index = ((IfOperationResult)operationResultDO).getNextOperationNumber();
				if(index>0)
					currentOpCodeIndex += (index-1);
				else {
					while(currentOpCodeIndex<instructionOpCodes.size()) {
						currentOpCodeIndex++;
						if(instructionOpCodes.get(currentOpCodeIndex) instanceof ReturnModel)
							break;
					}
				}
			}
		}
		return currentOpCodeIndex;
	}

	public List<OpCodeBaseModel> getInstructionOpCodes() {
		return instructionOpCodes;
	}

	public void setInstructionOpCodes(List<OpCodeBaseModel> instructionOpCodes) {
		this.instructionOpCodes = instructionOpCodes;
	}

	public MyRewardDataSegment getMyRewardDataSegment() {
		return myRewardDataSegment;
	}

	public void setMyRewardDataSegment(MyRewardDataSegment myRewardDataSegment) {
		this.myRewardDataSegment = myRewardDataSegment;
	}
}
