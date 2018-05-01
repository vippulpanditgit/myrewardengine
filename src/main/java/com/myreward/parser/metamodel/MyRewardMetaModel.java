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
import com.myreward.parser.model.EventInteractionFunctionModel;
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
	@Override
	public void build(EventInteractionFunctionModel eventInteractionFunctionModel) throws BuildException {
		List<String> myRewardOpcodeList = new ArrayList<String>();
		ListIterator<PackageMetaModel> packageMetaModelListIterator = myRewardMetaModelList.listIterator();
		while(packageMetaModelListIterator.hasNext()) {
			packageMetaModelListIterator.next().build(eventInteractionFunctionModel);
		}
	}
}
