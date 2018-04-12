package com.myreward.parser.model;

import java.util.ArrayList;
import java.util.List;

public class CallStackFunctionModel {
	public enum EventAttributeType {
		DURATION,
		REWARD,
		GATEKEEPER,
		REPEAT,
		SHOW,
		PRIORITY,
		GROUP,
		EVENT
	}

	public class _v_table_function {
		public String eventName;
		public EventAttributeType eventAttributeType;
		public String namespace;
		public String[] p_code_lst;
		public _v_table_function(String eventName, String namespace, String[] p_code_lst) {
			this.eventName = eventName;
			this.namespace = namespace;
			this.p_code_lst = p_code_lst;
			this.eventAttributeType = EventAttributeType.EVENT;
		}
		public _v_table_function(String eventName, String namespace, EventAttributeType eventAttributeType, String[] p_code_lst) {
			this.eventName = eventName;
			this.namespace = namespace;
			this.p_code_lst = p_code_lst;
			this.eventAttributeType = eventAttributeType;
		}
	};
	public List<_v_table_function> v_table_function_list = new ArrayList<_v_table_function>();
	public void add(String eventName, String namespace, String[] p_code_lst) {
		v_table_function_list.add(new _v_table_function(eventName, namespace, p_code_lst));
	}
	public void add(String eventName, String namespace, EventAttributeType eventAttributeType, String[] p_code_lst) {
		v_table_function_list.add(new _v_table_function(eventName, namespace, eventAttributeType, p_code_lst));
	}
	public void addAll(CallStackFunctionModel callStackFunctionModel) {
		v_table_function_list.addAll(callStackFunctionModel.v_table_function_list);
	}

}
