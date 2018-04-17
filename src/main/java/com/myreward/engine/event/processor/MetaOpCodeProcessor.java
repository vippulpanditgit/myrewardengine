package com.myreward.engine.event.processor;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.antlr.v4.runtime.RecognitionException;
import org.apache.commons.lang3.StringUtils;

import com.myreward.engine.app.AppInstanceContext;
import com.myreward.engine.event.error.BuildException;
import com.myreward.engine.event.error.ErrorCode;
import com.myreward.engine.event.error.EventProcessingException;
import com.myreward.engine.event.error.MetaDataCreationException;
import com.myreward.engine.event.error.MetaDataParsingException;
import com.myreward.engine.event.error.ReferencedModelException;
import com.myreward.engine.event.opcode.processing.OpCodeBaseModel;
import com.myreward.parser.generator.MyRewardDataSegment;
import com.myreward.parser.generator.MyRewardPCodeGenerator;
import com.myreward.parser.grammar.MyRewardParser;
import com.myreward.parser.grammar.MyRewardParser.Myreward_defContext;
import com.myreward.parser.grammar.MyRewardParser.Myreward_defsContext;
import com.myreward.parser.metamodel.MyRewardMetaModel;
import com.myreward.parser.model.CallStackFunctionModel;
import com.myreward.parser.model.EventFunctionModel;
import com.myreward.parser.model.EventInteractionFunctionModel;
import com.myreward.parser.util.FileProcessingUtil;
import com.myreward.parser.util.MyRewardParserUtil;
import com.myreward.parser.util.RuntimeLib;

public class MetaOpCodeProcessor  implements java.io.Serializable  {
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
		if(parentContext.symbolTable!=null)
			parentContext.symbolTable.getAllSymbol().clear();
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
	public boolean isAlreadyAdded(String rule) {
		boolean isAlreadyAdded = false;
		for(int index=0;index<metaDataList.size();index++) {
			if(metaDataList.get(index).hashCode()==rule.hashCode()) {
				isAlreadyAdded = true;
				break;
			}
		}
		return isAlreadyAdded;
	}
	public MyRewardParser setup(String rule) throws MetaDataParsingException {
		try {
			MyRewardParser myRewardParser = MyRewardParserUtil.getParsed(this, rule.toString());
			return myRewardParser;
		} catch (IOException e) {
			throw new MetaDataParsingException(ErrorCode.GENERAL_PARSING_EXCEPTION);
		}
	}
	private void processRule(List<MyRewardMetaModel> myRewardMetaModelList, String rule) throws RecognitionException, MetaDataParsingException {
		Myreward_defsContext fileContext = setup(rule).myreward_defs();

		if(fileContext!=null && fileContext.children!=null && fileContext.children.size()>0) {
			for(int index=0; index < fileContext.children.size();index++) {
				myRewardMetaModelList.add(((Myreward_defContext)(fileContext.children.get(index))).myRewardMetaModel);
			}
		}
		
	}
	public String[] parse(String rule, boolean isReturnGeneratedPCode) throws RecognitionException, MetaDataParsingException, BuildException, ReferencedModelException {
		List<MyRewardMetaModel> myRewardMetaModelList = new ArrayList<>();
		this.setMyRewardPCodeGenerator(new MyRewardPCodeGenerator());
        CallStackFunctionModel callStackFunctionModel = new CallStackFunctionModel();
		callStackFunctionModel.add("lbl_main", null, new String[]{"lbl_main"});
		EventFunctionModel eventFunctionModel = new EventFunctionModel();
		EventInteractionFunctionModel eventInteractionFunctionModel = new EventInteractionFunctionModel();
		this.processRule(myRewardMetaModelList, rule);
		if(this.parentContext.nextMetaDataToProcess!=null 
				&& this.parentContext.nextMetaDataToProcess.size()>0) {
			this.parentContext.nextMetaDataToProcess.forEach((k, v) -> {
				try {
					this.processRule(myRewardMetaModelList, v);
				} catch (RecognitionException | MetaDataParsingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			});
		}
	        for(int index=0; index < myRewardMetaModelList.size();index++) {
				MyRewardMetaModel myRewardMetaModel = myRewardMetaModelList.get(index);
				myRewardMetaModel.lib_lookup();
				myRewardMetaModel.symbolTable.getAllSymbol().forEach(symbol-> {
					System.out.println(symbol);
					parentContext.symbolTable.getAllSymbol().addAll(myRewardMetaModel.symbolTable.getAllSymbol());
				});
			
				myRewardMetaModel.model(eventFunctionModel);
				myRewardMetaModel.build(eventInteractionFunctionModel);
		        myRewardMetaModel.call_stack(callStackFunctionModel);  
			}
	        callStackFunctionModel.add("return", null, new String[]{"return"});

	        this.getMyRewardPCodeGenerator().getCodeSegment().addAll(eventFunctionModel.merge_p_code());
	        this.getMyRewardPCodeGenerator().getCodeSegment().addAll(eventInteractionFunctionModel.merge_p_code());
	        this.getMyRewardPCodeGenerator().getCodeSegment().addAll(Arrays.asList(optimize_events(callStackFunctionModel)));
	        if(isReturnGeneratedPCode) {
	        		return this.getPCode();
	        }
        return null;
	}
	public String[] optimize_events(CallStackFunctionModel callStackFunctionModel) {
		Map<String, Integer> functionXRef = new LinkedHashMap<String, Integer>();
		List<String> code = new ArrayList<String>();
		int netCodeDisplacement = 0;
		for(int index=0;index< callStackFunctionModel.v_table_function_list.size();index++) {
			if(functionXRef.get(callStackFunctionModel.v_table_function_list.get(index).eventName)==null) {
				if(StringUtils.equalsIgnoreCase(callStackFunctionModel.v_table_function_list.get(index).eventName, "return")) {
					code.add("return");
					continue;
				}
				functionXRef.put(callStackFunctionModel.v_table_function_list.get(index).eventName, new Integer(code.size()));
				code.addAll(Arrays.asList(callStackFunctionModel.v_table_function_list.get(index).p_code_lst));
			} else {
				Integer functionIndex = functionXRef.get(callStackFunctionModel.v_table_function_list.get(index).eventName);
				String[] otherSameEventPCode = callStackFunctionModel.v_table_function_list.get(index).p_code_lst;
				int otherSameEventPCodeSize = callStackFunctionModel.v_table_function_list.get(index).p_code_lst.length;
//				if(callStackFunctionModel.v_table_function_list.size() >= (functionIndex.intValue())) {//Check if last
					//if not
					Integer nextCodeSegmentIndex = 0;//functionXRef.get(callStackFunctionModel.v_table_function_list.get(index+1).eventName);

//					if(!StringUtils.equalsIgnoreCase(callStackFunctionModel.v_table_function_list.get(index+1).eventName,"return"))
					if(functionIndex.intValue()<functionXRef.values().toArray(new Integer[0])[functionXRef.size()-1]) {
//						nextCodeSegmentIndex = functionXRef.get(callStackFunctionModel.v_table_function_list.get(index+1).eventName);
						Integer[] eventCodeIndexArray = functionXRef.values().toArray(new Integer[0]);
						for(int eventCodeIndex=0;eventCodeIndex<eventCodeIndexArray.length;eventCodeIndex++) {
							if(eventCodeIndexArray[eventCodeIndex]>functionIndex) {
								nextCodeSegmentIndex = eventCodeIndexArray[eventCodeIndex];
								break;
							}
						}
					} else
						nextCodeSegmentIndex = code.size();
					code.remove(nextCodeSegmentIndex-1);//remove "return"
//					if(callStackFunctionModel.v_table_function_list.size() > (functionIndex.intValue()+1))
					if(functionIndex.intValue()==functionXRef.get(functionXRef.keySet().toArray(new String[0])[functionXRef.size()-1])) //Check if last entry
//						code.addAll(netCodeDisplacement+nextCodeSegmentIndex-1, Arrays.asList(otherSameEventPCode));
						code.addAll(Arrays.asList(otherSameEventPCode));
					else 
						code.addAll(nextCodeSegmentIndex-1, Arrays.asList(otherSameEventPCode));
					code.remove(nextCodeSegmentIndex-1);// remove "if..."
					netCodeDisplacement = (otherSameEventPCodeSize - 2);
					final int codeDisplacement = netCodeDisplacement; 
					String ifStmt = code.get(functionIndex);
					code.remove(functionIndex.intValue());
					code.add(functionIndex.intValue(), StringUtils.substringBefore(ifStmt, ",")+","+(Integer.valueOf(StringUtils.substringBetween(ifStmt, ",", ")"))+(otherSameEventPCodeSize - 2))+")");
					for(int indexFunction=0;indexFunction<functionXRef.size();indexFunction++) {
						String keyValue = functionXRef.keySet().toArray(new String[0])[indexFunction];
						if(functionXRef.get(keyValue)> functionIndex.intValue()+1) {
							functionXRef.put(keyValue, functionXRef.get(keyValue)+netCodeDisplacement);
						}
					}
			}
		}
		return code.toArray(new String[0]);
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
        myRewardDataSegment.processDataSegment(parentContext.symbolTable);
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
						if(opcode.length()>=opCodeHandler[opCodeIndex].length() && 
								opCodeHandler[opCodeIndex].equalsIgnoreCase(opcode.substring(0, opCodeHandler[opCodeIndex].length()))) {
							try {
								Constructor constructor = opCodeBaseModel.getClass().getConstructor(new Class[] { String.class});
								OpCodeBaseModel realInstance = (OpCodeBaseModel) constructor.newInstance(new Object[] { opcode });
								runtimeOpCodes.add(realInstance);
								isOpcodeFound = true;
								break;
							} catch(Exception exp){
								throw new MetaDataCreationException("Exception creating Metadata tree "+opcode, exp);
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
	public boolean captureToProcessList(String importFile) {
		if(parentContext.nextMetaDataToProcess==null)
			parentContext.nextMetaDataToProcess = new HashMap<String, String>();
		if(importFile!=null && parentContext.nextMetaDataToProcess.get(importFile.hashCode())==null) {
			try {
				parentContext.nextMetaDataToProcess.put(String.valueOf(importFile.hashCode()), FileProcessingUtil.readFile(importFile));
				return true;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return false;
	}
}
