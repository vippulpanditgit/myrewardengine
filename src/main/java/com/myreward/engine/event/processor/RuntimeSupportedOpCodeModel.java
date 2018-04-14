package com.myreward.engine.event.processor;

import java.util.Arrays;
import java.util.List;

import com.myreward.engine.event.opcode.processing.AddRewardModel;
import com.myreward.engine.event.opcode.processing.CallDurationModel;
import com.myreward.engine.event.opcode.processing.CallFunctionModel;
import com.myreward.engine.event.opcode.processing.CallGatekeeperModel;
import com.myreward.engine.event.opcode.processing.CallPriorityModel;
import com.myreward.engine.event.opcode.processing.CallRepeatModel;
import com.myreward.engine.event.opcode.processing.CallRewardModel;
import com.myreward.engine.event.opcode.processing.CallShowModel;
import com.myreward.engine.event.opcode.processing.CommentModel;
import com.myreward.engine.event.opcode.processing.DebugModel;
import com.myreward.engine.event.opcode.processing.DescriptionModel;
import com.myreward.engine.event.opcode.processing.IfDurationModel;
import com.myreward.engine.event.opcode.processing.IfEventModel;
import com.myreward.engine.event.opcode.processing.IfGatekeeperModel;
import com.myreward.engine.event.opcode.processing.IfRepeatModel;
import com.myreward.engine.event.opcode.processing.IfRewardModel;
import com.myreward.engine.event.opcode.processing.IncrementEventModel;
import com.myreward.engine.event.opcode.processing.LabelDurationModel;
import com.myreward.engine.event.opcode.processing.LabelFunctionModel;
import com.myreward.engine.event.opcode.processing.LabelGatekeeperModel;
import com.myreward.engine.event.opcode.processing.LabelMainModel;
import com.myreward.engine.event.opcode.processing.LabelPriorityModel;
import com.myreward.engine.event.opcode.processing.LabelRepeatModel;
import com.myreward.engine.event.opcode.processing.LabelRewardModel;
import com.myreward.engine.event.opcode.processing.LabelShowModel;
import com.myreward.engine.event.opcode.processing.OpCodeBaseModel;
import com.myreward.engine.event.opcode.processing.ResetDurationModel;
import com.myreward.engine.event.opcode.processing.ResetEventModel;
import com.myreward.engine.event.opcode.processing.ResetGatekeeperModel;
import com.myreward.engine.event.opcode.processing.ResetPriorityModel;
import com.myreward.engine.event.opcode.processing.ResetRewardModel;
import com.myreward.engine.event.opcode.processing.ResetShowModel;
import com.myreward.engine.event.opcode.processing.ReturnModel;
import com.myreward.engine.event.opcode.processing.SetDurationModel;
import com.myreward.engine.event.opcode.processing.SetEventModel;
import com.myreward.engine.event.opcode.processing.SetGatekeeperModel;
import com.myreward.engine.event.opcode.processing.SetPriorityModel;
import com.myreward.engine.event.opcode.processing.SetRepeatModel;
import com.myreward.engine.event.opcode.processing.SetRewardModel;
import com.myreward.engine.event.opcode.processing.SetShowModel;

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
												new DebugModel(),
												new CommentModel());
	
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
