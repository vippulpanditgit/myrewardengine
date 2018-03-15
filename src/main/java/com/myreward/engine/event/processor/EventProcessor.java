package com.myreward.engine.event.processor;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.myreward.parser.generator.MyRewardDataSegment;
import com.myreward.engine.audit.AuditEvent;
import com.myreward.engine.audit.AuditEventType;
import com.myreward.engine.audit.AuditManager;
import com.myreward.engine.event.error.DebugException;
import com.myreward.engine.event.error.ErrorCode;
import com.myreward.engine.event.error.EventProcessingException;
import com.myreward.engine.event.error.MetaDataCreationException;
import com.myreward.engine.event.opcode.*;
import com.myreward.engine.model.event.EventDO;
import com.myreward.engine.model.event.IfOperationResult;
import com.myreward.engine.model.event.OperationResultDO;

public class EventProcessor {
	private MetaOpCodeProcessor metaOpCodeProcessor;
	private MyRewardDataSegment myRewardDataSegment;
	private Map<String, Integer> fnXref = new HashMap<String, Integer>();
	private List<OpCodeBaseModel> instructionOpCodes = new ArrayList<OpCodeBaseModel>();
	private List<OpCodeBaseModel> opCodeList = Arrays.asList(new CallFunctionModel(),
											new CallGatekeeperModel(),
											new CallPriorityModel(),
											new CallRepeatModel(),
											new CallRewardModel(),
											new CallShowModel(),
											new CallDurationModel(),
											new IfDurationModel(),
											new IfEventModel(),
											new IfGatekeeperModel(),
											new IfRewardModel(),
											new IfRepeatModel(),
											new LabelDurationModel(),
											new LabelFunctionModel(),
											new LabelGatekeeperModel(),
											new LabelMainModel(),
											new LabelPriorityModel(),
											new LabelRepeatModel(),
											new LabelRewardModel(),
											new LabelShowModel(),
											new SetGatekeeperModel(),
											new SetPriorityModel(),
											new SetRewardModel(),
											new SetShowModel(),
											new SetDurationModel(),
											new ReturnModel(),
											new ResetDurationModel(),
											new ResetGatekeeperModel(),
											new ResetPriorityModel(),
											new ResetRewardModel(),
											new ResetShowModel(),
											new ResetEventModel(),
											new SetRepeatModel(),
											new SetEventModel(),
											new AddRewardModel(),
											new IncrementEventModel(),
											new DescriptionModel(),
											new DebugModel());
	
	public EventProcessor(MetaOpCodeProcessor metaOpCodeProcessor) {
		this.metaOpCodeProcessor = metaOpCodeProcessor;
		
	}
	public EventProcessor(MetaOpCodeProcessor metaOpCodeProcessor, MyRewardDataSegment myRewardDataSegment) {
		this.metaOpCodeProcessor = metaOpCodeProcessor;
		this.myRewardDataSegment = myRewardDataSegment;
	}
	public void create_meta_tree() throws EventProcessingException, MetaDataCreationException {
		AuditManager.getInstance().audit(new AuditEvent(null, AuditEventType.AUDIT_EVENT_CREATE_META_DATA_TREE_START, null));
		if(metaOpCodeProcessor.getMyRewardPCodeGenerator()==null)
			throw new EventProcessingException(ErrorCode.EVENT_METADATA_NOT_PRESET);
		if(metaOpCodeProcessor.getMyRewardPCodeGenerator().getCodeSegment()!=null
				&& metaOpCodeProcessor.getMyRewardPCodeGenerator().getCodeSegment().size()>0) {
			Iterator<String> codeSegmentIterator = metaOpCodeProcessor.getMyRewardPCodeGenerator().getCodeSegment().iterator();
			int index=0;
			while(codeSegmentIterator.hasNext()) {
				boolean isOpcodeFound = false;
				String opcode = codeSegmentIterator.next();
				Iterator<OpCodeBaseModel> opCodeBaseModelIterator = opCodeList.iterator();
				while(opCodeBaseModelIterator.hasNext()) {
					OpCodeBaseModel opCodeBaseModel = opCodeBaseModelIterator.next();
					String[] opCodeHandler = opCodeBaseModel.getOpcodes();
					for(int opCodeIndex=0;opCodeIndex<opCodeHandler.length;opCodeIndex++) {
//						System.out.println(opCodeHandler[opCodeIndex]);
						if(opcode.length()>=opCodeHandler[opCodeIndex].length() && 
								opCodeHandler[opCodeIndex].equalsIgnoreCase(opcode.substring(0, opCodeHandler[opCodeIndex].length()))) {
							try {
								Constructor constructor = opCodeBaseModel.getClass().getConstructor(new Class[] { String.class});
								OpCodeBaseModel realInstance = (OpCodeBaseModel) constructor.newInstance(new Object[] { opcode });
								instructionOpCodes.add(realInstance);
								isOpcodeFound = true;
//								System.out.println("Test");
								break;
							} catch(Exception exp){
								throw new MetaDataCreationException("Exception creating Metadata tree", exp);
							}
						}
					}
				}
				if(!isOpcodeFound)
					throw new MetaDataCreationException("Exception creating Metadata tree", new Exception("Opcode Handler not found- "+opcode));
				index++;
			}
		}
		AuditManager.getInstance().audit(new AuditEvent(null, AuditEventType.AUDIT_EVENT_CREATE_META_DATA_TREE_END, null));
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
System.out.println(opCodeBaseModel);
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
