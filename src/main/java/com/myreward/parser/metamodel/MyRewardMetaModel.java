package com.myreward.parser.metamodel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ListIterator;

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
	public CallStackFunctionModel call_stack(CallStackFunctionModel callStackFunctionModel) {
		List<String> myRewardOpcodeList = new ArrayList<String>();
		if(callStackFunctionModel==null)
			callStackFunctionModel = new CallStackFunctionModel();
		myRewardOpcodeList.add("lbl_main");
		ListIterator<PackageMetaModel> packageMetaModelListIterator = myRewardMetaModelList.listIterator();
		while(packageMetaModelListIterator.hasNext()) {
			callStackFunctionModel.addAll(packageMetaModelListIterator.next().call_stack(callStackFunctionModel));
		}
		return callStackFunctionModel;
	}

}
