package com.myreward.parser.metamodel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.myreward.engine.event.error.BuildException;
import com.myreward.engine.event.error.MetaModelException;
import com.myreward.engine.event.error.ReferencedModelException;
import com.myreward.parser.model.CallStackFunctionModel;
import com.myreward.parser.model.EventFunctionModel;
import com.myreward.parser.symbol.Symbol;

public class MyRewardMetaModel extends BaseMetaModel {
	public List<PackageMetaModel> myRewardMetaModelList = new ArrayList<PackageMetaModel>();

	public void lib_lookup() throws ReferencedModelException {
		ListIterator<PackageMetaModel> packagesMetaModelListIterator = myRewardMetaModelList.listIterator();
		while(packagesMetaModelListIterator.hasNext()) {
			packagesMetaModelListIterator.next().lib_lookup();
		}
	}
	@Override
	public String[] build() throws BuildException {
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
	public void model(EventFunctionModel eventFunctionModel) {
		List<String> myRewardOpcodeList = new ArrayList<String>();
		ListIterator<PackageMetaModel> packageMetaModelListIterator = myRewardMetaModelList.listIterator();
		while(packageMetaModelListIterator.hasNext()) {
			packageMetaModelListIterator.next().model(eventFunctionModel);
		}
	}

	@Override
	public void call_stack(CallStackFunctionModel callStackFunctionModel) {
		ListIterator<PackageMetaModel> packageMetaModelListIterator = myRewardMetaModelList.listIterator();
		while(packageMetaModelListIterator.hasNext()) {
			packageMetaModelListIterator.next().call_stack(callStackFunctionModel);
		}
	}
	public String[] optimize_events(CallStackFunctionModel callStackFunctionModel) {
		Map<String, Integer> functionXRef = new LinkedHashMap<String, Integer>();
		List<String> code = new ArrayList<String>();
		int netCodeDisplacement = 0;
		for(int index=0;index< callStackFunctionModel.v_table_function_list.size();index++) {
			if(functionXRef.get(callStackFunctionModel.v_table_function_list.get(index).eventName)==null) {
				if(StringUtils.equalsIgnoreCase(callStackFunctionModel.v_table_function_list.get(index).eventName, "return"))
					continue;
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
			}
		}
		return code.toArray(new String[0]);
	}
	@Override
	public BaseMetaModel find(Symbol symbol) throws MetaModelException {
		ListIterator<PackageMetaModel> packageMetaModelListIterator = myRewardMetaModelList.listIterator();
		while(packageMetaModelListIterator.hasNext()) {
			BaseMetaModel baseMetaModel = packageMetaModelListIterator.next().find(symbol);
			if(baseMetaModel!=null)
				return baseMetaModel;
		}
		return null;
	}
}
