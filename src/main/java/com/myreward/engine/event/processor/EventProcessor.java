package com.myreward.engine.event.processor;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.myreward.parser.generator.MyRewardPCodeGenerator;
import com.myreward.engine.event.opcode.*;

public class EventProcessor {
	private MyRewardPCodeGenerator myRewardCodeGenerator;
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
											new LabelDurationModel(),
											new LabelFunctionModel(),
											new LabelGatekeeperModel(),
											new LabelMainModel(),
											new LabelPriorityModel(),
											new LabelRepeatModel(),
											new LabelRewardModel(),
											new LabelShowModel(),
											new StoreGatekeeperModel(),
											new StorePriorityModel(),
											new StoreRewardModel(),
											new StoreShowModel(),
											new StoreDurationModel(),
											new ReturnModel(),
											new ResetDurationModel(),
											new ResetGatekeeperModel(),
											new ResetPriorityModel(),
											new ResetRewardModel(),
											new ResetShowModel(),
											new StoreRepeatModel(),
											new AddRewardModel());
	
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
	
	public void run() {
		if(this.myRewardCodeGenerator==null)
			return;
		
		while(true) {
			
		}
	}

	public List<OpCodeBaseModel> getInstructionOpCodes() {
		return instructionOpCodes;
	}

	public void setInstructionOpCodes(List<OpCodeBaseModel> instructionOpCodes) {
		this.instructionOpCodes = instructionOpCodes;
	}
}
