package com.myreward.engine.app;

import java.util.function.Consumer;

public class AppInstanceContextBuilder {
	public AppInstanceContextBuilder with (Consumer<AppInstanceContextBuilder> appInstanceContextBuilderFunction) {
		appInstanceContextBuilderFunction.accept(this);
		return this;
	}
	public AppInstanceContext createAppInstanceContext() {
		return new AppInstanceContext();
	}
}
