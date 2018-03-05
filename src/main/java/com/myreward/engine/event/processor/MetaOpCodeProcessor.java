package com.myreward.engine.event.processor;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import com.myreward.parser.generator.MyRewardPCodeGenerator;
import com.myreward.parser.grammar.MyRewardParser;
import com.myreward.parser.grammar.MyRewardParser.Myreward_defsContext;
import com.myreward.parser.util.MyRewardParserUtil;

public class MetaOpCodeProcessor {
	private List<String> metaDataList;
	
	public MetaOpCodeProcessor() {
		metaDataList = new ArrayList<String>();
	}
	public void initialize() {
		if(metaDataList!=null) {
			metaDataList = null;
			metaDataList = new ArrayList<String>();
		}
	}
	public void addRule(String rule) {
		metaDataList.add(rule);
	}
	public void cleanUp() {
		metaDataList = null;
		
	}
	public MyRewardParser setup(String rule) {
		if(rule!=null) {
			metaDataList.add(rule);
		}
		StringBuffer ruleList = new StringBuffer();
		Iterator<String> ruleIterator = metaDataList.iterator();
		while(ruleIterator.hasNext()) {
			ruleList.append(ruleIterator.next());
		}
		try {
			MyRewardParser myRewardParser = MyRewardParserUtil.getParsed(ruleList.toString());
			return myRewardParser;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	public MyRewardPCodeGenerator parse(String rule) {
        Myreward_defsContext fileContext = setup(rule).myreward_defs(); 
        
        MyRewardPCodeGenerator myRewardCodeGenerator = new MyRewardPCodeGenerator();

        myRewardCodeGenerator.getCodeSegment().addAll(Arrays.asList(fileContext.myRewardDef.myRewardMetaModel.model())); // side effect of receiving an event
        myRewardCodeGenerator.getCodeSegment().addAll(Arrays.asList(fileContext.myRewardDef.myRewardMetaModel.build())); // default execution of receiving the event
        myRewardCodeGenerator.getCodeSegment().addAll(Arrays.asList(fileContext.myRewardDef.myRewardMetaModel.call_stack())); //mapping of event name to id
        
        return myRewardCodeGenerator;
	}
	
}
