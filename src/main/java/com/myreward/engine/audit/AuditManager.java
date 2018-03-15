package com.myreward.engine.audit;

public class AuditManager implements IAuditManager {
	private static IAuditManager auditManager;

	private AuditManager() {
		auditManager = new AuditManager();
	}
	
	public static IAuditManager getInstance() {
		IAuditManager result = auditManager;
		if(result==null) {
			synchronized(AuditManager.class) {
				result = auditManager;
				AuditContext.init();
				auditManager = result = new AuditManager();
			}
			auditManager = new AuditManager();
		}
		return auditManager;
	}
	@Override
	public boolean audit(AuditEvent event) {
		AuditContext.getAuditStream().write(event);
		return true;
	}
	public boolean audit(EventBatch<AuditEvent> batch) {
		AuditContext.getAuditStream().write(batch);
		return true;
	}


}
