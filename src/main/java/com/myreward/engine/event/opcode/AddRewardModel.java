package com.myreward.engine.event.opcode;

import org.apache.commons.lang3.StringUtils;

public class AddRewardModel extends OpCodeBaseModel {
	private static final String ARGUMENT_SEPERATOR = ",";
	private static String OPCODE_LABEL = "add_rwd_amt";
	private String name;
	private String amount;
	public static String[] OPCODE_HANDLER = {OPCODE_LABEL};

	public AddRewardModel() {
	}

	public AddRewardModel(String statement) {
		String[] fields = StringUtils.split(statement, ARGUMENT_SEPERATOR);
		if(fields.length==3) {
			if(!StringUtils.equalsIgnoreCase(fields[0], OPCODE_LABEL))
				return;
			name = fields[1];
			amount = fields[2];
		}
	}
	@Override
	public String[] getOpcodes() {
		return OPCODE_HANDLER;
	}

}
