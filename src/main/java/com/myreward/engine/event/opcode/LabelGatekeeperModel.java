package com.myreward.engine.event.opcode;

import org.apache.commons.lang3.StringUtils;

public class LabelGatekeeperModel extends LabelBaseModel {
	private static String OPCODE_LABEL = "lbl_gtk";
	private String name;
	private String version;
	
	public LabelGatekeeperModel(String statement) {
		String[] fields = StringUtils.split(statement, ARGUMENT_SEPERATOR);
		if(fields.length==3) {
			if(!StringUtils.equalsIgnoreCase(fields[0], OPCODE_LABEL))
				return;
			name = fields[1];
			version = fields[2];
		}
	}
}