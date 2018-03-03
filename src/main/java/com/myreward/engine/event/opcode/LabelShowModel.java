package com.myreward.engine.event.opcode;

import org.apache.commons.lang3.StringUtils;

public class LabelShowModel extends LabelBaseModel {
	private static String OPCODE_LABEL = "lbl_shw";
	private String name;
	private String version;
	public static String[] OPCODE_HANDLER = {OPCODE_LABEL};
	
	public LabelShowModel() {
	}
	public LabelShowModel(String name, String version) {
		this.name = name;
		this.version = version;
	}
	public LabelShowModel(String showStatement) {
		String[] fields = StringUtils.split(showStatement, ARGUMENT_SEPERATOR);
		if(fields.length==3) {
			if(!StringUtils.equalsIgnoreCase(fields[0], OPCODE_LABEL))
				return;
			name = fields[1];
			version = fields[2];
		}
	}
	@Override
	public String[] getOpcodes() {
		return OPCODE_HANDLER;
	}
	public String toString() {
		return OPCODE_LABEL+ARGUMENT_SEPERATOR+name+ARGUMENT_SEPERATOR+version;
	}
	public boolean equals(LabelShowModel labelShowModel) {
		return StringUtils.equalsIgnoreCase(name, labelShowModel.name)
				&& StringUtils.equalsIgnoreCase(version, labelShowModel.version);
	}

}
