package com.myreward.engine.event.processor;

import java.lang.reflect.Constructor;
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
	private List<OpCodeBaseModel> opCodeList = Arrays.asList(new CallFunctionModel(),
											new CallGatekeeperModel(),
											new CallPriorityModel(),
											new CallRepeatModel(),
											new CallRewardModel(),
											new CallShowModel(),
											new IfDurationModel(),
											new IfEventModel(),
											new IfGatekeeperModel(),
											new IfRewardModel(),
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
											new StoreShowModel());
	
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
				String opcode = codeSegmentIterator.next();
				Iterator<OpCodeBaseModel> opCodeBaseModelIterator = opCodeList.iterator();
				while(opCodeBaseModelIterator.hasNext()) {
					OpCodeBaseModel opCodeBaseModel = opCodeBaseModelIterator.next();
					String[] opCodeHandler = opCodeBaseModel.OPCODE_HANDLER;
					for(int opCodeIndex=0;opCodeIndex<opCodeHandler.length;opCodeIndex++) {
						if(opCodeHandler[opCodeIndex].equalsIgnoreCase(opcode.substring(0, opCodeHandler[opCodeIndex].length()))) {
							try {
								OpCodeBaseModel newFoo = opCodeBaseModel.getClass().newInstance();
								Constructor constructor = opCodeBaseModel.getClass().getConstructor(new Class[] { String.class});
								OpCodeBaseModel realInstance = (OpCodeBaseModel) constructor.newInstance(new Object[] { opcode });
							} catch(Exception exp){
								exp.printStackTrace();
							}
						}
					}
				}
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
}
