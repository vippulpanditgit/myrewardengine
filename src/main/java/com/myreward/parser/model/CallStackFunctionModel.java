package com.myreward.parser.model;

import java.util.ArrayList;
import java.util.List;

public class CallStackFunctionModel {
	public class _v_table_function {
		public String eventName;
		public String namespace;
		public String[] p_code_lst;
		public _v_table_function(String eventName, String namespace, String[] p_code_lst) {
			this.eventName = eventName;
			this.namespace = namespace;
			this.p_code_lst = p_code_lst;
		}
	};
	public List<_v_table_function> v_table_function_list = new ArrayList<_v_table_function>();
	public void add(String eventName, String namespace, String[] p_code_lst) {
		v_table_function_list.add(new _v_table_function(eventName, namespace, p_code_lst));
	}

}
