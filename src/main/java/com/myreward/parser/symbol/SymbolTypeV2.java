package com.myreward.parser.symbol;

public class SymbolTypeV2 {
	public static byte IMPORT = 0x01;
	public static byte EVENT = 0x02;
	public static byte GROUP = 0x04;
	public static byte PACKAGE = 0x08;
	public static byte GATEKEEPER = 0x10;

	private byte value;
	
	public void reset() {
		this.value = 0x00;
	}
	public void addImportType() {
		this.value |= SymbolTypeV2.IMPORT;
	}
	public void addEventType() {
		this.value |= SymbolTypeV2.EVENT;
	}
	public void addGroupType() {
		this.value |= SymbolTypeV2.GROUP;
	}
	public void addPackageType() {
		this.value |= SymbolTypeV2.PACKAGE;
	}
	public void addGatekeeperType() {
		this.value |= SymbolTypeV2.GATEKEEPER;
	}
/*	public boolean isSymbolType(byte type) {
		byte result = 0;
		if(type==SymbolTypeV2.IMPORT){
			result = value SymbolTypeV2.IMPORT;
			if(this.value!=0)
				return true;
		} else if(value==SymbolTypeV2.EVENT){
			this.value &= SymbolTypeV2.EVENT;
			if(this.value!=0)
				return true;
		} else if(value==SymbolTypeV2.GROUP){
			this.value &= SymbolTypeV2.GROUP;
			if(this.value!=0)
				return true;
		} else if(value==SymbolTypeV2.PACKAGE){
			this.value &= SymbolTypeV2.PACKAGE;
			if(this.value!=0)
				return true;
		} else if(value==SymbolTypeV2.GATEKEEPER){
			this.value &= SymbolTypeV2.GATEKEEPER;
			if(this.value!=0)
				return true;
		}
		return false;
	}*/
	public int getSymbolType() {
		return this.value;
	}
	public void setSymbolType(byte value) {
		this.value = value;
	}
	public String toString() {
		StringBuffer serialized = new StringBuffer();
		serialized.append((this.value&SymbolTypeV2.IMPORT)!=0?"IMPORT ":"");
		serialized.append((this.value&SymbolTypeV2.GATEKEEPER)!=0?"GATEKEEPER ":"");
		serialized.append((this.value&SymbolTypeV2.PACKAGE)!=0?"PACKAGE ":"");
		serialized.append((this.value&SymbolTypeV2.GROUP)!=0?"GROUP ":"");
		serialized.append((this.value&SymbolTypeV2.EVENT)!=0?"EVENT":"");
		return serialized.toString();
	}

}
