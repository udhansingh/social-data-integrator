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
package org.onesun.sdi.swing.app.views.connection;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SpringLayout;

import org.onesun.commons.swing.SpringLayoutUtils;
import org.onesun.sdi.core.api.Connection;
import org.onesun.sdi.core.api.ServiceProvider;
import org.onesun.sdi.core.model.connection.GnipConnection;
import org.onesun.sdi.spi.api.ProviderFactory;
import org.onesun.sdi.swing.app.AppCommons;
import org.onesun.sdi.swing.app.AppMessages;
import org.onesun.sdi.swing.app.api.ConnectionViewPanel;

public class GnipConnectionView extends ConnectionViewPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 8926936146403943618L;

	private JTextField usernameTextField = new JTextField();
	private JTextField passwordTextField = new JTextField();
	private JTextField urlTextField = new JTextField();
	private JTextField compressionEnabledTextField = new JTextField();

	public GnipConnectionView(){
		super();
		onInitComplete();
	}
	
	@Override
	public void init(){
		List<ServiceProvider> providers = ProviderFactory.getProviderNamesByCategory("GNIP");
		
		for(ServiceProvider provider : providers){
			if(providers.size() == 1){
				identityComboBox.addItem(provider.getIdentity());
			}
		}
	}

	@Override
	protected void initControls(){
		connectionNameTextField.addPropertyChangeListener(new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
			}
		});

		connectionNameTextField.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				AppCommons.TASKLET.getConnectionProperties().setName(connectionNameTextField.getText().trim());
			}
		});

		if(saveButton != null) {
			saveButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					String connectionName = connectionNameTextField.getText();
					String username = usernameTextField.getText();
					String password = passwordTextField.getText();
					String url = urlTextField.getText();
					String ceStatus = compressionEnabledTextField.getText();

					try {
						if((connectionName == null && username == null && url == null) || connectionName.isEmpty() || url.isEmpty()) {
							JOptionPane.showMessageDialog(rootPanel, AppMessages.ERROR_INVALID_CONNECTION);
							return;
						}
						else {
							GnipConnection connection = new GnipConnection();
							connection.setName(connectionName);
							connection.setUsername(username);
							connection.setPassword(password);
							connection.setUrl(url);
							
							if(ceStatus.compareToIgnoreCase("true") == 0 || ceStatus.compareToIgnoreCase("yes") == 0){
								connection.setCompressionEnabled(true);
							} else {
								connection.setCompressionEnabled(false);
							}
							
							connection.setIdentity("Gnip");

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

		final JPanel panel = new JPanel(new SpringLayout());
		JLabel label = null;

		label = new JLabel("Connection Name", JLabel.LEADING);
		label.setPreferredSize(new Dimension(150, 24));
		panel.add(label);
		connectionNameTextField.setPreferredSize(new Dimension(400, 24));
		label.setLabelFor(connectionNameTextField);
		panel.add(connectionNameTextField);

		label = new JLabel("Service", JLabel.LEADING);
		label.setPreferredSize(new Dimension(150, 24));
		panel.add(label);
		identityComboBox.setPreferredSize(new Dimension(500, 24));
		label.setLabelFor(identityComboBox);
		panel.add(identityComboBox);

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

		label = new JLabel("Url", JLabel.LEADING);
		label.setPreferredSize(new Dimension(150, 24));
		panel.add(label);
		urlTextField.setPreferredSize(new Dimension(300, 24));
		label.setLabelFor(urlTextField);
		panel.add(urlTextField);

		label = new JLabel("Is data compressed ", JLabel.LEADING);
		label.setPreferredSize(new Dimension(150, 24));
		panel.add(label);
		compressionEnabledTextField.setPreferredSize(new Dimension(300, 24));
		label.setLabelFor(compressionEnabledTextField);
		panel.add(compressionEnabledTextField);
		
		int rows = 12;
		if(overrideSaveButton == true){
			JPanel buttonPanel = new JPanel(new BorderLayout(15, 15));
			buttonPanel.add(saveButton, BorderLayout.EAST);
			panel.add(buttonPanel);

			rows = 13;
		}

		SpringLayoutUtils.makeCompactGrid(panel, rows, 1, 5, 5,	5, 5);

		JPanel containerPanel = new JPanel(new BorderLayout());
		containerPanel.add(panel, BorderLayout.NORTH);

		this.add(containerPanel);
	}

	@Override
	public void updateFields(Connection connector) {
		if(connector instanceof GnipConnection){
			GnipConnection connection = (GnipConnection)connector;

			final String provider = connection.getIdentity();
			if(identityComboBox != null)			identityComboBox.setSelectedItem(provider);

			connectionNameTextField.setText(connection.getName());
			usernameTextField.setText(connection.getUsername());
			passwordTextField.setText(connection.getPassword());
			urlTextField.setText(connection.getUrl());
			compressionEnabledTextField.setText(Boolean.toString(connection.isCompressionEnabled()));
		}
	}

	public void resetFields() {
		connectionNameTextField.setText("");
		usernameTextField.setText("");
		passwordTextField.setText("");
		urlTextField.setText("");
		compressionEnabledTextField.setText("");
	}
}
