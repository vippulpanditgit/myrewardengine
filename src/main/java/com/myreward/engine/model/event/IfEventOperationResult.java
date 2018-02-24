package com.myreward.engine.model.event;

public class IfEventOperationResult extends OperationResultDO {
	private boolean result;
	private int nextOperationNumber;
	public boolean isResult() {
		return result;
	}
	public void setResult(boolean result) {
		this.result = result;
	}
	public int getNextOperationNumber() {
		return nextOperationNumber;
	}
	public void setNextOperationNumber(int nextOperationNumber) {
		this.nextOperationNumber = nextOperationNumber;
	}

}
