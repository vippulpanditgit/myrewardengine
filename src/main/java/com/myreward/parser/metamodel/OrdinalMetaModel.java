package com.myreward.parser.metamodel;

import java.util.ArrayList;
import java.util.List;

import com.myreward.engine.event.error.MetaModelException;
import com.myreward.engine.event.error.ReferencedModelException;
import com.myreward.parser.model.CallStackFunctionModel;
import com.myreward.parser.model.EventFunctionModel;
import com.myreward.parser.symbol.Symbol;

public class OrdinalMetaModel extends BaseMetaModel {
	public int ordinal;
	public List<GroupMetaModel> groupMetaModel = new ArrayList<GroupMetaModel>();
	@Override
	public String[] build() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public String[] model() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public void call_stack(CallStackFunctionModel callStackFunctionModel) {
	}
	@Override
	public void lib_lookup() throws ReferencedModelException {
		// TODO Auto-generated method stub
		
	}
	@Override
	public BaseMetaModel find(Symbol symbol) throws MetaModelException {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public void model(EventFunctionModel eventFunctionModel) {
		// TODO Auto-generated method stub
		
	}


}
