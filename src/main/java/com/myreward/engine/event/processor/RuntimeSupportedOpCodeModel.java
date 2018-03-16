package com.myreward.engine.event.processor;

import java.util.Arrays;
import java.util.List;

import com.myreward.engine.event.opcode.AddRewardModel;
import com.myreward.engine.event.opcode.CallDurationModel;
import com.myreward.engine.event.opcode.CallFunctionModel;
import com.myreward.engine.event.opcode.CallGatekeeperModel;
import com.myreward.engine.event.opcode.CallPriorityModel;
import com.myreward.engine.event.opcode.CallRepeatModel;
import com.myreward.engine.event.opcode.CallRewardModel;
import com.myreward.engine.event.opcode.CallShowModel;
import com.myreward.engine.event.opcode.DebugModel;
import com.myreward.engine.event.opcode.DescriptionModel;
import com.myreward.engine.event.opcode.IfDurationModel;
import com.myreward.engine.event.opcode.IfEventModel;
import com.myreward.engine.event.opcode.IfGatekeeperModel;
import com.myreward.engine.event.opcode.IfRepeatModel;
import com.myreward.engine.event.opcode.IfRewardModel;
import com.myreward.engine.event.opcode.IncrementEventModel;
import com.myreward.engine.event.opcode.LabelDurationModel;
import com.myreward.engine.event.opcode.LabelFunctionModel;
import com.myreward.engine.event.opcode.LabelGatekeeperModel;
import com.myreward.engine.event.opcode.LabelMainModel;
import com.myreward.engine.event.opcode.LabelPriorityModel;
import com.myreward.engine.event.opcode.LabelRepeatModel;
import com.myreward.engine.event.opcode.LabelRewardModel;
import com.myreward.engine.event.opcode.LabelShowModel;
import com.myreward.engine.event.opcode.OpCodeBaseModel;
import com.myreward.engine.event.opcode.ResetDurationModel;
import com.myreward.engine.event.opcode.ResetEventModel;
import com.myreward.engine.event.opcode.ResetGatekeeperModel;
import com.myreward.engine.event.opcode.ResetPriorityModel;
import com.myreward.engine.event.opcode.ResetRewardModel;
import com.myreward.engine.event.opcode.ResetShowModel;
import com.myreward.engine.event.opcode.ReturnModel;
import com.myreward.engine.event.opcode.SetDurationModel;
import com.myreward.engine.event.opcode.SetEventModel;
import com.myreward.engine.event.opcode.SetGatekeeperModel;
import com.myreward.engine.event.opcode.SetPriorityModel;
import com.myreward.engine.event.opcode.SetRepeatModel;
import com.myreward.engine.event.opcode.SetRewardModel;
import com.myreward.engine.event.opcode.SetShowModel;

public class RuntimeSupportedOpCodeModel {
	private static RuntimeSupportedOpCodeModel runtimeSupportedOpCodeModel;
	private List<OpCodeBaseModel> opCodeList = Arrays.asList(new CallFunctionModel(),
												new CallGatekeeperModel(),
												new CallPriorityModel(),
												new CallRepeatModel(),
												new CallRewardModel(),
												new CallShowModel(),
												new CallDurationModel(),
												new IfDurationModel(),
												new IfEventModel(),
												new IfGatekeeperModel(),
												new IfRewardModel(),
												new IfRepeatModel(),
												new LabelDurationModel(),
												new LabelFunctionModel(),
												new LabelGatekeeperModel(),
												new LabelMainModel(),
												new LabelPriorityModel(),
												new LabelRepeatModel(),
												new LabelRewardModel(),
												new LabelShowModel(),
												new SetGatekeeperModel(),
												new SetPriorityModel(),
												new SetRewardModel(),
												new SetShowModel(),
												new SetDurationModel(),
												new ReturnModel(),
												new ResetDurationModel(),
												new ResetGatekeeperModel(),
												new ResetPriorityModel(),
												new ResetRewardModel(),
												new ResetShowModel(),
												new ResetEventModel(),
												new SetRepeatModel(),
												new SetEventModel(),
												new AddRewardModel(),
												new IncrementEventModel(),
												new DescriptionModel(),
												new DebugModel());
	
	protected RuntimeSupportedOpCodeModel() {
		
	}
	public static RuntimeSupportedOpCodeModel getInstance() {
	      if(runtimeSupportedOpCodeModel == null) {
	    	  runtimeSupportedOpCodeModel = new RuntimeSupportedOpCodeModel();
	      }
	      return runtimeSupportedOpCodeModel;
	   }

	public List<OpCodeBaseModel> getSupportedOpCodeHandlers() {
		return this.opCodeList;
	}
}
