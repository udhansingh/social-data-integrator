/*
   Copyright 2011 Udayakumar Dhansingh (Udy)

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
   
 */
package org.onesun.smc.api;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import org.onesun.smc.app.AppIcons;
import org.onesun.smc.app.handlers.UserActionListener;
import org.onesun.smc.app.views.shared.UIConnectionWriter;
import org.onesun.smc.app.views.shared.UIConnectionWriterImpl;

public abstract class ConnectorPanel extends JPanel implements ConnectorView, Cloneable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -6626522842243299823L;

	protected UIConnectionWriter connectionWriter = new UIConnectionWriterImpl();
	protected UserActionListener userActionListener = null;
	
	protected JButton saveButton = new JButton("Save", AppIcons.getIcon("save"));
	
	protected JButton authorizeButton = new JButton("Authorize...", AppIcons.getIcon("authorize"));
	protected JTextField connectionNameTextField = new JTextField();
	protected ConnectorPanel rootPanel = this;
	
	protected boolean overrideSaveButton = true;
	
	protected JComboBox<String> identityComboBox = new JComboBox<String>();

	public JComboBox<String> getIdentityComboBox(){
		return identityComboBox;
	}
	
	public ConnectorPanel() {
		super();

		this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		
		identityComboBox.setEnabled(false);
	}

	
	public final void onInitComplete(){
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				initControls();
			}
		});
	}
	
	protected abstract void initControls();

	@Override
	public ConnectorPanel getView() {
		return this;
	}

	@Override
	public abstract void updateFields(Connector connector);
	
	@Override
	public Object clone() {
		try {
			return super.clone();
		} catch(CloneNotSupportedException e) {
			return this;
		}
	}
}
