package com.myreward.engine.app;

public class AppVariables {
	public boolean isDebug = false;
	private static AppVariables instance;
	protected AppVariables() {
		
	}
	public static AppVariables getInstance() {
		if(instance==null)
			instance = new AppVariables();
		return instance;
	}
	
}
