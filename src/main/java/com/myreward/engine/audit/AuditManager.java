package com.myreward.engine.audit;

public class AuditManager implements IAuditManager {
	private IAuditManager auditManager;
	
	private AuditManager() {
		auditManager = new AuditManager();
	}
	
	public IAuditManager getInstance() {
		if(auditManager==null)
			auditManager = new AuditManager();
		return auditManager;
	}
	@Override
	public boolean audit(AuditEvent event) {
		// TODO Auto-generated method stub
		return false;
	}


}
