package com.myreward.engine.event.processor;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.antlr.v4.runtime.RecognitionException;

import com.myreward.engine.app.AppInstanceContext;
import com.myreward.engine.event.error.ErrorCode;
import com.myreward.engine.event.error.EventProcessingException;
import com.myreward.engine.event.error.MetaDataCreationException;
import com.myreward.engine.event.error.MetaDataParsingException;
import com.myreward.engine.event.opcode.OpCodeBaseModel;
import com.myreward.parser.generator.MyRewardDataSegment;
import com.myreward.parser.generator.MyRewardPCodeGenerator;
import com.myreward.parser.grammar.MyRewardParser;
import com.myreward.parser.grammar.MyRewardParser.Myreward_defsContext;
import com.myreward.parser.util.MyRewardParserUtil;
import com.myreward.parser.util.RuntimeLib;

public class MetaOpCodeProcessor {
	private AppInstanceContext parentContext;
	private List<String> metaDataList;
	private MyRewardPCodeGenerator myRewardPCodeGenerator;
	private List<OpCodeBaseModel> runtimeOpCodes = new ArrayList<OpCodeBaseModel>();
	
	public MetaOpCodeProcessor(AppInstanceContext parentContext) {
		metaDataList = new ArrayList<String>();
		this.parentContext = parentContext;
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
	public void initialize(String rule) {
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
    public EventProcessor createEventProcessor(List<OpCodeBaseModel> instructionOpCodes, MyRewardDataSegment myRewardDataSegment) {
	    	EventProcessor eventProcessor = new EventProcessor(parentContext, this);
	    	eventProcessor.setInstructionOpCodes(instructionOpCodes);
	    	eventProcessor.setMyRewardDataSegment(myRewardDataSegment);
	    	return eventProcessor;
    }
	public List<OpCodeBaseModel> create_runtime_opcode_tree() throws EventProcessingException, MetaDataCreationException {
//		AuditManager.getInstance().audit(new AuditEvent(null, AuditEventType.AUDIT_EVENT_CREATE_META_DATA_TREE_START, null));
		runtimeOpCodes = new ArrayList<OpCodeBaseModel>();
		if(this.getMyRewardPCodeGenerator()==null)
			throw new EventProcessingException(ErrorCode.EVENT_METADATA_NOT_PRESET);
		if(this.getMyRewardPCodeGenerator().getCodeSegment()!=null
				&& this.getMyRewardPCodeGenerator().getCodeSegment().size()>0) {
			Iterator<String> codeSegmentIterator = this.getMyRewardPCodeGenerator().getCodeSegment().iterator();
			int index=0;
			while(codeSegmentIterator.hasNext()) {
				boolean isOpcodeFound = false;
				String opcode = codeSegmentIterator.next();
				Iterator<OpCodeBaseModel> opCodeBaseModelIterator = RuntimeSupportedOpCodeModel.getInstance().getSupportedOpCodeHandlers().iterator();
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
								runtimeOpCodes.add(realInstance);
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
//		AuditManager.getInstance().audit(new AuditEvent(null, AuditEventType.AUDIT_EVENT_CREATE_META_DATA_TREE_END, null));
		return runtimeOpCodes;
	}
}
