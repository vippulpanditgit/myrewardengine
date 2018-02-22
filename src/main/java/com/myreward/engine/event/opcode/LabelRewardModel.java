package com.myreward.engine.event.opcode;

import org.apache.commons.lang3.StringUtils;

public class LabelRewardModel extends LabelBaseModel {
	private static String OPCODE_LABEL = "lbl_rwd";
	private String name;
	private String version;
	
	public LabelRewardModel(String statement) {
		String[] fields = StringUtils.split(statement, ARGUMENT_SEPERATOR);
		if(fields.length==3) {
			if(!StringUtils.equalsIgnoreCase(fields[0], OPCODE_LABEL))
				return;
			name = fields[1];
			version = fields[2];
		}
	}

}
