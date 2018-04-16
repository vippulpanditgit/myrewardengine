package com.myreward.parser.metamodel;

import java.util.ArrayList;
import java.util.List;

import com.myreward.engine.event.error.BuildException;
import com.myreward.engine.event.error.MetaModelException;
import com.myreward.engine.event.error.ReferencedModelException;
import com.myreward.parser.grammar.MyRewardParser;
import com.myreward.parser.model.CallStackFunctionModel;
import com.myreward.parser.model.EventFunctionModel;
import com.myreward.parser.model.EventInteractionFunctionModel;
import com.myreward.parser.model.CallStackFunctionModel.EventAttributeType;
import com.myreward.parser.symbol.Symbol;
import com.myreward.parser.symbol.SymbolTable;

public class ShowMetaModel extends BaseMetaModel {
	public boolean isShow;

	private String[] showOpCodeListTemplate = {"lbl_shw:%s:%s", "desc(\".show(%s(%s),%s)\")", "set_shw_flg(%d)", "return"};

	@Override
	public String[] build() {
		return null;
	}

	@Override
	public String[] model() {
		List<String> showOpCodeList = new ArrayList<String>();
		if(this.parent instanceof EventMetaModel) {
			EventMetaModel parentEventMetaModel = (EventMetaModel)this.parent;
			Symbol eventSymbol = new Symbol(parentEventMetaModel.getEventName());
			eventSymbol.setNamespace(parentEventMetaModel.namespace);
			eventSymbol = symbolTable.lookup(eventSymbol);
			showOpCodeList.add(String.format(showOpCodeListTemplate[0], String.valueOf(eventSymbol.getFullyQualifiedId()),String.format(EventMetaModel.overrideTemplate, eventSymbol.version)));
			showOpCodeList.add(String.format(showOpCodeListTemplate[1], eventSymbol.getName(), eventSymbol.getFullyQualifiedId(), "true"));
			showOpCodeList.add(String.format(showOpCodeListTemplate[2], eventSymbol.getFullyQualifiedId()));
			showOpCodeList.add(String.format(showOpCodeListTemplate[3], eventSymbol.getFullyQualifiedId()));
		}
		return showOpCodeList.toArray(new String[0]);
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
		List<String> showOpCodeList = new ArrayList<String>();
		if(this.parent instanceof EventMetaModel) {
			EventMetaModel parentEventMetaModel = (EventMetaModel)this.parent;
			Symbol eventSymbol = new Symbol(parentEventMetaModel.getEventName());
			eventSymbol.setNamespace(parentEventMetaModel.namespace);
			eventSymbol = symbolTable.lookup(eventSymbol);
			showOpCodeList.add(String.format(showOpCodeListTemplate[0], String.valueOf(eventSymbol.getFullyQualifiedId()),String.format(EventMetaModel.overrideTemplate, eventSymbol.version)));
			showOpCodeList.add(String.format(showOpCodeListTemplate[1], eventSymbol.getName(), eventSymbol.getFullyQualifiedId(), "true"));
			showOpCodeList.add(String.format(showOpCodeListTemplate[2], eventSymbol.getFullyQualifiedId()));
			showOpCodeList.add(String.format(showOpCodeListTemplate[3], eventSymbol.getFullyQualifiedId()));
			eventFunctionModel.add(String.format(showOpCodeListTemplate[0], String.valueOf(eventSymbol.getFullyQualifiedId()),String.format(EventMetaModel.overrideTemplate, eventSymbol.version)),
					this.namespace, 
					EventAttributeType.SHOW, 
					showOpCodeList.toArray(new String[0]),
					"// "+eventSymbol.getFullyQualifiedName());
		}
	}

	@Override
	public void build(EventInteractionFunctionModel eventInteractionFunctionModel) throws BuildException {
		// TODO Auto-generated method stub
		
	}

}
