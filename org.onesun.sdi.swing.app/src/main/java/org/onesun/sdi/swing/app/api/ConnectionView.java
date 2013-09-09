package org.onesun.sdi.swing.app.api;

import org.onesun.sdi.core.api.Connection;


public interface ConnectionView {
	void updateFields(Connection properties);
	ConnectionViewPanel getView();
	void init();
}
