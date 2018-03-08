package com.myreward.engine.event.processor;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.antlr.v4.runtime.RecognitionException;

import com.myreward.engine.event.error.ErrorCode;
import com.myreward.engine.event.error.MetadataParsingException;
import com.myreward.parser.generator.MyRewardPCodeGenerator;
import com.myreward.parser.grammar.MyRewardParser;
import com.myreward.parser.grammar.MyRewardParser.Myreward_defsContext;
import com.myreward.parser.util.MyRewardParserUtil;

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
	public MyRewardParser setup(String rule) throws MetadataParsingException {
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
			throw new MetadataParsingException(ErrorCode.GENERAL_PARSING_EXCEPTION);
		}
	}
	public void parse(String rule) throws RecognitionException, MetadataParsingException {
        Myreward_defsContext fileContext = setup(rule).myreward_defs(); 
        
        MyRewardPCodeGenerator myRewardCodeGenerator = new MyRewardPCodeGenerator();

        myRewardCodeGenerator.getCodeSegment().addAll(Arrays.asList(fileContext.myRewardDef.myRewardMetaModel.model())); // side effect of receiving an event
        myRewardCodeGenerator.getCodeSegment().addAll(Arrays.asList(fileContext.myRewardDef.myRewardMetaModel.build())); // default execution of receiving the event
        myRewardCodeGenerator.getCodeSegment().addAll(Arrays.asList(fileContext.myRewardDef.myRewardMetaModel.call_stack())); //mapping of event name to id
        
        this.setMyRewardPCodeGenerator(myRewardCodeGenerator);
	}
	public void print_code_segment() {
		System.out.println(this.getMyRewardPCodeGenerator().getCodeSegment());
	}
	
}
