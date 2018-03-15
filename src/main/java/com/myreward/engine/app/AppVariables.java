package com.myreward.engine.app;

import java.util.List;
import java.util.Map;

import com.myreward.engine.event.opcode.OpCodeBaseModel;

public class AppVariables {
	@Deprecated
	public boolean isDebug = false;
	private static AppVariables instance;
	private static Map<String, List<OpCodeBaseModel>> instructionLibrary;
	protected AppVariables() {
		
	}
	public static AppVariables getInstance() {
		if(instance==null)
			instance = new AppVariables();
		return instance;
	}
	
}
