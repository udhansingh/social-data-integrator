package org.onesun.smc.api;


public interface ConnectionPropertiesView {
	void updateFields(ConnectionProperties properties);
	ConnectionPropertiesPanel getView();
	void init();
}
