package com.myreward.engine.event.opcode.processing;

import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.myreward.engine.model.event.EventDO;
import com.myreward.engine.model.event.OperationResultDO;
import com.myreward.parser.generator.MyRewardDataSegment;

public class LabelRewardModel extends LabelBaseModel {
	private static String OPCODE_LABEL = "lbl_rwd";
	private String name;
	private String version;
	public static String[] OPCODE_HANDLER = {OPCODE_LABEL};

	public LabelRewardModel() {
	}
	public LabelRewardModel(String name, String version) {
		this.name = name;
		this.version = version;
	}

	public LabelRewardModel(String statement) {
		String[] fields = StringUtils.split(statement, ARGUMENT_SEPERATOR);
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
	public boolean equals(LabelRewardModel labelRewardModel) {
		return StringUtils.equalsIgnoreCase(name, labelRewardModel.name)
				&& StringUtils.equalsIgnoreCase(version, labelRewardModel.version);
	}
	@Override
	public OperationResultDO process(List<OpCodeBaseModel> instructionOpCodes, MyRewardDataSegment myRewardDataSegment,
			EventDO event) {
		// TODO Auto-generated method stub
		return null;
	}

}
