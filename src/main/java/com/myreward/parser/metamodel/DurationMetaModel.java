package com.myreward.parser.metamodel;

import java.time.ZoneId;
import java.util.Date;

import com.myreward.parser.util.DateTimeConvertorUtil;

public class DurationMetaModel extends BaseMetaModel {
	public Date effectiveDate; //yyyy-MM-dd1997-07-16T19:20:30.45+01:00
	public Date expirationDate;
	
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
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public String[] call_stack() {
		// TODO Auto-generated method stub
		return null;
	}

}
