package com.myreward.parser.symbol;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.myreward.parser.grammar.RewardSymbolAttribute;
import com.myreward.parser.model.RepeatOn;
import com.myreward.parser.model.Reward;

public class Symbol implements Serializable{
	public enum SymbolType {
		IMPORT,
		EVENT,
		DERIVED_EVENT,
		PACKAGE,
		UNKNOWN,
		GATEKEEPER
	}
	// The symbol's name
	private String name;
	private String packageName;
	private SymbolType type;
	private int level=0;
	private int id;
	private int fullyQualifiedId;
	// version would be 0 for the base event symbol, incremented by 1 for each occurence.
	public int version=0;
	// If the event is used in more than 1 place, an alias is created for 
	//     each instance and the version field is incremented.
	public List<Symbol> childrenList = null;
	// Container of the symbol. 
	private Symbol parent;
	// True if the symbol is used in its block
	private String namespace;
	private boolean isReferenced;
	// True if the symbol is created from an import statement
	private boolean isImported;
	// True if the symbol is a parameter;
	private boolean isParameter;
	// True if the symbol is global
	private boolean isGlobal;
	// True if the symbol is declared global with a global statement
	private boolean isDeclaredGlobal;
	// True if the symbol is local to its block
	private boolean isLocal;
	// True if the symbol is referenced in its block, but not assigned to
	private boolean isFree;
	// True if the symbol is assigned to in its block
	private boolean isAssigned;
	// True if name binding introduces new namespace
	private boolean isNameSpace;
	
	public List<String> callDeclarationList = new ArrayList<String>();
	
	private RewardSymbolAttribute rewardAttribute;
	public Symbol() {
		rewardAttribute = new RewardSymbolAttribute();
	}
	public Symbol(String symbolName) {
		this.setName(symbolName);
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
		setId(this.name.hashCode());
	}
	public String getFullyQualifiedName() {
		String fullResourceName = name; 
		if(parent!=null)
			fullResourceName = parent.getFullyQualifiedName()+"."+name;
		return fullResourceName;
		
	}
	public SymbolType getType() {
		return type;
	}
	public void setType(SymbolType type) {
		this.type = type;
	}
	public int getLevel() {
		return level;
	}
	public void setLevel(int level) {
		this.level = level;
	}
	public Symbol getContainer() {
		return parent;
	}
	public void setContainer(Symbol container) {
		this.parent = container;
	}
	public boolean isReferenced() {
		return isReferenced;
	}
	public void setReferenced(boolean isReferenced) {
		this.isReferenced = isReferenced;
	}
	public boolean isImported() {
		return isImported;
	}
	public void setImported(boolean isImported) {
		this.isImported = isImported;
	}
	public boolean isParameter() {
		return isParameter;
	}
	public void setParameter(boolean isParameter) {
		this.isParameter = isParameter;
	}
	public boolean isGlobal() {
		return isGlobal;
	}
	public void setGlobal(boolean isGlobal) {
		this.isGlobal = isGlobal;
	}
	public boolean isDeclaredGlobal() {
		return isDeclaredGlobal;
	}
	public void setDeclaredGlobal(boolean isDeclaredGlobal) {
		this.isDeclaredGlobal = isDeclaredGlobal;
	}
	public boolean isLocal() {
		return isLocal;
	}
	public void setLocal(boolean isLocal) {
		this.isLocal = isLocal;
	}
	public boolean isFree() {
		return isFree;
	}
	public void setFree(boolean isFree) {
		this.isFree = isFree;
	}
	public boolean isAssigned() {
		return isAssigned;
	}
	public void setAssigned(boolean isAssigned) {
		this.isAssigned = isAssigned;
	}
	public boolean isNameSpace() {
		return isNameSpace;
	}
	public void setNameSpace(boolean isNameSpace) {
		this.isNameSpace = isNameSpace;
	}
	public boolean isShow() {
		return rewardAttribute.isShow();
	}
	public void setShow(boolean isShow) {
		rewardAttribute.setShow(isShow);
	}
	public Date getEffectiveDate() {
		return rewardAttribute.getEffectiveDate();
	}
	public void setEffectiveDate(Date effectiveDate) {
		rewardAttribute.setEffectiveDate(effectiveDate);
	}
	public Date getExpirationDate() {
		return rewardAttribute.getExpirationDate();
	}
	public void setExpirationDate(Date expirationDate) {
		rewardAttribute.setExpirationDate(expirationDate);
	}
	public RepeatOn getRepeatOn() {
		return rewardAttribute.getRepeatOn();
	}
	public void setRepeatOn(RepeatOn repeatOn) {
		rewardAttribute.setRepeatOn(repeatOn);
	}
	public Reward getReward() {
		return rewardAttribute.getReward();
	}
	public void setReward(Reward reward) {
		rewardAttribute.setReward(reward);
	}
	public int getPriority() {
		return rewardAttribute.getPriority();
	}
	public void setPriority(int priority) {
		rewardAttribute.setPriority(priority);
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getFullyQualifiedId() {
		String fullResourceName = name; 
		if(namespace!=null)
			fullResourceName = namespace+"."+name;
		fullyQualifiedId = fullResourceName.hashCode();
		return fullyQualifiedId;
	}
	public String toString() {
		return this.getName()+"<<"+this.packageName+"<<"+this.getId()+"<<"+getFullyQualifiedId()+"<<"
					+this.getNamespace()+"<<"+this.getType()+"<<"
					+this.getLevel()+"<<"+rewardAttribute+"<<"+callDeclarationList+"\n"+childrenList;
	}
	public int getVersion() {
		return version;
	}
	public void setVersion(int version) {
		this.version = version;
	}
	public List<Symbol> getChildrenList() {
		if(this.childrenList==null)
			this.childrenList = new ArrayList<Symbol>();
		return childrenList;
	}
	public void setChildrenList(List<Symbol> children) {
		if(this.childrenList==null)
			this.childrenList = new ArrayList<Symbol>();
		childrenList.forEach(p -> p.parent = this);
		childrenList.forEach(p -> p.namespace = this.name);
		this.childrenList = children;
	}
	public void addChildren(List<Symbol> childrenList) {
		if(this.childrenList==null)
			this.childrenList = new ArrayList<Symbol>();
		childrenList.forEach(p -> p.parent = this);
		childrenList.forEach(p -> p.namespace = this.namespace==null?this.name: this.namespace+"."+this.name);
		this.childrenList.addAll(childrenList);
	}
	public void addChild(Symbol child) {
		Symbol parent = this;
		while(parent.level >= child.level)
			parent = parent.parent;
		child.parent = parent;
		child.namespace = parent.namespace==null?parent.name: parent.namespace+"."+parent.name;
		if(parent.childrenList==null)
			parent.childrenList = new ArrayList<Symbol>();
		parent.setType(SymbolType.DERIVED_EVENT);
		parent.childrenList.add(child);
	}
	public String getNamespace() {
		return namespace;
	}
	public void setNamespace(String namespace) {
		this.namespace = namespace;
	}
	public Symbol getParent() {
		return parent;
	}
	public void setParent(Symbol parent) {
		this.parent = parent;
	}
	public String getPackageName() {
		return packageName;
	}
	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}
}
