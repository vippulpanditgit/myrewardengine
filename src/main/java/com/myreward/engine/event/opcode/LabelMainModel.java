package com.myreward.engine.event.opcode;

import org.apache.commons.lang3.StringUtils;

public class LabelMainModel extends LabelBaseModel {
	private static String OPCODE_LABEL = "lbl_main";
	private static int OPCODE_MAX_LENGTH = 1;
	public LabelMainModel(String statement) {
		if(!StringUtils.equalsIgnoreCase(statement, OPCODE_LABEL))
				return;
	}

}
