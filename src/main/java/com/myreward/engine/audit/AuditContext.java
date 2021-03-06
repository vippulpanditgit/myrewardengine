package com.myreward.engine.audit;

import com.myreward.parser.util.StopWatch;

public final class AuditContext {
		
	
	/** The audit output stream. */ 
 	private static IAuditOutputStream<AuditEvent> auditStream; 

	private AuditContext() {
		init();
	}
	final static void init() {
		StopWatch stopWatch = new StopWatch();
		stopWatch.start();
		auditStream = new AuditProcessOuptutStream();
		stopWatch.stop();
	}
	public static IAuditOutputStream<AuditEvent> getAuditStream() {
		return auditStream;
	}
	public static void setAuditStream(IAuditOutputStream<AuditEvent> auditStream) {
		AuditContext.auditStream = auditStream;
	}

}
