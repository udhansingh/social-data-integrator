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
package org.onesun.smc.app.views.connectors;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SpringLayout;

import org.onesun.commons.swing.SpringLayoutUtils;
import org.onesun.smc.api.Connector;
import org.onesun.smc.api.ConnectorPanel;
import org.onesun.smc.app.AppCommons;
import org.onesun.smc.app.AppMessages;
import org.onesun.smc.core.connectors.RESTConnector;

public class RESTConnectorView extends ConnectorPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 8926936146403943618L;

	private JTextField usernameTextField = new JTextField();

	private JTextField passwordTextField = new JTextField();

	public RESTConnectorView(){
		super();
		onInitComplete();
	}
	
	@Override
	public void init(){
	}

	@Override
	protected void initControls(){
		final JPanel panel = new JPanel(new SpringLayout());
		JLabel label = null;

		label = new JLabel("Connection Name", JLabel.LEADING);
		label.setPreferredSize(new Dimension(150, 24));
		panel.add(label);
		connectionNameTextField.setPreferredSize(new Dimension(400, 24));
		label.setLabelFor(connectionNameTextField);
		panel.add(connectionNameTextField);

		label = new JLabel("Username", JLabel.LEADING);
		label.setPreferredSize(new Dimension(150, 24));
		panel.add(label);
		usernameTextField.setPreferredSize(new Dimension(300, 24));
		label.setLabelFor(usernameTextField);
		panel.add(usernameTextField);

		label = new JLabel("Password", JLabel.LEADING);
		label.setPreferredSize(new Dimension(150, 24));
		panel.add(label);
		passwordTextField.setPreferredSize(new Dimension(300, 24));
		label.setLabelFor(passwordTextField);
		panel.add(passwordTextField);

		int rows = 6;
		if(overrideSaveButton == true){
			JPanel buttonPanel = new JPanel(new BorderLayout(15, 15));
			buttonPanel.add(saveButton, BorderLayout.EAST);
			panel.add(buttonPanel);

			rows = 7;
		}

		SpringLayoutUtils.makeCompactGrid(panel, rows, 1, 5, 5,	5, 5);

		JPanel containerPanel = new JPanel(new BorderLayout());
		containerPanel.add(panel, BorderLayout.NORTH);

		this.add(containerPanel);
		
		connectionNameTextField.addPropertyChangeListener(new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
			}
		});

		connectionNameTextField.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				AppCommons.TASKLET.getConnection().setName(connectionNameTextField.getText().trim());
			}
		});

		if(saveButton != null){
			saveButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					String connectionName = connectionNameTextField.getText();
					String username = usernameTextField.getText();
					String password = passwordTextField.getText();

					try {
						if((connectionName == null && username == null) || connectionName.isEmpty()){
							JOptionPane.showMessageDialog(rootPanel, AppMessages.ERROR_INVALID_CONNECTION);
							return;
						}
						else {
							RESTConnector connection = new RESTConnector();
							connection.setName(connectionName);
							connection.setUsername(username);
							connection.setPassword(password);
							connection.setIdentity("General");

							connectionWriter.saveToFile(connection);
						}
					}
					catch(NullPointerException npe){
						JOptionPane.showMessageDialog(rootPanel, "Could not save connection!");
					}finally {
					}
				}
			});
		}
	}

	@Override
	public void updateFields(Connector connector) {
		if(connector instanceof RESTConnector){
			RESTConnector connection = (RESTConnector)connector;

			connectionNameTextField.setText(connection.getName());
			usernameTextField.setText(connection.getUsername());
			passwordTextField.setText(connection.getPassword());
		}
	}

	public void resetFields() {
		connectionNameTextField.setText("");
		usernameTextField.setText("");
		passwordTextField.setText("");		
	}
}
