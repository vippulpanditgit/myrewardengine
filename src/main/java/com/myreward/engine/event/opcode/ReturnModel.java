package com.myreward.engine.event.opcode;

import org.apache.commons.lang3.StringUtils;

public class ReturnModel extends OpCodeBaseModel {
	private static String OPCODE_LABEL = "return";
	private static int OPCODE_MAX_LENGTH = 1;
	public static String[] OPCODE_HANDLER = {OPCODE_LABEL};

	public ReturnModel() {
	}

	public ReturnModel(String statement) {
		if(!StringUtils.equalsIgnoreCase(statement, OPCODE_LABEL))
				return;
	}

}
