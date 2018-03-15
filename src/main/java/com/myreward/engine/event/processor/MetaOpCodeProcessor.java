package com.myreward.engine.event.processor;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.antlr.v4.runtime.RecognitionException;

import com.myreward.engine.event.error.ErrorCode;
import com.myreward.engine.event.error.MetaDataParsingException;
import com.myreward.parser.generator.MyRewardDataSegment;
import com.myreward.parser.generator.MyRewardPCodeGenerator;
import com.myreward.parser.grammar.MyRewardParser;
import com.myreward.parser.grammar.MyRewardParser.Myreward_defsContext;
import com.myreward.parser.util.MyRewardParserUtil;
import com.myreward.parser.util.RuntimeLib;

public class MetaOpCodeProcessor {
	private List<String> metaDataList;
	private MyRewardPCodeGenerator myRewardPCodeGenerator;
	
	public MetaOpCodeProcessor() {
		metaDataList = new ArrayList<String>();
		setMyRewardPCodeGenerator(null);
	}
	public MyRewardPCodeGenerator getMyRewardPCodeGenerator() {
		return myRewardPCodeGenerator;
	}
	public void setMyRewardPCodeGenerator(MyRewardPCodeGenerator myRewardPCodeGenerator) {
		this.myRewardPCodeGenerator = myRewardPCodeGenerator;
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
	public MyRewardParser setup(String rule) throws MetaDataParsingException {
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
			throw new MetaDataParsingException(ErrorCode.GENERAL_PARSING_EXCEPTION);
		}
	}
	public String[] parse(String rule, boolean isReturnGeneratedPCode) throws RecognitionException, MetaDataParsingException {
        Myreward_defsContext fileContext = setup(rule).myreward_defs(); 
        
        MyRewardPCodeGenerator myRewardCodeGenerator = new MyRewardPCodeGenerator();

        myRewardCodeGenerator.getCodeSegment().addAll(Arrays.asList(fileContext.myRewardDef.myRewardMetaModel.model())); // side effect of receiving an event
        myRewardCodeGenerator.getCodeSegment().addAll(Arrays.asList(fileContext.myRewardDef.myRewardMetaModel.build())); // default execution of receiving the event
        myRewardCodeGenerator.getCodeSegment().addAll(Arrays.asList(fileContext.myRewardDef.myRewardMetaModel.call_stack())); //mapping of event name to id
        
        this.setMyRewardPCodeGenerator(myRewardCodeGenerator);
        if(isReturnGeneratedPCode) {
        		return this.getPCode();
        }
        return null;
	}
	public String[] getPCode() {
		if(this.getMyRewardPCodeGenerator().getCodeSegment()!=null) {
			return this.getMyRewardPCodeGenerator().getCodeSegment().toArray(new String[0]);
		}
		return null;
	}
	public void print_code_segment() {
		System.out.println(this.getMyRewardPCodeGenerator().getCodeSegment());
	}
    public MyRewardDataSegment createDataSegment() {
        MyRewardDataSegment myRewardDataSegment = new MyRewardDataSegment();
        // Create the data segment
        myRewardDataSegment.processDataSegment(MyRewardParser.symbolTable);
        // Copy the data segment
        MyRewardDataSegment myRewardDataSegmentClone = (MyRewardDataSegment) RuntimeLib.deepClone(myRewardDataSegment);
        return myRewardDataSegmentClone;

    }

}
