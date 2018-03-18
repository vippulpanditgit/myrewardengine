package com.myreward.engine.event.opcode.processing;

import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.myreward.engine.model.event.EventDO;
import com.myreward.engine.model.event.OperationResultDO;
import com.myreward.parser.generator.MyRewardDataSegment;

public class LabelDurationModel extends LabelBaseModel {
	private static String OPCODE_LABEL = "lbl_dur";
	private String name;
	private String version;
	public static String[] OPCODE_HANDLER = {OPCODE_LABEL};

	public LabelDurationModel() {
	}
	public LabelDurationModel(String name, String version) {
		this.name = name;
		this.version = version;
	}
	public LabelDurationModel(String statement) {
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
	public boolean equals(LabelDurationModel labelDurationModel) {
		return StringUtils.equalsIgnoreCase(name, labelDurationModel.name)
				&& StringUtils.equalsIgnoreCase(version, labelDurationModel.version);
	}
	@Override
	public OperationResultDO process(List<OpCodeBaseModel> instructionOpCodes, MyRewardDataSegment myRewardDataSegment,
			EventDO event) {
		// TODO Auto-generated method stub
		return null;
	}

}
