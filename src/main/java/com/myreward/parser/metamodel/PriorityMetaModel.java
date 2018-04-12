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

public class PriorityMetaModel extends BaseMetaModel {
	public int priority;
	private String[] priorityOpCodeListTemplate = {"lbl_pri:%s:%s", "desc(\".priority(%s(%s),%d)\")", "set_pri_flg(%d)", "set_pri_amt(%d,%d)", "return"};

	@Override
	public String[] build() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String[] model() {
		List<String> priorityOpCodeList = new ArrayList<String>();
		if(this.parent instanceof EventMetaModel) {
			EventMetaModel parentEventMetaModel = (EventMetaModel)this.parent;
			Symbol eventSymbol = new Symbol(parentEventMetaModel.getEventName());
			eventSymbol.setNamespace(parentEventMetaModel.namespace);
			SymbolTable symbolTable = MyRewardParser.symbolTable;
			eventSymbol = symbolTable.lookup(eventSymbol);
			priorityOpCodeList.add(String.format(priorityOpCodeListTemplate[0], String.valueOf(eventSymbol.getFullyQualifiedId()),String.format(EventMetaModel.overrideTemplate, eventSymbol.version)));
			priorityOpCodeList.add(String.format(priorityOpCodeListTemplate[1], eventSymbol.getName(), eventSymbol.getFullyQualifiedId(), priority));
			priorityOpCodeList.add(String.format(priorityOpCodeListTemplate[2], eventSymbol.getFullyQualifiedId()));
			priorityOpCodeList.add(String.format(priorityOpCodeListTemplate[3], eventSymbol.getFullyQualifiedId(), priority));
			priorityOpCodeList.add(String.format(priorityOpCodeListTemplate[4], eventSymbol.getFullyQualifiedId()));
		}
		return priorityOpCodeList.toArray(new String[0]);
	}

	@Override
	public void call_stack(CallStackFunctionModel callStackFunctionModel) {
		// TODO Auto-generated method stub
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
		List<String> priorityOpCodeList = new ArrayList<String>();
		if(this.parent instanceof EventMetaModel) {
			EventMetaModel parentEventMetaModel = (EventMetaModel)this.parent;
			Symbol eventSymbol = new Symbol(parentEventMetaModel.getEventName());
			eventSymbol.setNamespace(parentEventMetaModel.namespace);
			SymbolTable symbolTable = MyRewardParser.symbolTable;
			eventSymbol = symbolTable.lookup(eventSymbol);
			priorityOpCodeList.add(String.format(priorityOpCodeListTemplate[0], String.valueOf(eventSymbol.getFullyQualifiedId()),String.format(EventMetaModel.overrideTemplate, eventSymbol.version)));
			priorityOpCodeList.add(String.format(priorityOpCodeListTemplate[1], eventSymbol.getName(), eventSymbol.getFullyQualifiedId(), priority));
			priorityOpCodeList.add(String.format(priorityOpCodeListTemplate[2], eventSymbol.getFullyQualifiedId()));
			priorityOpCodeList.add(String.format(priorityOpCodeListTemplate[3], eventSymbol.getFullyQualifiedId(), priority));
			priorityOpCodeList.add(String.format(priorityOpCodeListTemplate[4], eventSymbol.getFullyQualifiedId()));
			eventFunctionModel.add(String.format(String.format(priorityOpCodeListTemplate[0], String.valueOf(eventSymbol.getFullyQualifiedId()),String.format(EventMetaModel.overrideTemplate, eventSymbol.version))),
					this.namespace, 
					EventAttributeType.PRIORITY, 
					priorityOpCodeList.toArray(new String[0]));
		}
	}

	@Override
	public void build(EventInteractionFunctionModel eventInteractionFunctionModel) throws BuildException {
		// TODO Auto-generated method stub
		
	}

}
