package org.onesun.smc.api;


public interface ConnectorView {
	void updateFields(Connector connector);
	ConnectorPanel getView();
	void init();
}
