package com.myreward.parser.metamodel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ListIterator;

import com.myreward.engine.event.error.BuildException;
import com.myreward.engine.event.error.MetaModelException;
import com.myreward.engine.event.error.ReferencedModelException;
import com.myreward.parser.grammar.MyRewardParser;
import com.myreward.parser.model.CallStackFunctionModel;
import com.myreward.parser.model.EventFunctionModel;
import com.myreward.parser.symbol.Symbol;
import com.myreward.parser.symbol.SymbolTable;

public class PackageMetaModel extends BaseMetaModel {
	public String packageName;
	public List<BaseMetaModel> packageMetaModelList = new ArrayList<BaseMetaModel>();
	@Override
	public String[] build() throws BuildException {
		metaSymbol = new Symbol(packageName);
		metaSymbol =  MyRewardParser.symbolTable.lookup(metaSymbol);

		List<String> packageOpcodeList = new ArrayList<String>();
		ListIterator<BaseMetaModel> packageMetaModelListIterator = packageMetaModelList.listIterator();
		while(packageMetaModelListIterator.hasNext()) {
			packageOpcodeList.addAll(Arrays.asList(packageMetaModelListIterator.next().build()));
		}
		return packageOpcodeList.toArray(new String[0]);
	}
	@Override
	public String[] model() {
		List<String> packageOpcodeList = new ArrayList<String>();
		ListIterator<BaseMetaModel> packageMetaModelListIterator = packageMetaModelList.listIterator();
		while(packageMetaModelListIterator.hasNext()) {
			packageOpcodeList.addAll(Arrays.asList(packageMetaModelListIterator.next().model()));
		}
		return packageOpcodeList.toArray(new String[0]);
	}
	@Override
	public void model(EventFunctionModel eventFunctionModel) {
		ListIterator<BaseMetaModel> packageMetaModelListIterator = packageMetaModelList.listIterator();
//		List<String> call_stack = new ArrayList<String>();
		while(packageMetaModelListIterator.hasNext()) {
			packageMetaModelListIterator.next().model(eventFunctionModel);
		}
	}

	@Override
	public void call_stack(CallStackFunctionModel callStackFunctionModel) {
		ListIterator<BaseMetaModel> packageMetaModelListIterator = packageMetaModelList.listIterator();
//		List<String> call_stack = new ArrayList<String>();
		while(packageMetaModelListIterator.hasNext()) {
			packageMetaModelListIterator.next().call_stack(callStackFunctionModel);
		}
	}
	@Override
	public void lib_lookup() throws ReferencedModelException {
		ListIterator<BaseMetaModel> packageMetaModelListIterator = packageMetaModelList.listIterator();
		while(packageMetaModelListIterator.hasNext()) {
			packageMetaModelListIterator.next().lib_lookup();
		}
	}
	@Override
	public BaseMetaModel find(Symbol symbol) throws MetaModelException {
		ListIterator<BaseMetaModel> packageMetaModelListIterator = packageMetaModelList.listIterator();
		while(packageMetaModelListIterator.hasNext()) {
			BaseMetaModel baseMetaModel = packageMetaModelListIterator.next().find(symbol);
			if(baseMetaModel!=null)
				return baseMetaModel;
		}
		return null;
	}

}
