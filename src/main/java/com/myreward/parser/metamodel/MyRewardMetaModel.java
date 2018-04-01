package com.myreward.parser.metamodel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.ibm.icu.impl.UResource.Array;
import com.myreward.parser.model.CallStackFunctionModel;

public class MyRewardMetaModel extends BaseMetaModel {
	public List<PackageMetaModel> myRewardMetaModelList = new ArrayList<PackageMetaModel>();

	@Override
	public String[] build() {
		List<String> myRewardOpcodeList = new ArrayList<String>();
		ListIterator<PackageMetaModel> packageMetaModelListIterator = myRewardMetaModelList.listIterator();
		while(packageMetaModelListIterator.hasNext()) {
			myRewardOpcodeList.addAll(Arrays.asList(packageMetaModelListIterator.next().build()));
		}
		return myRewardOpcodeList.toArray(new String[0]);
	}

	@Override
	public String[] model() {
		List<String> myRewardOpcodeList = new ArrayList<String>();
		ListIterator<PackageMetaModel> packageMetaModelListIterator = myRewardMetaModelList.listIterator();
		while(packageMetaModelListIterator.hasNext()) {
			myRewardOpcodeList.addAll(Arrays.asList(packageMetaModelListIterator.next().model()));
		}
		return myRewardOpcodeList.toArray(new String[0]);
	}

	@Override
	public void call_stack(CallStackFunctionModel callStackFunctionModel) {
		if(callStackFunctionModel==null)
			callStackFunctionModel = new CallStackFunctionModel();
		callStackFunctionModel.add("lbl_main", null, new String[]{"lbl_main"});
		ListIterator<PackageMetaModel> packageMetaModelListIterator = myRewardMetaModelList.listIterator();
		while(packageMetaModelListIterator.hasNext()) {
			packageMetaModelListIterator.next().call_stack(callStackFunctionModel);
		}
		callStackFunctionModel.add("return", null, new String[]{"return"});
	}
	public String[] optimize_events(CallStackFunctionModel callStackFunctionModel) {
		Map<String, Integer> functionXRef = new LinkedHashMap<String, Integer>();
		List<String> code = new ArrayList<String>();
		int netCodeDisplacement = 0;
		for(int index=0;index< callStackFunctionModel.v_table_function_list.size();index++) {
			if(functionXRef.get(callStackFunctionModel.v_table_function_list.get(index).eventName)==null) {
				functionXRef.put(callStackFunctionModel.v_table_function_list.get(index).eventName, new Integer(code.size()));
				code.addAll(Arrays.asList(callStackFunctionModel.v_table_function_list.get(index).p_code_lst));
			} else {
				Integer functionIndex = functionXRef.get(callStackFunctionModel.v_table_function_list.get(index).eventName);
				String[] otherSameEventPCode = callStackFunctionModel.v_table_function_list.get(index).p_code_lst;
				int otherSameEventPCodeSize = callStackFunctionModel.v_table_function_list.get(index).p_code_lst.length;
//				if(callStackFunctionModel.v_table_function_list.size() >= (functionIndex.intValue())) {//Check if last
					//if not
					Integer nextCodeSegmentIndex = 0;
					if(callStackFunctionModel.v_table_function_list.size() > (functionIndex.intValue()+1))
						nextCodeSegmentIndex = functionXRef.get(callStackFunctionModel.v_table_function_list.get(functionIndex.intValue()+1).eventName);
					else
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
					String ifStmt = code.get(functionIndex);
					code.remove(functionIndex.intValue());
					code.add(functionIndex.intValue(), StringUtils.substringBefore(ifStmt, ",")+","+(Integer.valueOf(StringUtils.substringBetween(ifStmt, ",", ")"))+(otherSameEventPCodeSize - 2))+")");
					for(int lowerIndex=functionIndex.intValue()+1; lowerIndex < functionXRef.size();lowerIndex++) {
						String eventName = functionXRef.keySet().toArray(new String[0])[lowerIndex];
						Integer sizeValue = functionXRef.get(eventName);
						sizeValue += netCodeDisplacement;
						functionXRef.put(eventName, sizeValue);
					}
/*					for(int lowerIndex=index+1;lowerIndex<callStackFunctionModel.v_table_function_list.size();lowerIndex++) {
						Integer sizeValue = 0;
						try {
							sizeValue = functionXRef.get(callStackFunctionModel.v_table_function_list.get(lowerIndex).eventName);
							if(sizeValue!=null) {
								sizeValue += netCodeDisplacement;
								functionXRef.put(callStackFunctionModel.v_table_function_list.get(lowerIndex).eventName, sizeValue);
							}
						} catch(Exception exp) {
							
						}
					}
*///				}
			}
		}
		return code.toArray(new String[0]);
	}
}
