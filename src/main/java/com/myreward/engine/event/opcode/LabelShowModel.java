package com.myreward.engine.event.opcode;

import org.apache.commons.lang3.StringUtils;

public class LabelShowModel extends LabelBaseModel {
	private static String OPCODE_LABEL = "lbl_shw";
	private String name;
	private String version;
	public static String[] OPCODE_HANDLER = {OPCODE_LABEL};
	
	public LabelShowModel(String showStatement) {
		String[] fields = StringUtils.split(showStatement, ARGUMENT_SEPERATOR);
		if(fields.length==3) {
			if(!StringUtils.equalsIgnoreCase(fields[0], OPCODE_LABEL))
				return;
			name = fields[1];
			version = fields[2];
		}
	}

}
