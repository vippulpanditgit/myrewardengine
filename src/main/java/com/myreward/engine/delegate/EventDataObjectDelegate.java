package com.myreward.engine.delegate;

public class EventDataObjectDelegate implements IRuntimeDelegate {
	public static String trace(StackTraceElement e[]) {
		boolean doNext = false;
		for (StackTraceElement s : e) {
			if (doNext) {
				return s.getMethodName();
			}
			doNext = s.getMethodName().equals("getStackTrace");
		}
		return null;
	}

	@Override
	public void creation(Object source) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void changed(Object source, String methodName) {
System.out.println("******** "+methodName);		
	}

	@Override
	public void termination(Object source) {
		// TODO Auto-generated method stub
		
	}
}
