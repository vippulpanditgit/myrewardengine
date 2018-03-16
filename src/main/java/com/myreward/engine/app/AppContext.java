package com.myreward.engine.app;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.myreward.engine.event.opcode.OpCodeBaseModel;

public class AppContext {
	@Deprecated
	public boolean isDebug = false;
	private static AppContext instance;
	private static Map<String, List<OpCodeBaseModel>> instructionLibrary;
	protected AppContext() {
		
	}
	public static AppContext getInstance() {
		if(instance==null)
			instance = new AppContext();
		return instance;
	}
	public boolean add(String name, List<OpCodeBaseModel> model) {
		if(instructionLibrary==null)
			instructionLibrary = new ConcurrentHashMap<String, List<OpCodeBaseModel>>();
		instructionLibrary.put(name, model);
		return true;
	}
	public List<OpCodeBaseModel> get(String name) {
		if(instructionLibrary!=null)
			instructionLibrary.get(name);
		return null;
	}
}
