package com.myreward.engine.model;

import java.io.Serializable;

public class Reward implements Serializable {
	private boolean status;
	private double amount;
	public boolean isStatus() {
		return status;
	}
	public void setStatus(boolean status) {
		this.status = status;
	}
	public double getAmount() {
		return amount;
	}
	public void setAmount(double amount) {
		this.amount = amount;
	}
	public String toString() {
		return amount+"<<"+status;
	}
}
