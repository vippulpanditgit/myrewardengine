package com.myreward.parser.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;

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
		public boolean equals(String eventName, String namespace, EventAttributeType eventAttributeType) {
			if(StringUtils.equalsAnyIgnoreCase(this.eventName, eventName)
					&& StringUtils.equalsAnyIgnoreCase(this.namespace, namespace)
					&& this.eventAttributeType == eventAttributeType)
				return true;
			return false;
		}
	};
	public List<_v_table_function> v_table_function_list = new ArrayList<_v_table_function>();
	public void add(String eventName, String namespace, String[] p_code_lst) {
		if(p_code_lst!=null && p_code_lst.length>0) {
			if(v_table_function_list
					.stream()
					.filter(function_def -> {
						if(StringUtils.equalsAnyIgnoreCase(function_def.eventName, eventName)
								&& StringUtils.equalsAnyIgnoreCase(function_def.namespace, namespace)
								&& function_def.eventAttributeType == EventAttributeType.EVENT)
								return true;
							return false;})
					.count()==0)
				v_table_function_list.add(new _v_table_function(eventName, namespace, EventAttributeType.EVENT, p_code_lst));
		}
	}
	public void add(String eventName, String namespace, EventAttributeType eventAttributeType, String[] p_code_lst) {
		if(p_code_lst!=null && p_code_lst.length>0) {
			if(v_table_function_list
					.stream()
					.filter(function_def -> {
						if(StringUtils.equalsAnyIgnoreCase(function_def.eventName, eventName)
							&& StringUtils.equalsAnyIgnoreCase(function_def.namespace, namespace)
							&& function_def.eventAttributeType == eventAttributeType)
							return true;
						return false;
					})
					.count()==0)
				v_table_function_list.add(new _v_table_function(eventName, namespace, eventAttributeType, p_code_lst));
		}
	}
	public void addAll(CallStackFunctionModel callStackFunctionModel) {
		v_table_function_list.addAll(callStackFunctionModel.v_table_function_list);
	}
	public List<String> merge_p_code() {
		List<String> opcodesList = new ArrayList<String>();
		for(int index=0;index<v_table_function_list.size();index++) {
			opcodesList.addAll(Arrays.asList(v_table_function_list.get(index).p_code_lst));
		}
		return opcodesList;
	}
}
