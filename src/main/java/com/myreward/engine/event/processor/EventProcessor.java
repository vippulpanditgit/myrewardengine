package com.myreward.engine.event.processor;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.myreward.parser.generator.MyRewardPCodeGenerator;

public class EventProcessor {
	private MyRewardPCodeGenerator myRewardCodeGenerator;
	private Map<String, Integer> fnXref = new HashMap<String, Integer>();
	
	public void readPCode(MyRewardPCodeGenerator myRewardCodeGenerator) {
		myRewardCodeGenerator = myRewardCodeGenerator;
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
				if(opcode.startsWith("lbl_")) {
					String fnDecl = opcode.substring(beginIndex, endIndex)
					fnXref.put(key, value)
				}
				index++;
			}
		}
		
	}
	public void run() {
		if(this.myRewardCodeGenerator==null)
			return;
		
		while(true) {
			
		}
	}
}
