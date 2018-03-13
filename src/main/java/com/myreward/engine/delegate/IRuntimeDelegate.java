package com.myreward.engine.delegate;

public interface IRuntimeDelegate {
	public void creation(Object source);
	public void changed(Object source, String methodName);
	public void termination(Object source);
}
