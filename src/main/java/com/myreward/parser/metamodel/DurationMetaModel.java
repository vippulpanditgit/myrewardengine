package com.myreward.parser.metamodel;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.myreward.engine.event.error.BuildException;
import com.myreward.engine.event.error.MetaModelException;
import com.myreward.engine.event.error.ReferencedModelException;
import com.myreward.parser.grammar.MyRewardParser;
import com.myreward.parser.model.CallStackFunctionModel;
import com.myreward.parser.model.CallStackFunctionModel.EventAttributeType;
import com.myreward.parser.model.EventFunctionModel;
import com.myreward.parser.model.EventInteractionFunctionModel;
import com.myreward.parser.symbol.Symbol;
import com.myreward.parser.symbol.SymbolTable;
import com.myreward.parser.util.DateTimeConvertorUtil;

public class DurationMetaModel extends BaseMetaModel {
	public Date effectiveDate; //yyyy-MM-dd1997-07-16T19:20:30.45+01:00
	public Date expirationDate;

	private String[] durationOpCodeListTemplate = {"lbl_dur:%s:%s", "desc(\".between(%s(%s),%s,%s)\")", "if_evt_dt_le(%d)", "return", "if_evt_dt_ge(%d)", "return", "set_dur_flg(%d)", "return"};

	public long getRelativeEffectiveDateInMilliSeconds() {
		if(effectiveDate!=null)
			return DateTimeConvertorUtil.toLong(effectiveDate);
		else return DateTimeConvertorUtil.toLong(new Date());
	}
	public long getRelativeExpirationDateInMilliSeconds() {
		if(expirationDate!=null)
			return DateTimeConvertorUtil.toLong(expirationDate);
		else return 0L;
	}
	@Override
	public String[] build() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public String[] model() {
		List<String> durationOpCodeList = new ArrayList<String>();
		if(this.parent instanceof EventMetaModel) {
			EventMetaModel parentEventMetaModel = (EventMetaModel)this.parent;
			Symbol eventSymbol = new Symbol(parentEventMetaModel.getEventName());
			eventSymbol.setNamespace(parentEventMetaModel.namespace);
			eventSymbol = symbolTable.lookup(eventSymbol);
			durationOpCodeList.add(String.format(durationOpCodeListTemplate[0], String.valueOf(eventSymbol.getFullyQualifiedId()),String.format(EventMetaModel.overrideTemplate, eventSymbol.version)));
			durationOpCodeList.add(String.format(durationOpCodeListTemplate[1], eventSymbol.getName(), eventSymbol.getFullyQualifiedId(), new Date(this.getRelativeEffectiveDateInMilliSeconds()), new Date(this.getRelativeExpirationDateInMilliSeconds())));
			durationOpCodeList.add(String.format(durationOpCodeListTemplate[2], this.getRelativeEffectiveDateInMilliSeconds()));
			durationOpCodeList.add(String.format(durationOpCodeListTemplate[3]));
			durationOpCodeList.add(String.format(durationOpCodeListTemplate[4], this.getRelativeExpirationDateInMilliSeconds()));
			durationOpCodeList.add(String.format(durationOpCodeListTemplate[5]));
			durationOpCodeList.add(String.format(durationOpCodeListTemplate[6], eventSymbol.getFullyQualifiedId()));
			durationOpCodeList.add(String.format(durationOpCodeListTemplate[7]));
		}
		return durationOpCodeList.toArray(new String[0]);
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
		List<String> durationOpCodeList = new ArrayList<String>();
		if(this.parent instanceof EventMetaModel) {
			EventMetaModel parentEventMetaModel = (EventMetaModel)this.parent;
			Symbol eventSymbol = new Symbol(parentEventMetaModel.getEventName());
			eventSymbol.setNamespace(parentEventMetaModel.namespace);
			eventSymbol = symbolTable.lookup(eventSymbol);
			if(eventFunctionModel.isExisting(String.format(durationOpCodeListTemplate[0], 
					String.valueOf(eventSymbol.getFullyQualifiedId()),
					String.format(EventMetaModel.overrideTemplate, eventSymbol.version)), 
				eventSymbol.getNamespace(), 
				EventAttributeType.DURATION)) {
					eventSymbol.version += 1;
				}
			durationOpCodeList.add(String.format(durationOpCodeListTemplate[0], String.valueOf(eventSymbol.getFullyQualifiedId()),String.format(EventMetaModel.overrideTemplate, eventSymbol.version)));
			durationOpCodeList.add(String.format(durationOpCodeListTemplate[1], eventSymbol.getName(), eventSymbol.getFullyQualifiedId(), new Date(this.getRelativeEffectiveDateInMilliSeconds()), new Date(this.getRelativeExpirationDateInMilliSeconds())));
			durationOpCodeList.add(String.format(durationOpCodeListTemplate[2], this.getRelativeEffectiveDateInMilliSeconds()));
			durationOpCodeList.add(String.format(durationOpCodeListTemplate[3]));
			durationOpCodeList.add(String.format(durationOpCodeListTemplate[4], this.getRelativeExpirationDateInMilliSeconds()));
			durationOpCodeList.add(String.format(durationOpCodeListTemplate[5]));
			durationOpCodeList.add(String.format(durationOpCodeListTemplate[6], eventSymbol.getFullyQualifiedId()));
			durationOpCodeList.add(String.format(durationOpCodeListTemplate[7]));

			eventFunctionModel.add(String.format(durationOpCodeListTemplate[0], 
						String.valueOf(eventSymbol.getFullyQualifiedId()),
						String.format(EventMetaModel.overrideTemplate, eventSymbol.version)), 
					eventSymbol.getNamespace(), 
					EventAttributeType.DURATION, 
					durationOpCodeList.toArray(new String[0]),
					"// "+eventSymbol.getFullyQualifiedName());

		}
	}
	@Override
	public void build(EventInteractionFunctionModel eventInteractionFunctionModel) throws BuildException {
		// TODO Auto-generated method stub
		
	}

}
