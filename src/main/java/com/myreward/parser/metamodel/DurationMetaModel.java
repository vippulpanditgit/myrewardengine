package com.myreward.parser.metamodel;

import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.myreward.parser.grammar.MyRewardParser;
import com.myreward.parser.symbol.Symbol;
import com.myreward.parser.symbol.SymbolTable;
import com.myreward.parser.util.DateTimeConvertorUtil;

public class DurationMetaModel extends BaseMetaModel {
	public Date effectiveDate; //yyyy-MM-dd1997-07-16T19:20:30.45+01:00
	public Date expirationDate;

	private String[] durationOpCodeListTemplate = {"lbl_dur:%d", "if_evt_dt_le(%d)", "return", "if_evt_dt_ge(%d)", "return", "set_dur_flg(%d)", "return"};

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
			SymbolTable symbolTable = MyRewardParser.symbolTable;
			eventSymbol = symbolTable.lookup(eventSymbol);
			durationOpCodeList.add(String.format(durationOpCodeListTemplate[0], eventSymbol.getFullyQualifiedId()));
			durationOpCodeList.add(String.format(durationOpCodeListTemplate[1], this.getRelativeEffectiveDateInMilliSeconds()));
			durationOpCodeList.add(String.format(durationOpCodeListTemplate[2]));
			durationOpCodeList.add(String.format(durationOpCodeListTemplate[3], this.getRelativeExpirationDateInMilliSeconds()));
			durationOpCodeList.add(String.format(durationOpCodeListTemplate[4]));
			durationOpCodeList.add(String.format(durationOpCodeListTemplate[5], eventSymbol.getFullyQualifiedId()));
			durationOpCodeList.add(String.format(durationOpCodeListTemplate[6]));
		}
		return durationOpCodeList.toArray(new String[0]);
	}
	@Override
	public String[] call_stack() {
		// TODO Auto-generated method stub
		return null;
	}

}
