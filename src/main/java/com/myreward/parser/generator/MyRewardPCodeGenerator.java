package com.myreward.parser.generator;

import java.util.ArrayList;
import java.util.List;

public class MyRewardPCodeGenerator implements java.io.Serializable  {
	private String version="0.00";
	private byte debugFlag;
	private List<String> codeSegment = new ArrayList<String>();

	public void setDebugFlag() {
		debugFlag = (byte)0xff;
	}
	public void resetDebugFlag() {
		debugFlag = 0x00;
	}
	public List<String> getCodeSegment() {
		return codeSegment;
	}
	public void setCodeSegment(List<String> codeSegment) {
		this.codeSegment = codeSegment;
	}
}
