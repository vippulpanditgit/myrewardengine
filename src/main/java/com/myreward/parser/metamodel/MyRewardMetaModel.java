package com.myreward.parser.metamodel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.List;
import java.util.ListIterator;

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
		this.optimize_events(callStackFunctionModel);
	}
	private void optimize_events(CallStackFunctionModel callStackFunctionModel) {
		Hashtable<String, Integer> functionXRef = new Hashtable<String, Integer>();
		List<String> code = new ArrayList<String>();
		for(int index=0;index< callStackFunctionModel.v_table_function_list.size();index++) {
			if(functionXRef.get(callStackFunctionModel.v_table_function_list.get(index).eventName)==null) {
				functionXRef.put(callStackFunctionModel.v_table_function_list.get(index).eventName, new Integer(code.size()));
				code.addAll(Arrays.asList(callStackFunctionModel.v_table_function_list.get(index).p_code_lst));
			} else {
				Integer functionIndex = functionXRef.get(callStackFunctionModel.v_table_function_list.get(index).eventName);
				String[] otherSameEventPCode = callStackFunctionModel.v_table_function_list.get(index).p_code_lst;
				int otherSameEventPCodeSize = callStackFunctionModel.v_table_function_list.get(index).p_code_lst.length;
				if(callStackFunctionModel.v_table_function_list.size() < (index+1)) {//Check if last
					//if not
					Integer nextCodeSegmentIndex = functionXRef.get(callStackFunctionModel.v_table_function_list.get(index+1).eventName);
					code.remove(nextCodeSegmentIndex-1);
					code.addAll(nextCodeSegmentIndex-1, Arrays.asList(otherSameEventPCode));
				}
				
				
			}
		}
	}
}
