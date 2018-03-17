package com.myreward.engine.app;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.myreward.engine.event.opcode.OpCodeBaseModel;
import com.myreward.engine.event.processor.MetaOpCodeProcessor;

public class AppContext {
	private static AppContext instance;
	private static Map<String, MetaOpCodeProcessor> instructionLibrary;
	protected AppContext() {
		
	}
	public static AppContext getInstance() {
		if(instance==null)
			instance = new AppContext();
		return instance;
	}
	public boolean add(String name, MetaOpCodeProcessor model) {
		if(instructionLibrary==null)
			instructionLibrary = new ConcurrentHashMap<String, MetaOpCodeProcessor>();
		instructionLibrary.put(name, model);
		return true;
	}
	public MetaOpCodeProcessor get(String name) {
		if(instructionLibrary!=null)
			return instructionLibrary.get(name);
		return null;
	}
}
