package com.myreward.engine.event.opcode;

import org.apache.commons.lang3.StringUtils;

public class LabelPriorityModel extends LabelBaseModel {
	private static String OPCODE_LABEL = "lbl_pri";
	private String name;
	private String version;
	public static String[] OPCODE_HANDLER = {OPCODE_LABEL};

	public LabelPriorityModel() {
	}

	public LabelPriorityModel(String statement) {
		String[] fields = StringUtils.split(statement, ARGUMENT_SEPERATOR);
		if(fields.length==3) {
			if(!StringUtils.equalsIgnoreCase(fields[0], OPCODE_LABEL))
				return;
			name = fields[1];
			version = fields[2];
		}
	}

}
