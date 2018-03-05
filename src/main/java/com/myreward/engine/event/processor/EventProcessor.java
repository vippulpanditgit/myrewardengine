package com.myreward.engine.event.processor;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.myreward.parser.generator.MyRewardDataSegment;
import com.myreward.parser.generator.MyRewardPCodeGenerator;
import com.myreward.parser.grammar.MyRewardParser;
import com.myreward.parser.util.RuntimeLib;
import com.myreward.engine.event.opcode.*;
import com.myreward.engine.model.event.EventDO;
import com.myreward.engine.model.event.IfOperationResult;
import com.myreward.engine.model.event.OperationResultDO;

public class EventProcessor {
	private MyRewardPCodeGenerator myRewardCodeGenerator;
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
											new IncrementEventModel());
	
	public void readPCode(MyRewardPCodeGenerator myRewardCodeGenerator) {
		this.myRewardCodeGenerator = myRewardCodeGenerator;
	}

	public void preprocess() {
		if(this.myRewardCodeGenerator==null)
			return;
		if(this.myRewardCodeGenerator.getCodeSegment()!=null
				&& this.myRewardCodeGenerator.getCodeSegment().size()>0) {
			Iterator<String> codeSegmentIterator = this.myRewardCodeGenerator.getCodeSegment().iterator();
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
								exp.printStackTrace();
							}
						}
					}
				}
				if(!isOpcodeFound)
					System.out.println(opcode);
				index++;
			}
		}
	}
	
    public static void main(String[] args) {
        MyRewardPCodeGenerator test = new MyRewardPCodeGenerator();
        test.getCodeSegment().add("lbl_dur:65:0");
        test.getCodeSegment().add("if_evt_dt_le(869077230045)");
        EventProcessor eventProcessor = new EventProcessor();
        eventProcessor.readPCode(test);
        eventProcessor.preprocess();
        
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
    public MyRewardDataSegment createDataSegment() {
        // Create the data segment
        myRewardDataSegment.processDataSegment(MyRewardParser.symbolTable);
        // Copy the data segment
        MyRewardDataSegment myRewardDataSegmentClone = (MyRewardDataSegment) RuntimeLib.deepClone(myRewardDataSegment);
        return myRewardDataSegmentClone;

    }
	public boolean run() {
		if(this.myRewardCodeGenerator==null)
			return false;
		if(this.myRewardDataSegment==null)
			return false;
		int mainIndex = findMainOpCode();
		if(mainIndex==0)
			return false;
		// read event
		EventDO eventDO = new EventDO();
		//Testing - VP
		eventDO.setActivityName("H");
		eventDO.setActivityDate(new Date());
		//Testing - VP
		mainIndex = processEvent(mainIndex, eventDO);
		
		mainIndex = findMainOpCode();
		if(mainIndex==0)
			return false;

		eventDO.setActivityName("B");
		eventDO.setActivityDate(new Date());
		mainIndex = processEvent(mainIndex, eventDO);
		return true;
	}

	private int processEvent(int mainIndex, EventDO eventDO) {
		while(true) {
			if(mainIndex < instructionOpCodes.size()-1)
				mainIndex++;
			OpCodeBaseModel opCodeBaseModel = instructionOpCodes.get(mainIndex);
System.out.println(opCodeBaseModel);
			if(opCodeBaseModel instanceof ReturnModel)
				break;
			OperationResultDO operationResultDO = opCodeBaseModel.process(instructionOpCodes, myRewardDataSegment, eventDO);
			if(operationResultDO instanceof IfOperationResult) {
				int index = ((IfOperationResult)operationResultDO).getNextOperationNumber();
				if(index>0)
					mainIndex += (index-1);
				else {
					while(mainIndex<instructionOpCodes.size()) {
						mainIndex++;
						if(instructionOpCodes.get(mainIndex) instanceof ReturnModel)
							break;
					}
				}
			}
		}
		return mainIndex;
	}

	public List<OpCodeBaseModel> getInstructionOpCodes() {
		return instructionOpCodes;
	}

	public void setInstructionOpCodes(List<OpCodeBaseModel> instructionOpCodes) {
		this.instructionOpCodes = instructionOpCodes;
	}

	public MyRewardPCodeGenerator getMyRewardCodeGenerator() {
		return myRewardCodeGenerator;
	}

	public void setMyRewardCodeGenerator(MyRewardPCodeGenerator myRewardCodeGenerator) {
		this.myRewardCodeGenerator = myRewardCodeGenerator;
	}

	public MyRewardDataSegment getMyRewardDataSegment() {
		return myRewardDataSegment;
	}

	public void setMyRewardDataSegment(MyRewardDataSegment myRewardDataSegment) {
		this.myRewardDataSegment = myRewardDataSegment;
	}
}
